
package com.example.sod401_team_project;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SupabaseClient {

    private static final String SUPABASE_URL =
            BuildConfig.SUPABASE_URL;

    private static final String SUPABASE_KEY =
            BuildConfig.SUPABASE_PUBLISHABLE_KEY;

    private static final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    private static final Handler mainHandler =
            new Handler(Looper.getMainLooper());

    private SupabaseClient() {
        // Prevent creating instances of this class.
    }

    public interface Callback {
        void onSuccess(JSONObject response);

        void onError(String message);
    }

    // Register a new account using Supabase Authentication.
    public static void signUp(
            String email,
            String password,
            String fullName,
            Callback callback
    ) {
        executor.execute(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("email", email);
                body.put("password", password);

                JSONObject metadata = new JSONObject();
                metadata.put("full_name", fullName);
                body.put("data", metadata);

                JSONObject response = request(
                        "/auth/v1/signup",
                        "POST",
                        body
                );

                deliverSuccess(callback, response);

            } catch (Exception e) {
                deliverError(callback, e.getMessage());
            }
        });
    }

    // Sign in an existing user using Supabase Authentication.
    public static void signIn(
            String email,
            String password,
            Callback callback
    ) {
        executor.execute(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("email", email);
                body.put("password", password);

                JSONObject response = request(
                        "/auth/v1/token?grant_type=password",
                        "POST",
                        body
                );

                deliverSuccess(callback, response);

            } catch (Exception e) {
                deliverError(callback, e.getMessage());
            }
        });
    }

    // Send an HTTP request to Supabase.
    private static JSONObject request(
            String endpoint,
            String method,
            JSONObject body
    ) throws Exception {

        if (SUPABASE_URL == null
                || SUPABASE_URL.trim().isEmpty()
                || SUPABASE_KEY == null
                || SUPABASE_KEY.trim().isEmpty()
                || SUPABASE_URL.contains("YOUR_PROJECT_REF")
                || SUPABASE_KEY.contains("YOUR_KEY")) {

            throw new Exception(
                    "Supabase URL or publishable key is missing. "
                            + "Check local.properties."
            );
        }

        String baseUrl = SUPABASE_URL.trim()
                .replaceAll("/+$", "");

        URL url = new URL(baseUrl + endpoint);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod(method);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            connection.setRequestProperty(
                    "apikey",
                    SUPABASE_KEY
            );

            connection.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            connection.setRequestProperty(
                    "Accept",
                    "application/json"
            );

            connection.setDoOutput(true);

            byte[] requestBody =
                    body.toString().getBytes(StandardCharsets.UTF_8);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(requestBody);
            }

            int status = connection.getResponseCode();

            InputStream stream =
                    status >= 200 && status < 300
                            ? connection.getInputStream()
                            : connection.getErrorStream();

            StringBuilder result = new StringBuilder();

            if (stream != null) {
                try (BufferedReader reader =
                             new BufferedReader(
                                     new InputStreamReader(
                                             stream,
                                             StandardCharsets.UTF_8
                                     )
                             )) {

                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                }
            }

            String responseText = result.toString();

            if (status < 200 || status >= 300) {
                throw new Exception(
                        "Supabase error (" + status + "): "
                                + extractErrorMessage(responseText)
                );
            }

            if (responseText.trim().isEmpty()) {
                return new JSONObject();
            }

            return new JSONObject(responseText);

        } finally {
            connection.disconnect();
        }
    }

    // Extract a readable error message from Supabase.
    private static String extractErrorMessage(String responseText) {
        try {
            JSONObject error = new JSONObject(responseText);

            return error.optString(
                    "msg",
                    error.optString(
                            "message",
                            error.optString(
                                    "error_description",
                                    error.optString(
                                            "error",
                                            responseText
                                    )
                            )
                    )
            );

        } catch (Exception ignored) {
            return responseText.isEmpty()
                    ? "Unknown server error."
                    : responseText;
        }
    }

    // Return successful responses to the Android UI thread.
    private static void deliverSuccess(
            Callback callback,
            JSONObject response
    ) {
        mainHandler.post(() -> callback.onSuccess(response));
    }

    // Return errors to the Android UI thread.
    private static void deliverError(
            Callback callback,
            String message
    ) {
        mainHandler.post(() ->
                callback.onError(
                        message == null
                                ? "Unknown connection error."
                                : message
                )
        );
    }
}
