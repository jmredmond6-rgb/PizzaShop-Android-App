package com.example.pizzashop.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/* The item added to the customer’s shopping cart. */
@Entity(
        tableName = "cart_items",
        foreignKeys = {
                @ForeignKey(entity = Customer.class, parentColumns = "id", childColumns = "customerId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Pizza.class, parentColumns = "id", childColumns = "pizzaId", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("customerId"), @Index("pizzaId")}
)
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long customerId;
    public long pizzaId;
    public int quantity;

    public CartItem(long customerId, long pizzaId, int quantity) {

        this.customerId = customerId;
        this.pizzaId = pizzaId;
        this.quantity = quantity;

    }
}