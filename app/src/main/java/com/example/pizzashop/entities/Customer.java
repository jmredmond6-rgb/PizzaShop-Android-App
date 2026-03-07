package com.example.pizzashop.entities;

import androidx.room.Entity;

import androidx.room.PrimaryKey;



import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(
        tableName = "customers",
        indices = {@Index(value = {"email"}, unique = true)}
)
public class Customer {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String email;


    public String password;

    public long createdAtEpochMs;

    public Customer(String name, String email, String password) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAtEpochMs = System.currentTimeMillis();

    }
}