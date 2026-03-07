package com.example.pizzashop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.pizzashop.database.PizzaShopRepository;
import com.example.pizzashop.databinding.ActivityOrderDetailsBinding;
import com.example.pizzashop.entities.Order;
import com.example.pizzashop.entities.OrderItem;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import Util.MoneyUtil;

/*  Shows purchased items and allows for the reorders.  */

public class OrderDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_CUSTOMER_ID = "customer_id";
    public static final String EXTRA_ORDER_ID = "order_id";

    private ActivityOrderDetailsBinding b;
    private PizzaShopRepository repo;

    private long customerId;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setSupportActionBar(b.toolbar);

        customerId = getIntent().getLongExtra(EXTRA_CUSTOMER_ID, -1);
        orderId = getIntent().getLongExtra(EXTRA_ORDER_ID, -1);

        if (customerId == -1 || orderId == -1) {
            Toast.makeText(this, "Missing order info", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        repo = new PizzaShopRepository(this);


        setSupportActionBar(b.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        b.recycler.setLayoutManager(new LinearLayoutManager(this));

        bindOrder();



        b.btnReorder.setOnClickListener(v -> {
            int added = repo.reorderFromOrder(customerId, orderId);
            if (added == 0) {
                Toast.makeText(this, "Nothing to reorder.", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Reorder added to cart!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, CartActivity.class);
            i.putExtra(CartActivity.EXTRA_CUSTOMER_ID, customerId);
            startActivity(i);
        });

        b.btnBack.setOnClickListener(v -> finish());
    }

    private void bindOrder() {
        Order order = repo.getOrderById(orderId);
        if (order == null) {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //  ensure this order belongs to the same user
        if (order.customerId != customerId) {
            Toast.makeText(this, "Order does not belong to this account", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        b.tvOrderId.setText("Order #" + order.id);
        b.tvOrderDate.setText(DateFormat.getDateTimeInstance().format(new Date(order.createdAtEpochMs)));
        b.tvOrderTotal.setText(MoneyUtil.dollars(order.totalDollars));

        List<OrderItem> items = repo.getOrderItems(orderId);
        b.recycler.setAdapter(new OrderDetailsAdapter(items));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
