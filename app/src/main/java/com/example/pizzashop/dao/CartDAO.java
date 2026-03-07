package com.example.pizzashop.dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Dao;
import com.example.pizzashop.entities.CartItem;

import java.util.List;
@Dao
public interface CartDAO {
    
    @Query("SELECT * FROM cart_items WHERE customerId = :customerId")
    List<CartItem> getCartItems(long customerId);

    @Query("SELECT * FROM cart_items WHERE customerId = :customerId AND pizzaId = :pizzaId LIMIT 1")
    CartItem findItem(long customerId, long pizzaId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(CartItem item);

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :cartItemId")
    void updateQuantity(long cartItemId, int quantity);

    @Query("DELETE FROM cart_items WHERE id = :cartItemId")
    void deleteById(long cartItemId);

    @Query("DELETE FROM cart_items WHERE customerId = :customerId")
    void clearCart(long customerId);

    @Transaction
    default void addOrIncrement(long customerId, long pizzaId) {
        CartItem existing = findItem(customerId, pizzaId);
        if (existing == null) {
            insert(new CartItem(customerId, pizzaId, 1));
        } else {
            updateQuantity(existing.id, existing.quantity + 1);
        }
    }

}
