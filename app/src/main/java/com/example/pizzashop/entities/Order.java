package com.example.pizzashop.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/* The customer's order. */
@Entity(
        tableName = "orders",
        foreignKeys = {
                @ForeignKey(entity = Customer.class, parentColumns = "id", childColumns = "customerId", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("customerId")}
)
public class Order {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long customerId;


    public long createdAtEpochMs;

    public double totalDollars;

    public Order(long customerId, double totalDollars) {

        this.customerId = customerId;
        this.totalDollars = totalDollars;
        this.createdAtEpochMs = System.currentTimeMillis();

    }
}