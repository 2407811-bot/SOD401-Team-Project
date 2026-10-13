
package com.example.sod401_team_project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sod401_team_project.data.Event;
import com.example.sod401_team_project.repository.EventRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventsFragment extends Fragment {

    private EventRepository eventRepository;
    private EventAdapter eventAdapter;

    private final List<Event> allEvents = new ArrayList<>();

    private EditText etSearchEvents;
    private TextView tvEventCount;
    private TextView tvEmptyEvents;
    private MaterialCardView emptyEventsCard;

    private MaterialButton btnCategoryAll;
    private MaterialButton btnCategoryAcademic;
    private MaterialButton btnCategorySocial;
    private MaterialButton btnCategorySports;

    private String selectedCategory = "all";

    public EventsFragment() {
        // Required empty constructor.
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_events,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Connect the XML views.
        RecyclerView recyclerViewEvents =
                view.findViewById(R.id.recyclerViewEvents);

        etSearchEvents =
                view.findViewById(R.id.etSearchEvents);

        tvEventCount =
                view.findViewById(R.id.tvEventCount);

        tvEmptyEvents =
                view.findViewById(R.id.tvEmptyEvents);

        emptyEventsCard =
                view.findViewById(R.id.emptyEventsCard);

        btnCategoryAll =
                view.findViewById(R.id.btnCategoryAll);

        btnCategoryAcademic =
                view.findViewById(R.id.btnCategoryAcademic);

        btnCategorySocial =
                view.findViewById(R.id.btnCategorySocial);

        btnCategorySports =
                view.findViewById(R.id.btnCategorySports);

        MaterialButton btnHome =
                view.findViewById(R.id.btnHome);

        // Set up the RecyclerView.
        eventAdapter = new EventAdapter();

        recyclerViewEvents.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        recyclerViewEvents.setAdapter(eventAdapter);

        // Connect to the existing Room repository.
        eventRepository =
                new EventRepository(requireContext().getApplicationContext());

        // Load saved events.
        loadEvents();

        // Search as the user types.
        etSearchEvents.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {

                displayFilteredEvents();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Category filters.
        btnCategoryAll.setOnClickListener(v ->
                selectCategory("all")
        );

        btnCategoryAcademic.setOnClickListener(v ->
                selectCategory("academic")
        );

        btnCategorySocial.setOnClickListener(v ->
                selectCategory("social")
        );

        btnCategorySports.setOnClickListener(v ->
                selectCategory("sports")
        );

        // Return to the Home screen.
        btnHome.setOnClickListener(v -> {

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity())
                        .openFragment(new HomeFragment());
            } else {
                requireActivity().getSupportFragmentManager()
                        .popBackStack();
            }
        });

        updateCategoryButtons();
    }

    private void loadEvents() {

        eventRepository.getAllEvents(
                new EventRepository.OnEventsLoadedListener() {

                    @Override
                    public void onSuccess(List<Event> events) {

                        if (!isAdded()) {
                            return;
                        }

                        requireActivity().runOnUiThread(() -> {

                            if (!isAdded()) {
                                return;
                            }

                            allEvents.clear();

                            if (events != null) {
                                allEvents.addAll(events);
                            }

                            displayFilteredEvents();
                        });
                    }

                    @Override
                    public void onError(String message) {

                        if (!isAdded()) {
                            return;
                        }

                        requireActivity().runOnUiThread(() -> {

                            if (isAdded()) {
                                Toast.makeText(
                                        requireContext(),
                                        "Could not load events: " + message,
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
                    }
                }
        );
    }

    private void selectCategory(String category) {
        selectedCategory = category;
        updateCategoryButtons();
        displayFilteredEvents();
    }

    private void updateCategoryButtons() {

        setCategoryButtonStyle(btnCategoryAll,
                selectedCategory.equals("all"));

        setCategoryButtonStyle(btnCategoryAcademic,
                selectedCategory.equals("academic"));

        setCategoryButtonStyle(btnCategorySocial,
                selectedCategory.equals("social"));

        setCategoryButtonStyle(btnCategorySports,
                selectedCategory.equals("sports"));
    }

    private void setCategoryButtonStyle(
            MaterialButton button, boolean selected) {

        if (button == null) {
            return;
        }

        if (selected) {
            button.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            android.graphics.Color.parseColor("#4338CA")
                    )
            );

            button.setTextColor(
                    android.graphics.Color.WHITE
            );
        } else {
            button.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            android.graphics.Color.WHITE
                    )
            );

            button.setTextColor(
                    android.graphics.Color.parseColor("#475569")
            );
        }
    }

    private void displayFilteredEvents() {

        if (eventAdapter == null) {
            return;
        }

        String keyword = etSearchEvents.getText()
                .toString()
                .trim()
                .toLowerCase(Locale.ROOT);

        List<Event> filteredEvents = new ArrayList<>();

        for (Event event : allEvents) {

            String title = safeText(event.getTitle())
                    .toLowerCase(Locale.ROOT);

            String description = safeText(event.getDescription())
                    .toLowerCase(Locale.ROOT);

            String location = safeText(event.getLocation())
                    .toLowerCase(Locale.ROOT);

            boolean matchesSearch =
                    keyword.isEmpty()
                            || title.contains(keyword)
                            || description.contains(keyword)
                            || location.contains(keyword);

            String searchableText =
                    title + " " + description;

            boolean matchesCategory;

            switch (selectedCategory) {
                case "academic":
                    matchesCategory = containsAny(
                            searchableText,
                            "academic", "study", "lecture",
                            "workshop", "seminar", "research",
                            "education", "exam", "innovation"
                    );
                    break;

                case "social":
                    matchesCategory = containsAny(
                            searchableText,
                            "social", "party", "music",
                            "cultural", "festival", "welcome",
                            "networking", "celebration"
                    );
                    break;

                case "sports":
                    matchesCategory = containsAny(
                            searchableText,
                            "sport", "football", "basketball",
                            "netball", "volleyball", "tournament",
                            "fitness", "athletics"
                    );
                    break;

                default:
                    matchesCategory = true;
                    break;
            }

            if (matchesSearch && matchesCategory) {
                filteredEvents.add(event);
            }
        }

        eventAdapter.updateEvents(filteredEvents);

        tvEventCount.setText(
                filteredEvents.size()
                        + (filteredEvents.size() == 1
                        ? " EVENT" : " EVENTS")
        );

        boolean noEvents = filteredEvents.isEmpty();

        emptyEventsCard.setVisibility(
                noEvents ? View.VISIBLE : View.GONE
        );

        if (allEvents.isEmpty()) {
            tvEmptyEvents.setText(
                    "No events have been saved yet. Add an event to get started."
            );
        } else if (!keyword.isEmpty()
                || !selectedCategory.equals("all")) {
            tvEmptyEvents.setText(
                    "No events match your search or selected category."
            );
        } else {
            tvEmptyEvents.setText(
                    "There are no events to display yet. Check back soon!"
            );
        }
    }

    private boolean containsAny(String text, String... keywords) {

        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }
}
