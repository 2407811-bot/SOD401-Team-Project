
package com.example.sod401_team_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sod401_team_project.data.User;
import com.example.sod401_team_project.repository.UserRepository;

import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private Button btnLogin;
    private TextView tvRegister;

    private UserRepository userRepository;

    private final ExecutorService passwordExecutor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Connect to the views in activity_login.xml.
        // The XML uses etUsername for the email field.
        etEmail = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvSignUp);

        // Connect to the local database through the repository.
        userRepository = new UserRepository(getApplicationContext());

        btnLogin.setOnClickListener(v -> loginUser());

        // Open registration when the user taps Sign Up.
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );
            startActivity(intent);
        });
    }

    private void loginUser() {

        String email = getText(etEmail)
                .toLowerCase(Locale.ROOT);

        String password = getText(etPassword);

        // Check that an email was entered.
        if (email.isEmpty()) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return;
        }

        // Check the email format.
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        // Check that a password was entered.
        if (password.isEmpty()) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }

        // Prevent repeated taps while login is processing.
        btnLogin.setEnabled(false);
        btnLogin.setText("Signing in...");

        // Find the registered account using its email.
        userRepository.getUserByEmail(
                email,
                new UserRepository.OnUserLoadedListener() {

                    @Override
                    public void onSuccess(User user) {

                        if (user == null) {
                            runOnUiThread(() -> showLoginError());
                            return;
                        }

                        // Verify the password away from the UI thread.
                        passwordExecutor.execute(() -> {

                            boolean passwordMatches;

                            try {
                                passwordMatches = verifyPassword(
                                        password,
                                        user.getPasswordHash()
                                );
                            } catch (Exception e) {
                                passwordMatches = false;
                            }

                            final boolean valid = passwordMatches;

                            runOnUiThread(() -> {

                                if (isFinishing() || isDestroyed()) {
                                    return;
                                }

                                if (!valid) {
                                    showLoginError();
                                    return;
                                }

                                btnLogin.setEnabled(true);
                                btnLogin.setText("LOGIN");

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Login successful!",
                                        Toast.LENGTH_SHORT
                                ).show();

                                // Open the main screen.
                                Intent intent = new Intent(
                                        LoginActivity.this,
                                        MainActivity.class
                                );

                                // Pass the logged-in user's information.
                                intent.putExtra(
                                        "USER_ID",
                                        user.getId()
                                );

                                intent.putExtra(
                                        "USER_NAME",
                                        user.getFullName()
                                );

                                intent.putExtra(
                                        "USER_EMAIL",
                                        user.getEmail()
                                );

                                intent.putExtra(
                                        "USER_ROLE",
                                        user.getRole()
                                );

                                startActivity(intent);
                                finish();
                            });
                        });
                    }

                    @Override
                    public void onError(String message) {

                        runOnUiThread(() -> {

                            if (isFinishing() || isDestroyed()) {
                                return;
                            }

                            btnLogin.setEnabled(true);
                            btnLogin.setText("LOGIN");

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Could not access the local database. Please try again.",
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                }
        );
    }

    // Verify the entered password against the stored PBKDF2 hash.
    private boolean verifyPassword(
            String password,
            String storedPassword
    ) throws Exception {

        if (storedPassword == null) {
            return false;
        }

        String[] parts = storedPassword.split("\\$");

        if (parts.length != 4 || !parts[0].equals("pbkdf2")) {
            return false;
        }

        int iterations = Integer.parseInt(parts[1]);

        // Reject unexpected iteration values.
        if (iterations < 10000 || iterations > 1000000) {
            return false;
        }

        byte[] salt = Base64.decode(
                parts[2],
                Base64.NO_WRAP
        );

        byte[] expectedHash = Base64.decode(
                parts[3],
                Base64.NO_WRAP
        );

        KeySpec keySpec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                expectedHash.length * 8
        );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256"
                );

        byte[] actualHash;

        try {
            actualHash = factory
                    .generateSecret(keySpec)
                    .getEncoded();
        } finally {
            ((PBEKeySpec) keySpec).clearPassword();
        }

        // Compare the hashes securely.
        return MessageDigest.isEqual(
                expectedHash,
                actualHash
        );
    }

    // Safely read text from an EditText.
    private String getText(EditText input) {

        if (input.getText() == null) {
            return "";
        }

        return input.getText().toString().trim();
    }

    // Show an error when the account or password is incorrect.
    private void showLoginError() {

        if (isFinishing() || isDestroyed()) {
            return;
        }

        btnLogin.setEnabled(true);
        btnLogin.setText("LOGIN");

        Toast.makeText(
                LoginActivity.this,
                "Incorrect email or password.",
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        passwordExecutor.shutdown();
    }
}
