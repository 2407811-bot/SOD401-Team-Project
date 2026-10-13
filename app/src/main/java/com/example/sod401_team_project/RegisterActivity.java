
package com.example.sod401_team_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sod401_team_project.data.User;
import com.example.sod401_team_project.repository.UserRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;

    private MaterialButton btnRegister;
    private TextView tvLogin;

    private UserRepository userRepository;

    private final ExecutorService passwordExecutor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Connect the Java code to the existing XML views.
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Connect registration to the local Room database.
        userRepository = new UserRepository(getApplicationContext());

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> openLoginScreen());
    }

    private void registerUser() {

        String fullName = getText(etFullName);
        String email = getText(etEmail).toLowerCase(
                java.util.Locale.ROOT
        );
        String password = getText(etPassword);
        String confirmPassword = getText(etConfirmPassword);

        // Validate the full name.
        if (fullName.isEmpty()) {
            etFullName.setError("Please enter your full name");
            etFullName.requestFocus();
            return;
        }

        // Validate the email.
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

        // Validate the password.
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
            etConfirmPassword.setError(
                    "Passwords do not match"
            );
            etConfirmPassword.requestFocus();
            return;
        }

        // Prevent repeated registration taps while checking.
        btnRegister.setEnabled(false);
        btnRegister.setText("Creating account...");

        // Check whether this email is already registered.
        userRepository.getUserByEmail(
                email,
                new UserRepository.OnUserLoadedListener() {

                    @Override
                    public void onSuccess(User existingUser) {

                        if (existingUser != null) {
                            runOnUiThread(() -> {
                                btnRegister.setEnabled(true);
                                btnRegister.setText("Register");

                                etEmail.setError(
                                        "This email is already registered"
                                );
                                etEmail.requestFocus();
                            });

                            return;
                        }

                        // Hash the password off the main thread.
                        passwordExecutor.execute(() -> {
                            try {
                                String passwordHash =
                                        hashPassword(password);

                                User newUser = new User(
                                        fullName,
                                        email,
                                        passwordHash,
                                        "STUDENT"
                                );

                                userRepository.insertUser(
                                        newUser,
                                        new UserRepository
                                                .OnUserInsertedListener() {

                                            @Override
                                            public void onSuccess(
                                                    long userId) {

                                                runOnUiThread(() -> {
                                                    btnRegister
                                                            .setEnabled(true);

                                                    btnRegister.setText(
                                                            "Register"
                                                    );

                                                    Toast.makeText(
                                                            RegisterActivity.this,
                                                            "Account created successfully. Please log in.",
                                                            Toast.LENGTH_LONG
                                                    ).show();

                                                    openLoginScreen();
                                                });
                                            }

                                            @Override
                                            public void onError(
                                                    String message) {

                                                runOnUiThread(() -> {
                                                    btnRegister
                                                            .setEnabled(true);

                                                    btnRegister.setText(
                                                            "Register"
                                                    );

                                                    Toast.makeText(
                                                            RegisterActivity.this,
                                                            "Could not create account. Please try again.",
                                                            Toast.LENGTH_LONG
                                                    ).show();
                                                });
                                            }
                                        }
                                );

                            } catch (Exception e) {
                                runOnUiThread(() -> {
                                    btnRegister.setEnabled(true);
                                    btnRegister.setText("Register");

                                    Toast.makeText(
                                            RegisterActivity.this,
                                            "Could not secure the password. Please try again.",
                                            Toast.LENGTH_LONG
                                    ).show();
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> {
                            btnRegister.setEnabled(true);
                            btnRegister.setText("Register");

                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Could not check the database. Please try again.",
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                }
        );
    }

    // Read text safely from an input field.
    private String getText(TextInputEditText input) {
        if (input.getText() == null) {
            return "";
        }

        return input.getText().toString().trim();
    }

    // Hash the password with PBKDF2 and a random salt.
    private String hashPassword(String password) throws Exception {

        byte[] salt = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        int iterations = 120000;

        KeySpec keySpec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                256
        );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256"
                );

        byte[] hash = factory.generateSecret(keySpec).getEncoded();

        ((PBEKeySpec) keySpec).clearPassword();

        String encodedSalt = Base64.encodeToString(
                salt,
                Base64.NO_WRAP
        );

        String encodedHash = Base64.encodeToString(
                hash,
                Base64.NO_WRAP
        );

        return "pbkdf2$" + iterations
                + "$" + encodedSalt
                + "$" + encodedHash;
    }

    private void openLoginScreen() {
        Intent intent = new Intent(
                RegisterActivity.this,
                LoginActivity.class
        );

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop any queued password work when this screen is destroyed.
        passwordExecutor.shutdown();
    }
}
