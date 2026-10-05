package com.example.sod401_team_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class EventsFragment extends Fragment {

    public EventsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_events,
                container,
                false
        );

        RecyclerView recyclerEvents =
                view.findViewById(R.id.recyclerEvents);

        recyclerEvents.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        List<String> eventTitles = Arrays.asList(
                "Student Innovation Day",
                "Career and Internship Fair",
                "Campus Sports Day",
                "Programming Workshop",
                "Student Leadership Seminar"
        );

        EventAdapter adapter = new EventAdapter(eventTitles);

        recyclerEvents.setAdapter(adapter);

        view.findViewById(R.id.btnHome).setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        return view;
    }
}