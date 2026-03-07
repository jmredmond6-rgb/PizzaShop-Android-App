package com.example.pizzashop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzashop.database.PizzaShopRepository;
import com.example.pizzashop.databinding.ActivityLoginBinding;
import com.example.pizzashop.entities.Customer;

/* Used for login and user authentication. */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding b;
    private PizzaShopRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setSupportActionBar(b.toolbar);

        repo = new PizzaShopRepository(this);

        b.btnLogin.setOnClickListener(v -> attemptLogin());
        b.btnGoRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String email = b.etEmail.getText() == null ? "" : b.etEmail.getText().toString().trim();
        String pass = b.etPassword.getText() == null ? "" : b.etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        Customer c = repo.login(email, pass);
        if (c == null) {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, PizzaMenuActivity.class);
        i.putExtra(PizzaMenuActivity.EXTRA_CUSTOMER_ID, c.id);
        startActivity(i);
        finish();
    }
}
