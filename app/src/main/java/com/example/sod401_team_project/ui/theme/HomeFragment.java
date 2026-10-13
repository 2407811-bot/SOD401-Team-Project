package com.example.sod401_team_project.ui.theme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sod401_team_project.EventsFragment;
import com.example.sod401_team_project.MainActivity;
import com.example.sod401_team_project.R;

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

        Button btnEvents = view.findViewById(R.id.btnEvents);

        if (btnEvents != null) {

            btnEvents.setOnClickListener(v -> {

                if (getActivity() instanceof MainActivity) {

                    ((MainActivity) getActivity())
                            .openFragment(new EventsFragment());
                }
            });
        }
    }
}
