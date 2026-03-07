package com.example.pizzashop.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/* The pizzas on the menu. */
@Entity(tableName = "pizzas")
public class Pizza {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String description;


    public double priceDollars;

    public boolean active;

    public Pizza(String name, String description, double priceDollars, boolean active) {
        this.name = name;
        this.description = description;
        this.priceDollars = priceDollars;
        this.active = active;
    }
}


