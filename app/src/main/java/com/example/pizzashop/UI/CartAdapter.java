package com.example.pizzashop.UI;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzashop.databinding.RowCartItemBinding;

import java.util.List;

import Util.MoneyUtil;

/* Used to bind the cart item data to the RecyclerView. */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    public interface OnCartAction {
        void onQtyChanged(long cartItemId, int newQty);
    }

    private final List<CartActivity.CartLine> lines;
    private final OnCartAction listener;

    public CartAdapter(List<CartActivity.CartLine> lines, OnCartAction listener) {
        this.lines = lines;
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        RowCartItemBinding b;
        VH(RowCartItemBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowCartItemBinding b = RowCartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CartActivity.CartLine line = lines.get(position);
        holder.b.tvName.setText(line.name);
        holder.b.tvUnitPrice.setText(MoneyUtil.dollars(line.unitPriceDollars) + " each");
        holder.b.tvQty.setText(String.valueOf(line.qty));

        double lineTotal = line.unitPriceDollars * line.qty;
        lineTotal = Math.round(lineTotal * 100.0) / 100.0;
        holder.b.tvLineTotal.setText(MoneyUtil.dollars(lineTotal));

        holder.b.btnMinus.setOnClickListener(v -> {
            int newQty = line.qty - 1;
            if (listener != null) listener.onQtyChanged(line.cartItemId, newQty);
        });

        holder.b.btnPlus.setOnClickListener(v -> {
            int newQty = line.qty + 1;
            if (listener != null) listener.onQtyChanged(line.cartItemId, newQty);
        });
    }

    @Override
    public int getItemCount() {
        return lines == null ? 0 : lines.size();
    }
}
