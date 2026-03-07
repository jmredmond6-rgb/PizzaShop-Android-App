package com.example.pizzashop.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/* Used to represent an item for a completed order using the snapshot data fields. */
@Entity(
        tableName = "order_items",
        foreignKeys = {
                @ForeignKey(entity = Order.class, parentColumns = "id", childColumns = "orderId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Pizza.class, parentColumns = "id", childColumns = "pizzaId", onDelete = ForeignKey.NO_ACTION)
        },
        indices = {@Index("orderId"), @Index("pizzaId")}
)
public class OrderItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long orderId;
    public long pizzaId;

    public String pizzaNameSnapshot;
    public double unitPriceDollarsSnapshot;
    public int quantity;

    public OrderItem(long orderId,
                     long pizzaId,
                     String pizzaNameSnapshot,
                     double unitPriceDollarsSnapshot,
                     int quantity) {
        this.orderId = orderId;
        this.pizzaId = pizzaId;
        this.pizzaNameSnapshot = pizzaNameSnapshot;
        this.unitPriceDollarsSnapshot = unitPriceDollarsSnapshot;
        this.quantity = quantity;
    }
}