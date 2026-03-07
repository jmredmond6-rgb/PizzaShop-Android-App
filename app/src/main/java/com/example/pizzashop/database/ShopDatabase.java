package com.example.pizzashop.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.pizzashop.dao.CartDAO;
import com.example.pizzashop.dao.CustomerDAO;
import com.example.pizzashop.dao.OrderDAO;
import com.example.pizzashop.dao.PizzaDAO;
import com.example.pizzashop.entities.CartItem;
import com.example.pizzashop.entities.Customer;
import com.example.pizzashop.entities.Order;
import com.example.pizzashop.entities.OrderItem;
import com.example.pizzashop.entities.Pizza;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/* Room database configuration for the DAO files. */
@Database(
        entities = {Customer.class, Pizza.class, CartItem.class, Order.class, OrderItem.class},
        version = 2,
        exportSchema = false
)
public abstract class ShopDatabase extends RoomDatabase {

    private static volatile ShopDatabase INSTANCE;

    public abstract CustomerDAO customerDAO();

    public abstract PizzaDAO pizzaDAO();

    public abstract CartDAO cartDAO();

    public abstract OrderDAO orderDAO();

    public static ShopDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (ShopDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    ShopDatabase.class,
                                    "pizza_shop.db"
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(SupportSQLiteDatabase db) {
                                    Executors.newSingleThreadExecutor().execute(() ->
                                            seedPizzas(getInstance(context)));
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static void seedPizzas(ShopDatabase db) {

        PizzaDAO dao = db.pizzaDAO();
        if (dao.countAll() > 0) return;

        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(new Pizza("BBQ Chicken", "BBQ sauce, chicken, onion", 13.99, true));
        pizzas.add(new Pizza("Margherita", "Tomato, mozzarella, basil ", 10.99, true));
        pizzas.add(new Pizza("Pepperoni", "Pepperoni and cheese", 12.99, true));
        pizzas.add(new Pizza("Veggie", "Peppers, onion, olives, mushrooms", 12.49, true));

        dao.insertAll(pizzas);


    }



}