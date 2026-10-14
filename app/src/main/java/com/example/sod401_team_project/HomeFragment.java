
package com.example.sod401_team_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty constructor.
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_home,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // EVENTS CARD
        MaterialCardView btnEvents =
                view.findViewById(R.id.btnEvents);

        if (btnEvents != null) {
            btnEvents.setOnClickListener(v ->
                    openScreen(new EventsFragment()));
        }

        // ANNOUNCEMENTS CARD
        MaterialCardView btnAnnouncements =
                view.findViewById(R.id.btnAnnouncements);

        if (btnAnnouncements != null) {
            btnAnnouncements.setOnClickListener(v ->
                    openScreen(new AnnouncementsFragment()));
        }

        // PROFILE CARD
        MaterialCardView btnProfile =
                view.findViewById(R.id.btnProfile);

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v ->
                    openScreen(new ProfileFragment()));
        }

        // EXPLORE CAMPUS CARD
        MaterialCardView btnExploreCampus =
                view.findViewById(R.id.btnExploreCampus);

        if (btnExploreCampus != null) {
            btnExploreCampus.setOnClickListener(v ->
                    Toast.makeText(
                            requireContext(),
                            "Explore Campus is coming soon!",
                            Toast.LENGTH_SHORT
                    ).show());
        }

        // LOGOUT BUTTON
        ImageButton btnLogout =
                view.findViewById(R.id.btnHomeLogout);

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v ->
                    Toast.makeText(
                            requireContext(),
                            "Logout will be implemented next.",
                            Toast.LENGTH_SHORT
                    ).show());
        }

        // SEARCH FIELD
        EditText etSearch =
                view.findViewById(R.id.etSearch);

        if (etSearch != null) {
            etSearch.setOnEditorActionListener(
                    (textView, actionId, event) -> {

                        String keyword =
                                etSearch.getText()
                                        .toString()
                                        .trim();

                        if (!keyword.isEmpty()) {
                            Toast.makeText(
                                    requireContext(),
                                    "Search will be connected to events next.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        return false;
                    });
        }
    }

    // OPENS ANOTHER SCREEN INSIDE MAIN ACTIVITY
    private void openScreen(Fragment fragment) {

        if (getActivity() instanceof MainActivity) {

            ((MainActivity) requireActivity())
                    .openFragment(fragment);

        } else {

            Toast.makeText(
                    requireContext(),
                    "Unable to open this screen.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
