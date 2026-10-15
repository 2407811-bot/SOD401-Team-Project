package com.example.sod401_team_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "CampusPulseRegister";

    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;

    private MaterialButton btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> registerUser());
        tvLogin.setOnClickListener(v -> openLoginScreen());
    }

    private void registerUser() {

        String fullName = getText(etFullName);
        String email = getText(etEmail).toLowerCase(Locale.ROOT);
        String password = getText(etPassword);
        String confirmPassword = getText(etConfirmPassword);

        // Validate full name
        if (fullName.isEmpty()) {
            etFullName.setError("Please enter your full name");
            etFullName.requestFocus();
            return;
        }

        // Validate email
        if (email.isEmpty()) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            etPassword.setError("Please enter a password");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 8) {
            etPassword.setError(
                    "Password must contain at least 8 characters"
            );
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
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Prevent repeated submissions
        btnRegister.setEnabled(false);
        btnRegister.setText("Creating account...");

        // Register directly through Supabase Auth
        SupabaseClient.signUp(
                email,
                password,
                fullName,
                new SupabaseClient.Callback() {

                    @Override
                    public void onSuccess(JSONObject response) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }

                        btnRegister.setEnabled(true);
                        btnRegister.setText("Register");

                        /*
                         * Supabase may require email confirmation.
                         * An account can be created without returning
                         * an active access token.
                         */
                        JSONObject user = response.optJSONObject("user");

                        boolean accountCreated =
                                user != null || response.has("id");

                        if (!accountCreated) {
                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Registration response was incomplete. Please try again.",
                                    Toast.LENGTH_LONG
                            ).show();

                            Log.w(TAG, "Unexpected signup response");
                            return;
                        }

                        boolean hasSession =
                                response.has("access_token");

                        if (hasSession) {
                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Account created. You can now log in.",
                                    Toast.LENGTH_LONG
                            ).show();
                        } else {
                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Account created. Check your email to confirm your account before logging in.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        openLoginScreen();
                    }

                    @Override
                    public void onError(String message) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }

                        btnRegister.setEnabled(true);
                        btnRegister.setText("Register");

                        Log.e(TAG, "Supabase signup failed: " + message);

                        Toast.makeText(
                                RegisterActivity.this,
                                "Registration failed: " + message,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private String getText(TextInputEditText input) {
        if (input.getText() == null) {
            return "";
        }

        return input.getText().toString().trim();
    }

    private void openLoginScreen() {
        Intent intent = new Intent(
                RegisterActivity.this,
                LoginActivity.class
        );

        startActivity(intent);
        finish();
    }
}
