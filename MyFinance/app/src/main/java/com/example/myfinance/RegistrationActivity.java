package com.example.myfinance;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myfinance.R;
import com.example.myfinance.dao.UserDAO;
import com.example.myfinance.model.User;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnRegister;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userDAO = new UserDAO(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String u = etUsername.getText().toString().trim();
            String p = etPassword.getText().toString().trim();
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "সব ফিল্ড পূরণ করুন", Toast.LENGTH_SHORT).show();
                return;
            }
            User user = new User();
            user.setUsername(u);
            user.setPassword(p);
            if (userDAO.register(user)) {
                Toast.makeText(this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
