package com.example.pizzashop.UI;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzashop.database.PizzaShopRepository;
import com.example.pizzashop.databinding.ActivityRegisterBinding;

/* Creates an account and then returns to the login menu. */
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding b;
    private PizzaShopRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setSupportActionBar(b.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        repo = new PizzaShopRepository(this);

        b.btnRegister.setOnClickListener(v -> attemptRegistration());
        b.btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void attemptRegistration() {
        String name = b.etName.getText() == null ? "" : b.etName.getText().toString().trim();
        String email = b.etEmail.getText() == null ? "" : b.etEmail.getText().toString().trim();
        String pass = b.etPassword.getText() == null ? "" : b.etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 4) {
            Toast.makeText(this, "Password should be at least 4 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            repo.register(name, email, pass);
            Toast.makeText(this, "Account created. Please log in.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IllegalArgumentException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
