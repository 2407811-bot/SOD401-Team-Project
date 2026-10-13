
package com.example.sod401_team_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sod401_team_project.data.Event;
import com.example.sod401_team_project.repository.EventRepository;
import com.google.android.material.button.MaterialButton;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    private EditText etEventTitle;
    private EditText etEventDescription;
    private EditText etEventDate;
    private EditText etEventTime;
    private EditText etEventLocation;

    private MaterialButton btnSaveEvent;
    private EventRepository eventRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Connect Java variables to the XML fields.
        etEventTitle = findViewById(R.id.etEventTitle);
        etEventDescription = findViewById(R.id.etEventDescription);
        etEventDate = findViewById(R.id.etEventDate);
        etEventTime = findViewById(R.id.etEventTime);
        etEventLocation = findViewById(R.id.etEventLocation);

        btnSaveEvent = findViewById(R.id.btnSaveEvent);

        MaterialButton btnCancelEvent =
                findViewById(R.id.btnCancelEvent);

        // Connect to the existing Room database repository.
        eventRepository = new EventRepository(
                getApplicationContext()
        );

        // Open the calendar when the date field is tapped.
        etEventDate.setOnClickListener(v -> showDatePicker());

        // Open the clock when the time field is tapped.
        etEventTime.setOnClickListener(v -> showTimePicker());

        // Save the event when the Save button is tapped.
        btnSaveEvent.setOnClickListener(v -> saveEvent());

        // Return to the previous screen without saving.
        btnCancelEvent.setOnClickListener(v -> finish());
    }

    // Display a calendar for selecting the event date.
    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, year, month, dayOfMonth) -> {

                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(
                            year, month, dayOfMonth, 0, 0, 0
                    );
                    selectedDate.set(Calendar.MILLISECOND, 0);

                    SimpleDateFormat dateFormat =
                            new SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.US
                            );

                    String formattedDate =
                            dateFormat.format(selectedDate.getTime());

                    etEventDate.setText(formattedDate);
                    etEventDate.setError(null);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Do not allow dates in the past.
        datePickerDialog.getDatePicker().setMinDate(
                System.currentTimeMillis() - 1000
        );

        datePickerDialog.setTitle("Select event date");
        datePickerDialog.show();
    }

    // Display a clock for selecting the event time.
    private void showTimePicker() {

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (timePicker, selectedHour, selectedMinute) -> {

                    // Convert the selected hour to 12-hour format.
                    String period = selectedHour >= 12 ? "PM" : "AM";

                    int hour12 = selectedHour % 12;

                    if (hour12 == 0) {
                        hour12 = 12;
                    }

                    String formattedTime = String.format(
                            Locale.US,
                            "%02d:%02d %s",
                            hour12,
                            selectedMinute,
                            period
                    );

                    etEventTime.setText(formattedTime);
                    etEventTime.setError(null);
                },
                hour,
                minute,
                false
        );

        timePickerDialog.setTitle("Select event time");
        timePickerDialog.show();
    }

    // Validate the form and save the event.
    private void saveEvent() {

        String title = etEventTitle.getText()
                .toString().trim();

        String description = etEventDescription.getText()
                .toString().trim();

        String date = etEventDate.getText()
                .toString().trim();

        String time = etEventTime.getText()
                .toString().trim();

        String location = etEventLocation.getText()
                .toString().trim();

        // Check the event title.
        if (TextUtils.isEmpty(title)) {
            etEventTitle.setError("Enter an event title");
            etEventTitle.requestFocus();
            return;
        }

        // Check the event description.
        if (TextUtils.isEmpty(description)) {
            etEventDescription.setError("Enter a description");
            etEventDescription.requestFocus();
            return;
        }

        // Check that the selected date is valid.
        if (!isValidDate(date)) {
            etEventDate.setError("Select a valid event date");
            etEventDate.requestFocus();
            return;
        }

        // Check that a time has been selected.
        if (TextUtils.isEmpty(time)) {
            etEventTime.setError("Select an event time");
            etEventTime.requestFocus();
            return;
        }

        // Check the event location.
        if (TextUtils.isEmpty(location)) {
            etEventLocation.setError("Enter the event location");
            etEventLocation.requestFocus();
            return;
        }

        // Prevent repeated taps while saving.
        btnSaveEvent.setEnabled(false);
        btnSaveEvent.setText("Saving...");

        /*
         * Temporary organizer ID.
         * We will connect this to the logged-in user's ID
         * when authentication is implemented.
         */
        int organizerId = 0;

        // Create the event object.
        Event event = new Event(
                title,
                description,
                date,
                time,
                location,
                organizerId
        );

        // Save the event through the repository.
        eventRepository.insertEvent(
                event,
                new EventRepository.OnEventInsertedListener() {

                    @Override
                    public void onSuccess(long eventId) {

                        runOnUiThread(() -> {

                            Toast.makeText(
                                    AddEventActivity.this,
                                    "Event saved successfully!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // Tell the previous screen that we saved.
                            setResult(RESULT_OK);

                            // Return to the previous screen.
                            finish();
                        });
                    }

                    @Override
                    public void onError(String message) {

                        runOnUiThread(() -> {

                            btnSaveEvent.setEnabled(true);
                            btnSaveEvent.setText("Save Event");

                            Toast.makeText(
                                    AddEventActivity.this,
                                    "Could not save event: " + message,
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                }
        );
    }

    // Check the date format and reject impossible dates.
    private boolean isValidDate(String date) {

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
        );

        format.setLenient(false);

        ParsePosition position = new ParsePosition(0);

        java.util.Date parsedDate = format.parse(date, position);

        return parsedDate != null
                && position.getIndex() == date.length();
    }
}
