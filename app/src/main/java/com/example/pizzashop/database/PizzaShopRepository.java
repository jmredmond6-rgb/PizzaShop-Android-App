package com.example.pizzashop.database;

import android.content.Context;

import com.example.pizzashop.dao.CartDAO;
import com.example.pizzashop.dao.CustomerDAO;
import com.example.pizzashop.dao.OrderDAO;
import com.example.pizzashop.dao.PizzaDAO;
import com.example.pizzashop.entities.CartItem;
import com.example.pizzashop.entities.Customer;
import com.example.pizzashop.entities.Order;
import com.example.pizzashop.entities.OrderItem;
import com.example.pizzashop.entities.Pizza;
import com.example.pizzashop.security.PasswordSecurity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* Used to access the customer, pizza, cart, and order data for the application. */
public class PizzaShopRepository {

    private final CustomerDAO customerDao;
    private final PizzaDAO pizzaDao;
    private final CartDAO cartDao;
    private final OrderDAO orderDao;

    public PizzaShopRepository(Context context) {
        this(ShopDatabase.getInstance(context));
    }

    public PizzaShopRepository(ShopDatabase db) {
        customerDao = db.customerDAO();
        pizzaDao = db.pizzaDAO();
        cartDao = db.cartDAO();
        orderDao = db.orderDAO();
    }

    public long register(String name, String email, String password) throws IllegalArgumentException {
        String normalizedEmail = normalizeEmail(email);
        String trimmedName = name == null ? "" : name.trim();

        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (normalizedEmail.isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
        if (customerDao.findByEmail(normalizedEmail) != null) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        Customer customer = new Customer(trimmedName, normalizedEmail, PasswordSecurity.hashPassword(password));
        return customerDao.insert(customer);
    }

    public Customer login(String email, String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }

        Customer customer = customerDao.findByEmail(normalizeEmail(email));
        if (customer == null || customer.password == null) {
            return null;
        }

        if (!PasswordSecurity.verifyPassword(password, customer.password)) {
            return null;
        }

        if (!PasswordSecurity.isHashed(customer.password)) {
            String upgradedHash = PasswordSecurity.hashPassword(password);
            customerDao.updatePassword(customer.id, upgradedHash);
            customer.password = upgradedHash;
        }

        return customer;
    }

    public Customer getCustomer(long id) {
        return customerDao.findById(id);
    }

    public List<Pizza> getActivePizzas() {
        return pizzaDao.getActivePizzas();
    }

    public Pizza getPizza(long pizzaId) {
        return pizzaDao.getById(pizzaId);
    }

    public void addToCart(long customerId, long pizzaId) {
        cartDao.addOrIncrement(customerId, pizzaId);
    }

    public List<CartItem> getCart(long customerId) {
        return cartDao.getCartItems(customerId);
    }

    public void updateCartQuantity(long cartItemId, int newQty) {
        if (newQty <= 0) {
            cartDao.deleteById(cartItemId);
        } else {
            cartDao.updateQuantity(cartItemId, newQty);
        }
    }

    public void clearCart(long customerId) {
        cartDao.clearCart(customerId);
    }

    /* Creates an Order + OrderItems from the current cart. Then returns the new orderId, or -1 if cart is empty. */
    public long checkout(long customerId) {
        List<CartItem> cart = cartDao.getCartItems(customerId);
        if (cart == null || cart.isEmpty()) return -1;

        double total = 0.0;
        List<OrderItem> items = new ArrayList<>();

        for (CartItem ci : cart) {
            Pizza p = pizzaDao.getById(ci.pizzaId);
            if (p == null) continue;

            total += (p.priceDollars * ci.quantity);

            items.add(new OrderItem(
                    0,
                    p.id,
                    p.name,
                    p.priceDollars,
                    ci.quantity
            ));
        }

        total = Math.round(total * 100.0) / 100.0;

        Order order = new Order(customerId, total);
        long orderId = orderDao.insertOrder(order);

        for (OrderItem oi : items) {
            oi.orderId = orderId;
        }
        orderDao.insertOrderItems(items);

        cartDao.clearCart(customerId);
        return orderId;
    }

    public List<Order> getOrders(long customerId) {
        return orderDao.getOrdersForCustomer(customerId);
    }

    public Order getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    public List<OrderItem> getOrderItems(long orderId) {
        return orderDao.getItemsForOrder(orderId);
    }

    /* Adds items from a previous order back into the cart. */
    public int reorderFromOrder(long customerId, long orderId) {
        List<OrderItem> items = orderDao.getItemsForOrder(orderId);
        if (items == null || items.isEmpty()) return 0;

        int addedLines = 0;

        for (OrderItem oi : items) {
            Pizza p = pizzaDao.getById(oi.pizzaId);

            // Skip items that no longer exist or were disabled.
            if (p == null || !p.active) continue;

            for (int i = 0; i < oi.quantity; i++) {
                cartDao.addOrIncrement(customerId, oi.pizzaId);
            }
            addedLines++;
        }

        return addedLines;
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.US);
    }
}