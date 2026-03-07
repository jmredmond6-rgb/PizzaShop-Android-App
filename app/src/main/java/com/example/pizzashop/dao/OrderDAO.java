package com.example.pizzashop.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pizzashop.entities.Order;
import com.example.pizzashop.entities.OrderItem;

import java.util.List;
@Dao
public interface OrderDAO {

    @Insert
    long insertOrder(Order order);

    @Insert
    void insertOrderItems(List<OrderItem> items);


    @Query("SELECT * FROM orders WHERE customerId = :customerId ORDER BY createdAtEpochMs DESC")
    List<Order> getOrdersForCustomer(long customerId);


    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    List<OrderItem> getItemsForOrder(long orderId);


    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    Order getOrderById(long orderId);




}
