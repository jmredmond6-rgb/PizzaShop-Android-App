package com.example.pizzashop.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pizzashop.entities.Customer;

@Dao
public interface CustomerDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Customer customer);

    @Query("SELECT * FROM customers WHERE email = :email LIMIT 1")
    Customer findByEmail(String email);

    @Query("SELECT * FROM customers WHERE id = :id LIMIT 1")
    Customer findById(long id);
}