package com.example.pizzashop.UI;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzashop.databinding.RowPizzaBinding;
import com.example.pizzashop.entities.Pizza;

import java.util.List;

import Util.MoneyUtil;

/* Used to bind pizza data to menu list items for the RecyclerView. */
public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.VH> {

    public interface OnPizzaClick {
        void onAddToCart(Pizza pizza);
    }

    private final List<Pizza> pizzas;
    private final OnPizzaClick listener;

    public PizzaAdapter(List<Pizza> pizzas, OnPizzaClick listener) {
        this.pizzas = pizzas;
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        RowPizzaBinding b;
        VH(RowPizzaBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowPizzaBinding b = RowPizzaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Pizza p = pizzas.get(position);
        holder.b.tvName.setText(p.name);
        holder.b.tvDesc.setText(p.description);
        holder.b.tvPrice.setText(MoneyUtil.dollars(p.priceDollars));
        holder.b.btnAdd.setOnClickListener(v -> {
            if (listener != null) listener.onAddToCart(p);
        });
    }

    @Override
    public int getItemCount() {
        return pizzas == null ? 0 : pizzas.size();
    }
}
