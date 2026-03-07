package com.example.pizzashop.UI;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.pizzashop.database.PizzaShopRepository;
import com.example.pizzashop.databinding.ActivityHistoryBinding;
import com.example.pizzashop.entities.Order;
import com.example.pizzashop.entities.OrderItem;

import java.util.ArrayList;
import java.util.List;

/*  Used to display a summary list for the customer's order history. */
public class OrderHistoryActivity extends AppCompatActivity {

    public static final String EXTRA_CUSTOMER_ID = "customer_id";

    private ActivityHistoryBinding b;
    private PizzaShopRepository repo;
    private long customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        b = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        // Reads customer id from intent
        customerId = getIntent().getLongExtra(EXTRA_CUSTOMER_ID, -1);
        if (customerId == -1) {
            Toast.makeText(this, "Missing customer id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        setSupportActionBar(b.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        repo = new PizzaShopRepository(this);

        b.recycler.setLayoutManager(new LinearLayoutManager(this));

        refresh();
    }

    private void refresh() {

        List<Order> orders = repo.getOrders(customerId);

        List<OrderHistoryAdapter.OrderRow> rows = new ArrayList<>();
        for (Order o : orders) {
            List<OrderItem> items = repo.getOrderItems(o.id);
            rows.add(new OrderHistoryAdapter.OrderRow(o, items));

        }

        b.recycler.setAdapter(new OrderHistoryAdapter(rows, customerId));

        boolean empty = rows.isEmpty();
        b.emptyState.setVisibility(empty ? android.view.View.VISIBLE : android.view.View.GONE);
        b.recycler.setVisibility(empty ? android.view.View.GONE : android.view.View.VISIBLE);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}



