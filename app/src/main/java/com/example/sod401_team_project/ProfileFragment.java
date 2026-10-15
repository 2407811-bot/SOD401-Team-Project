
package com.example.sod401_team_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor.
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_profile,
                container,
                false
        );

        // Return to the previous screen.
        view.findViewById(R.id.btnHome).setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        // Log out and return to the login screen.
        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Intent intent = new Intent(
                    requireActivity(),
                    LoginActivity.class
            );

            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}
