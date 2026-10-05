package com.example.sod401_team_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        TextView tvEventTitle = findViewById(R.id.tvEventTitle);
        TextView tvEventDescription = findViewById(R.id.tvEventDescription);
        TextView tvEventDate = findViewById(R.id.tvEventDate);
        TextView tvEventTime = findViewById(R.id.tvEventTime);
        TextView tvEventLocation = findViewById(R.id.tvEventLocation);

        Button btnRegisterEvent = findViewById(R.id.btnRegisterEvent);
        Button btnBackEvents = findViewById(R.id.btnBackEvents);

        String eventTitle = getIntent().getStringExtra("event_title");

        if (eventTitle != null) {
            tvEventTitle.setText(eventTitle);
        }

        btnRegisterEvent.setOnClickListener(v -> {
            btnRegisterEvent.setText("REGISTERED");
            btnRegisterEvent.setEnabled(false);
        });

        btnBackEvents.setOnClickListener(v -> {
            finish();
        });
    }
}