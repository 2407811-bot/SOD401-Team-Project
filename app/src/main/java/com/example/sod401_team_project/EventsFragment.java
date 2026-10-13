
package com.example.sod401_team_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        // Campus Events card
        MaterialCardView btnEvents =
                view.findViewById(R.id.btnEvents);

        btnEvents.setOnClickListener(v ->
                openScreen(new EventsFragment())
        );

        // Announcements card
        MaterialCardView btnAnnouncements =
                view.findViewById(R.id.btnAnnouncements);

        btnAnnouncements.setOnClickListener(v ->
                openScreen(new AnnouncementsFragment())
        );

        // My Profile card
        MaterialCardView btnProfile =
                view.findViewById(R.id.btnProfile);

        btnProfile.setOnClickListener(v ->
                openScreen(new ProfileFragment())
        );

        // Explore Campus card
        MaterialCardView btnExploreCampus =
                view.findViewById(R.id.btnExploreCampus);

        btnExploreCampus.setOnClickListener(v ->
                Toast.makeText(
                        requireContext(),
                        "Explore Campus is coming soon!",
                        Toast.LENGTH_SHORT
                ).show()
        );

        // Logout button
        ImageButton btnLogout =
                view.findViewById(R.id.btnHomeLogout);

        btnLogout.setOnClickListener(v -> {

            Toast.makeText(
                    requireContext(),
                    "Logout clicked",
                    Toast.LENGTH_SHORT
            ).show();

            // We can connect this to LoginActivity
            // after confirming your existing login flow.
        });
    }

    private void openScreen(Fragment fragment) {

        if (getActivity() instanceof MainActivity) {

            ((MainActivity) getActivity())
                    .openFragment(fragment);
        }
    }
}
