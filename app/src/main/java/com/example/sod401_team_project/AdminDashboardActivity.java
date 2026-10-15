package com.example.sod401_team_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AdminDashboardActivity extends AppCompatActivity {

    private MaterialButton btnManageEvents;
    private MaterialButton btnManageAnnouncements;
    private MaterialButton btnViewRegistrations;
    private MaterialButton btnAdminLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnManageEvents = findViewById(R.id.btnManageEvents);
        btnManageAnnouncements =
                findViewById(R.id.btnManageAnnouncements);
        btnViewRegistrations =
                findViewById(R.id.btnViewRegistrations);
        btnAdminLogout = findViewById(R.id.btnAdminLogout);

        btnManageEvents.setOnClickListener(view ->
                Toast.makeText(
                        this,
                        "Connect the event management screen next.",
                        Toast.LENGTH_SHORT
                ).show()
        );

        btnManageAnnouncements.setOnClickListener(view ->
                Toast.makeText(
                        this,
                        "Connect the announcement management screen next.",
                        Toast.LENGTH_SHORT
                ).show()
        );

        btnViewRegistrations.setOnClickListener(view ->
                Toast.makeText(
                        this,
                        "Connect the registrations screen next.",
                        Toast.LENGTH_SHORT
                ).show()
        );

        btnAdminLogout.setOnClickListener(view -> {
            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });
    }
}
