package com.example.sod401_team_project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<String> eventTitles;

    public EventAdapter(List<String> eventTitles) {
        this.eventTitles = eventTitles;
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

        String title = eventTitles.get(position);

        holder.tvEventTitle.setText(title);

        holder.tvEventDescription.setText(
                "Campus event for students and the university community."
        );

        holder.tvEventDetails.setText(
                "Main Campus • 10:00 AM"
        );

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    EventDetailsActivity.class
            );

            intent.putExtra("event_title", title);

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventTitles.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventTitle;
        TextView tvEventDescription;
        TextView tvEventDetails;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEventTitle =
                    itemView.findViewById(R.id.tvEventTitle);

            tvEventDescription =
                    itemView.findViewById(R.id.tvEventDescription);

            tvEventDetails =
                    itemView.findViewById(R.id.tvEventDetails);
        }
    }
}