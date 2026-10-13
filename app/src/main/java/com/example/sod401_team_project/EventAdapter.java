
package com.example.sod401_team_project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sod401_team_project.data.Event;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter
        extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Event> events = new ArrayList<>();

    public EventAdapter() {
        // The list starts empty.
    }

    // Update the list with events loaded from the database.
    public void updateEvents(List<Event> newEvents) {
        events.clear();

        if (newEvents != null) {
            events.addAll(newEvents);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull EventViewHolder holder,
            int position) {

        Event event = events.get(position);

        // Display event information using the getters in Event.java.
        holder.tvEventTitle.setText(event.getTitle());
        holder.tvEventDescription.setText(event.getDescription());
        holder.tvEventDate.setText(event.getDate());
        holder.tvEventTime.setText(event.getTime());
        holder.tvEventDetails.setText(event.getLocation());

        // Open the event details when the button is clicked.
        holder.btnViewEvent.setOnClickListener(v ->
                openEventDetails(v, event)
        );

        // Also allow the card itself to open the details.
        holder.itemView.setOnClickListener(v ->
                openEventDetails(v, event)
        );
    }

    private void openEventDetails(View view, Event event) {

        Intent intent = new Intent(
                view.getContext(),
                EventDetailsActivity.class
        );

        intent.putExtra("event_id", event.getId());
        intent.putExtra("event_title", event.getTitle());
        intent.putExtra("event_description", event.getDescription());
        intent.putExtra("event_date", event.getDate());
        intent.putExtra("event_time", event.getTime());
        intent.putExtra("event_location", event.getLocation());

        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventTitle;
        TextView tvEventDescription;
        TextView tvEventDate;
        TextView tvEventTime;
        TextView tvEventDetails;
        MaterialButton btnViewEvent;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEventTitle =
                    itemView.findViewById(R.id.tvEventTitle);

            tvEventDescription =
                    itemView.findViewById(R.id.tvEventDescription);

            tvEventDate =
                    itemView.findViewById(R.id.tvEventDate);

            tvEventTime =
                    itemView.findViewById(R.id.tvEventTime);

            tvEventDetails =
                    itemView.findViewById(R.id.tvEventDetails);

            btnViewEvent =
                    itemView.findViewById(R.id.btnViewEvent);
        }
    }
}
