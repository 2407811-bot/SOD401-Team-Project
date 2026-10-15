package com.example.sod401_team_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = getText(etEmail).toLowerCase(Locale.ROOT);
        String password = getText(etPassword);

        if (email.isEmpty()) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Signing in...");

        SupabaseClient.signIn(
                email,
                password,
                new SupabaseClient.Callback() {

                    @Override
                    public void onSuccess(JSONObject response) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }

                        String accessToken =
                                response.optString("access_token", "");

                        JSONObject user = response.optJSONObject("user");

                        if (accessToken.isEmpty() || user == null) {
                            resetLoginButton();
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login response was incomplete. Please try again.",
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }

                        String userId = user.optString("id", "");
                        String userEmail = user.optString("email", email);

                        if (userId.isEmpty()) {
                            resetLoginButton();
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Could not identify your account.",
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }

                        JSONObject metadata =
                                user.optJSONObject("user_metadata");

                        String userName = "";
                        if (metadata != null) {
                            userName = metadata.optString(
                                    "full_name", ""
                            );
                        }

                        // Keep the authenticated session for later API requests.
                        getSharedPreferences(
                                "CampusPulseSession",
                                MODE_PRIVATE
                        ).edit()
                                .putString("ACCESS_TOKEN", accessToken)
                                .putString("USER_ID", userId)
                                .putString("USER_EMAIL", userEmail)
                                .putString("USER_NAME", userName)
                                .apply();

                        Toast.makeText(
                                LoginActivity.this,
                                "Login successful!",
                                Toast.LENGTH_SHORT
                        ).show();

                        Intent intent = new Intent(
                                LoginActivity.this,
                                MainActivity.class
                        );

                        intent.putExtra("USER_ID", userId);
                        intent.putExtra("USER_NAME", userName);
                        intent.putExtra("USER_EMAIL", userEmail);

                        // This is a safe default until roles are loaded
                        // from the profiles table.
                        intent.putExtra("USER_ROLE", "STUDENT");

                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }

                        resetLoginButton();

                        android.util.Log.e(
                                "CampusPulseLogin",
                                "Supabase sign-in failed: " + message
                        );

                        Toast.makeText(
                                LoginActivity.this,
                                "Login failed: " + message,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void resetLoginButton() {
        btnLogin.setEnabled(true);
        btnLogin.setText("LOGIN");
    }

    private String getText(EditText input) {
        if (input.getText() == null) {
            return "";
        }
        return input.getText().toString().trim();
    }
}
