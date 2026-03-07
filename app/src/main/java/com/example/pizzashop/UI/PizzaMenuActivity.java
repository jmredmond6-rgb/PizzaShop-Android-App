package com.example.pizzashop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pizzashop.R;
import com.example.pizzashop.database.PizzaShopRepository;
import com.example.pizzashop.databinding.ActivityPizzaMenuBinding;
import com.example.pizzashop.entities.Customer;
import com.example.pizzashop.entities.Pizza;

import java.util.ArrayList;
import java.util.List;

/* Shows available pizzas, search functionality, and add-to-cart actions. */
public class PizzaMenuActivity extends AppCompatActivity implements PizzaAdapter.OnPizzaClick {

    public static final String EXTRA_CUSTOMER_ID = "customer_id";

    private ActivityPizzaMenuBinding b;
    private PizzaShopRepository repo;
    private long customerId;
    private List<Pizza> allPizzas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPizzaMenuBinding.inflate(getLayoutInflater());
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

        Customer customer = repo.getCustomer(customerId);
        if (customer != null && customer.name != null && !customer.name.trim().isEmpty()) {
            b.tvWelcome.setText("Welcome back, " + customer.name.trim());
        }

        allPizzas = repo.getActivePizzas();
        filterAndShow("");

        b.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAndShow(s == null ? "" : s.toString());
            }
        });

        b.btnGoCart.setOnClickListener(v -> {
            Intent i = new Intent(this, CartActivity.class);
            i.putExtra(CartActivity.EXTRA_CUSTOMER_ID, customerId);
            startActivity(i);
        });
    }

    private void filterAndShow(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        List<Pizza> filtered = new ArrayList<>();

        for (Pizza p : allPizzas) {
            String name = p.name == null ? "" : p.name.toLowerCase();
            String desc = p.description == null ? "" : p.description.toLowerCase();
            if (q.isEmpty() || name.contains(q) || desc.contains(q)) {
                filtered.add(p);
            }
        }

        b.recycler.setAdapter(new PizzaAdapter(filtered, this));

        boolean empty = filtered.isEmpty();
        b.recycler.setVisibility(empty ? android.view.View.GONE : android.view.View.VISIBLE);
        b.emptyState.setVisibility(empty ? android.view.View.VISIBLE : android.view.View.GONE);

        if (empty && !q.isEmpty()) {
            b.emptyState.setText("No pizzas match \"" + query.trim() + "\".");
        } else {
            b.emptyState.setText("No pizzas available.");
        }

        String countLabel = filtered.size() + (filtered.size() == 1 ? " pizza shown" : " pizzas shown");
        if (!q.isEmpty() && !empty) {
            countLabel += " for \"" + query.trim() + "\"";
        }
        b.tvMenuMeta.setText(countLabel);
    }

    @Override
    public void onAddToCart(Pizza pizza) {
        repo.addToCart(customerId, pizza.id);
        Toast.makeText(this, pizza.name + " added to cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_history) {
            Intent i = new Intent(this, OrderHistoryActivity.class);
            i.putExtra(OrderHistoryActivity.EXTRA_CUSTOMER_ID, customerId);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
