package com.example.pizzashop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pizzashop.database.PizzaShopRepository;
import com.example.pizzashop.databinding.ActivityCartBinding;
import com.example.pizzashop.entities.CartItem;
import com.example.pizzashop.entities.Pizza;

import java.util.ArrayList;
import java.util.List;

import Util.MoneyUtil;

/* Shows cart items, the total, clear cart, and checkout actions. */
public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartAction {

    public static final String EXTRA_CUSTOMER_ID = "customer_id";

    private ActivityCartBinding b;
    private PizzaShopRepository repo;
    private long customerId;
    private double currentTotal = 0.0;
    private int currentItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setSupportActionBar(b.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        customerId = getIntent().getLongExtra(EXTRA_CUSTOMER_ID, -1);
        if (customerId == -1) {
            Toast.makeText(this, "Missing customer id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        repo = new PizzaShopRepository(this);
        b.recycler.setLayoutManager(new LinearLayoutManager(this));

        b.btnGoMenu.setOnClickListener(v -> finish());

        b.btnClearCart.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Clear cart?")
                .setMessage("This will remove all items currently in your cart.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Clear", (dialog, which) -> {
                    repo.clearCart(customerId);
                    refresh();
                    Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
                })
                .show());

        b.btnCheckout.setOnClickListener(v -> {
            long orderId = repo.checkout(customerId);
            if (orderId == -1) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                String summary = "Order #" + orderId
                        + " placed successfully.\n\n"
                        + currentItemCount + (currentItemCount == 1 ? " item" : " items")
                        + " • " + MoneyUtil.dollars(currentTotal)
                        + "\nSaved to order history.";
                new AlertDialog.Builder(this)
                        .setTitle("Thanks for your order")
                        .setMessage(summary)
                        .setPositiveButton("View history", (dialog, which) -> {
                            Intent intent = new Intent(this, OrderHistoryActivity.class);
                            intent.putExtra(EXTRA_CUSTOMER_ID, customerId);
                            startActivity(intent);
                        })
                        .setNegativeButton("Done", null)
                        .show();
                refresh();
            }
        });

        refresh();
    }

    private void refresh() {
        List<CartItem> cart = repo.getCart(customerId);
        List<CartLine> lines = new ArrayList<>();
        double total = 0.0;

        for (CartItem ci : cart) {
            Pizza p = repo.getPizza(ci.pizzaId);
            if (p == null) continue;

            double lineTotal = p.priceDollars * ci.quantity;
            total += lineTotal;
            lines.add(new CartLine(ci.id, p.name, p.priceDollars, ci.quantity));
        }

        total = Math.round(total * 100.0) / 100.0;
        currentTotal = total;
        currentItemCount = 0;
        for (CartLine line : lines) {
            currentItemCount += line.qty;
        }

        b.tvTotal.setText(MoneyUtil.dollars(total));
        b.tvItemCount.setText(currentItemCount + (currentItemCount == 1 ? " item" : " items"));
        b.recycler.setAdapter(new CartAdapter(lines, this));

        boolean empty = lines.isEmpty();
        b.emptyState.setVisibility(empty ? android.view.View.VISIBLE : android.view.View.GONE);
        b.recycler.setVisibility(empty ? android.view.View.GONE : android.view.View.VISIBLE);
        b.btnCheckout.setEnabled(!empty);
        b.btnClearCart.setEnabled(!empty);
        b.tvItemCount.setVisibility(empty ? android.view.View.GONE : android.view.View.VISIBLE);
    }

    @Override
    public void onQtyChanged(long cartItemId, int newQty) {
        repo.updateCartQuantity(cartItemId, newQty);
        refresh();
    }

    public static class CartLine {
        public long cartItemId;
        public String name;
        public double unitPriceDollars;
        public int qty;

        public CartLine(long cartItemId, String name, double unitPriceDollars, int qty) {
            this.cartItemId = cartItemId;
            this.name = name;
            this.unitPriceDollars = unitPriceDollars;
            this.qty = qty;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
