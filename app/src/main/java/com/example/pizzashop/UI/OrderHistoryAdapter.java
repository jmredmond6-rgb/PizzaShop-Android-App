package com.example.pizzashop.UI;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.pizzashop.databinding.RowOrderBinding;
import com.example.pizzashop.entities.Order;
import com.example.pizzashop.entities.OrderItem;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import Util.MoneyUtil;

/* Used to bind the order summary data to the order history list. */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.VH> {

    public static class OrderRow {
        public final Order order;
        public final List<OrderItem> items;

        public OrderRow(Order order, List<OrderItem> items) {
            this.order = order;
            this.items = items;
        }
    }

    private final List<OrderRow> rows;
    private final long customerId;

    public OrderHistoryAdapter(List<OrderRow> rows, long customerId) {
        this.rows = rows;
        this.customerId = customerId;
    }

    static class VH extends RecyclerView.ViewHolder {
        final RowOrderBinding b;
        VH(RowOrderBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowOrderBinding b = RowOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        OrderRow row = rows.get(position);


        holder.b.tvOrderTotal.setText(MoneyUtil.dollars(row.order.totalDollars));
        holder.b.tvOrderDate.setText(
                DateFormat.getDateTimeInstance().format(new Date(row.order.createdAtEpochMs))
        );


        StringBuilder sb = new StringBuilder();
        if (row.items != null) {
            for (OrderItem oi : row.items) {
                sb.append("• ")
                        .append(oi.quantity)
                        .append(" x ")
                        .append(oi.pizzaNameSnapshot)
                        .append("\n");
            }
        }
        holder.b.tvItems.setText(sb.toString().trim());


        holder.itemView.setClickable(true);
        holder.itemView.setFocusable(true);

        holder.itemView.setOnClickListener(v -> {
            Context ctx = v.getContext();
            Intent i = new Intent(ctx, OrderDetailsActivity.class);
            i.putExtra(OrderDetailsActivity.EXTRA_CUSTOMER_ID, customerId);
            i.putExtra(OrderDetailsActivity.EXTRA_ORDER_ID, row.order.id);
            ctx.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return rows == null ? 0 : rows.size();
    }
}
