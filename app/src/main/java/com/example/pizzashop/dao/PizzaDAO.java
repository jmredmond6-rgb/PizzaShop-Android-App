package com.example.pizzashop.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.pizzashop.entities.Pizza;

import java.util.List;

@Dao
public interface PizzaDAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Pizza> pizzas);
    @Query("SELECT * FROM pizzas WHERE active = 1 ORDER BY name")
    List<Pizza> getActivePizzas();

    @Query("SELECT * FROM pizzas WHERE id = :pizzaId LIMIT 1")
    Pizza getById(long pizzaId);

    @Query("SELECT COUNT(*) FROM pizzas")
    int countAll();



    @Query("DELETE FROM pizzas")
    void deleteAllPizzas();




}
