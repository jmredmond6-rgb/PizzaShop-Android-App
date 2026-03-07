package com.example.pizzashop;


import static org.junit.Assert.*;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pizzashop.dao.CustomerDAO;
import com.example.pizzashop.database.ShopDatabase;
import com.example.pizzashop.entities.Customer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    private ShopDatabase db;
    private CustomerDAO customerDAO;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ShopDatabase.class)
                .allowMainThreadQueries()
                .build();
        customerDAO = db.customerDAO();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void findByEmail_returnsCustomer_whenExists() {
        Customer c = new Customer("Jake", "jake@test.com", "JMR123");
        customerDAO.insert(c);

        Customer found = customerDAO.findByEmail("jake@test.com");

        assertNotNull(found);
        assertEquals("jake@test.com", found.email);
    }

    @Test
    public void findByEmail_returnsNull_whenMissing() {
        assertNull(customerDAO.findByEmail("missing@test.com"));
    }


    @Test
    public void loginRule_returnsTrue_whenPasswordMatches() {
        customerDAO.insert(new Customer("Jake", "jake@test.com", "JMR123"));

        Customer found = customerDAO.findByEmail("jake@test.com");
        assertNotNull(found);

        boolean loginOk = "JMR123".equals(found.password);
        assertTrue(loginOk);
    }

    @Test
    public void loginRule_returnsFalse_whenPasswordWrong() {
        customerDAO.insert(new Customer("Jake", "jake@test.com", "JMR123"));

        Customer found = customerDAO.findByEmail("jake@test.com");
        assertNotNull(found);

        boolean loginOk = "WRONG".equals(found.password);
        assertFalse(loginOk);
    }

    @Test
    public void loginRule_returnsFalse_whenEmailMissing() {
        Customer found = customerDAO.findByEmail("missing@test.com");
        assertNull(found);


        boolean loginOk = (found != null) && "anything".equals(found.password);
        assertFalse(loginOk);
    }
}