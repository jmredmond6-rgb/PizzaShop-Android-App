package com.example.pizzashop.UI;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.pizzashop.databinding.RowOrderItemBinding;
import com.example.pizzashop.entities.OrderItem;

import java.util.List;

import Util.MoneyUtil;
/* Used to bind the order item details to the order details list. */
public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.VH> {

    private final List<OrderItem> items;

    public OrderDetailsAdapter(List<OrderItem> items) {
        this.items = items;
    }

    static class VH extends RecyclerView.ViewHolder {
        RowOrderItemBinding b;
        VH(RowOrderItemBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowOrderItemBinding b = RowOrderItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        OrderItem oi = items.get(position);

        holder.b.tvName.setText(oi.pizzaNameSnapshot);
        holder.b.tvQty.setText("Qty: " + oi.quantity);

        holder.b.tvUnitPrice.setText("Unit: " + MoneyUtil.dollars(oi.unitPriceDollarsSnapshot));

        double line = oi.unitPriceDollarsSnapshot * oi.quantity;
        line = Math.round(line * 100.0) / 100.0;
        holder.b.tvLineTotal.setText(MoneyUtil.dollars(line));


    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
