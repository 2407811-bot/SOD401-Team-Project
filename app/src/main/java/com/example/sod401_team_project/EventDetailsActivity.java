
package com.example.sod401_team_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sod401_team_project.data.Event;
import com.example.sod401_team_project.repository.EventRepository;
import com.google.android.material.button.MaterialButton;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView tvEventTitle;
    private TextView tvEventDescription;
    private TextView tvEventAbout;
    private TextView tvEventDate;
    private TextView tvEventTime;
    private TextView tvEventLocation;

    private MaterialButton btnRegisterEvent;
    private MaterialButton btnEditEvent;
    private MaterialButton btnDeleteEvent;
    private MaterialButton btnBackEventsBottom;

    private ImageButton btnBackEvents;

    private EventRepository eventRepository;
    private int eventId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Connect to the views in your existing XML.
        tvEventTitle = findViewById(R.id.tvEventTitle);
        tvEventDescription = findViewById(R.id.tvEventDescription);
        tvEventAbout = findViewById(R.id.tvEventAbout);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventTime = findViewById(R.id.tvEventTime);
        tvEventLocation = findViewById(R.id.tvEventLocation);

        btnRegisterEvent = findViewById(R.id.btnRegisterEvent);
        btnEditEvent = findViewById(R.id.btnEditEvent);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);

        btnBackEvents = findViewById(R.id.btnBackEvents);
        btnBackEventsBottom =
                findViewById(R.id.btnBackEventsBottom);

        eventRepository =
                new EventRepository(getApplicationContext());

        // Get the ID of the selected event.
        eventId = getIntent().getIntExtra("event_id", -1);

        // Display the passed details immediately.
        displayIntentDetails();

        // Load the latest saved information from Room.
        if (eventId != -1) {
            loadEventDetails();
        } else {
            btnEditEvent.setEnabled(false);
            btnDeleteEvent.setEnabled(false);

            Toast.makeText(
                    this,
                    "Event ID is missing.",
                    Toast.LENGTH_LONG
            ).show();
        }

        // Preserve the existing registration behaviour.
        btnRegisterEvent.setOnClickListener(v -> {
            btnRegisterEvent.setText("REGISTERED");
            btnRegisterEvent.setEnabled(false);

            Toast.makeText(
                    this,
                    "You have registered for this event.",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Open the form in edit mode.
        btnEditEvent.setOnClickListener(v -> {
            if (eventId == -1) {
                return;
            }

            Intent intent = new Intent(
                    EventDetailsActivity.this,
                    AddEventActivity.class
            );

            intent.putExtra("event_id", eventId);
            startActivity(intent);
        });

        // Ask before deleting.
        btnDeleteEvent.setOnClickListener(
                v -> confirmDeleteEvent()
        );

        // Both Back buttons return to the previous screen.
        btnBackEvents.setOnClickListener(v -> finish());

        btnBackEventsBottom.setOnClickListener(v -> finish());
    }

    private void displayIntentDetails() {
        Intent intent = getIntent();

        setTextIfPresent(
                tvEventTitle,
                intent.getStringExtra("event_title")
        );

        String description =
                intent.getStringExtra("event_description");

        setTextIfPresent(tvEventDescription, description);
        setTextIfPresent(tvEventAbout, description);

        setTextIfPresent(
                tvEventDate,
                intent.getStringExtra("event_date")
        );

        setTextIfPresent(
                tvEventTime,
                intent.getStringExtra("event_time")
        );

        setTextIfPresent(
                tvEventLocation,
                intent.getStringExtra("event_location")
        );
    }

    private void setTextIfPresent(
            TextView view,
            String value
    ) {
        if (view != null
                && value != null
                && !value.trim().isEmpty()) {
            view.setText(value);
        }
    }

    private void loadEventDetails() {
        eventRepository.getEventById(
                eventId,
                new EventRepository.OnEventLoadedListener() {

                    @Override
                    public void onSuccess(Event event) {
                        runOnUiThread(() -> {
                            if (isFinishing() || isDestroyed()) {
                                return;
                            }

                            if (event == null) {
                                Toast.makeText(
                                        EventDetailsActivity.this,
                                        "Event not found.",
                                        Toast.LENGTH_LONG
                                ).show();
                                return;
                            }

                            tvEventTitle.setText(event.getTitle());

                            tvEventDescription.setText(
                                    event.getDescription()
                            );

                            tvEventAbout.setText(
                                    event.getDescription()
                            );

                            tvEventDate.setText(event.getDate());
                            tvEventTime.setText(event.getTime());

                            tvEventLocation.setText(
                                    event.getLocation()
                            );
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> Toast.makeText(
                                EventDetailsActivity.this,
                                "Could not load event: " + message,
                                Toast.LENGTH_LONG
                        ).show());
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the details after returning from editing.
        if (eventRepository != null && eventId != -1) {
            loadEventDetails();
        }
    }

    private void confirmDeleteEvent() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage(
                        "Are you sure you want to delete this event? "
                                + "This action cannot be undone."
                )
                .setNegativeButton(
                        "Cancel",
                        (dialog, which) -> dialog.dismiss()
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> deleteEvent()
                )
                .show();
    }

    private void deleteEvent() {
        btnDeleteEvent.setEnabled(false);
        btnDeleteEvent.setText("Deleting...");

        eventRepository.getEventById(
                eventId,
                new EventRepository.OnEventLoadedListener() {

                    @Override
                    public void onSuccess(Event event) {
                        if (event == null) {
                            runOnUiThread(() -> {
                                restoreDeleteButton();

                                Toast.makeText(
                                        EventDetailsActivity.this,
                                        "Event no longer exists.",
                                        Toast.LENGTH_LONG
                                ).show();
                            });
                            return;
                        }

                        eventRepository.deleteEvent(
                                event,
                                new EventRepository.OnEventDeletedListener() {

                                    @Override
                                    public void onSuccess(
                                            int rowsDeleted
                                    ) {
                                        runOnUiThread(() -> {
                                            if (rowsDeleted > 0) {
                                                Toast.makeText(
                                                        EventDetailsActivity.this,
                                                        "Event deleted successfully.",
                                                        Toast.LENGTH_SHORT
                                                ).show();

                                                setResult(RESULT_OK);
                                                finish();
                                            } else {
                                                restoreDeleteButton();

                                                Toast.makeText(
                                                        EventDetailsActivity.this,
                                                        "Event was not deleted.",
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(
                                            String message
                                    ) {
                                        runOnUiThread(() -> {
                                            restoreDeleteButton();

                                            Toast.makeText(
                                                    EventDetailsActivity.this,
                                                    "Could not delete event: "
                                                            + message,
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        });
                                    }
                                }
                        );
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> {
                            restoreDeleteButton();

                            Toast.makeText(
                                    EventDetailsActivity.this,
                                    "Could not find event: " + message,
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                }
        );
    }

    private void restoreDeleteButton() {
        btnDeleteEvent.setEnabled(true);
        btnDeleteEvent.setText("Delete Event");
    }
}
