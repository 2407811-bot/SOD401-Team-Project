package com.example.sod401_team_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextInputEditText etFullName =
                findViewById(R.id.etFullName);

        TextInputEditText etEmail =
                findViewById(R.id.etEmail);

        TextInputEditText etPassword =
                findViewById(R.id.etPassword);

        TextInputEditText etConfirmPassword =
                findViewById(R.id.etConfirmPassword);

        MaterialButton btnRegister =
                findViewById(R.id.btnRegister);

        TextView tvLogin =
                findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> {

            String fullName =
                    etFullName.getText().toString().trim();

            String email =
                    etEmail.getText().toString().trim();

            String password =
                    etPassword.getText().toString().trim();

            String confirmPassword =
                    etConfirmPassword.getText().toString().trim();

            if (fullName.isEmpty()) {
                etFullName.setError("Please enter your full name");
                etFullName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                etEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Please enter a password");
                etPassword.requestFocus();
                return;
            }

            if (confirmPassword.isEmpty()) {
                etConfirmPassword.setError(
                        "Please confirm your password"
                );
                etConfirmPassword.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError(
                        "Passwords do not match"
                );
                etConfirmPassword.requestFocus();
                return;
            }

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();
        });

        tvLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();
        });
    }
}