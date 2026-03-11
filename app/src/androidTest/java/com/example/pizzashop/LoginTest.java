package com.example.pizzashop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pizzashop.dao.CustomerDAO;
import com.example.pizzashop.database.PizzaShopRepository;
import com.example.pizzashop.database.ShopDatabase;
import com.example.pizzashop.entities.Customer;
import com.example.pizzashop.security.PasswordSecurity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    private ShopDatabase db;
    private CustomerDAO customerDAO;
    private PizzaShopRepository repo;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ShopDatabase.class)
                .allowMainThreadQueries()
                .build();
        customerDAO = db.customerDAO();
        repo = new PizzaShopRepository(db);
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void register_storesHashedPasswordAndNormalizedEmail() {
        long customerId = repo.register("Jake", "  Jake@Test.com  ", "JMR123");

        Customer found = customerDAO.findById(customerId);

        assertNotNull(found);
        assertEquals("jake@test.com", found.email);
        assertNotEquals("JMR123", found.password);
        assertTrue(PasswordSecurity.isHashed(found.password));
    }

    @Test
    public void register_rejectsDuplicateEmailIgnoringCase() {
        repo.register("Jake", "jake@test.com", "JMR123");

        try {
            repo.register("Jake 2", "JAKE@test.com", "another123");
        } catch (IllegalArgumentException ex) {
            assertEquals("Email is already registered.", ex.getMessage());
            return;
        }

        assertTrue("Expected duplicate email registration to fail", false);
    }

    @Test
    public void login_returnsCustomer_whenPasswordMatchesHashedCredential() {
        long customerId = repo.register("Jake", "jake@test.com", "JMR123");

        Customer found = repo.login("JAKE@test.com", "JMR123");

        assertNotNull(found);
        assertEquals(customerId, found.id);
    }

    @Test
    public void login_returnsNull_whenPasswordWrong() {
        repo.register("Jake", "jake@test.com", "JMR123");

        Customer found = repo.login("jake@test.com", "WRONG");

        assertNull(found);
    }

    @Test
    public void login_upgradesLegacyPlaintextPasswordAfterSuccessfulLogin() {
        Customer legacy = new Customer("Jake", "jake@test.com", "JMR123");
        long customerId = customerDAO.insert(legacy);

        Customer loggedIn = repo.login("jake@test.com", "JMR123");
        Customer updated = customerDAO.findById(customerId);

        assertNotNull(loggedIn);
        assertNotNull(updated);
        assertTrue(PasswordSecurity.isHashed(updated.password));
        assertFalse("JMR123".equals(updated.password));
    }
}