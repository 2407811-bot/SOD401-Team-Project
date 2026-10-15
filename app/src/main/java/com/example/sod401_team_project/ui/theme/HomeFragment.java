package com.example.sod401_team_project;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sod401_team_project.ui.theme.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the main activity layout.
        setContentView(R.layout.activity_main);

        // Open the Home screen when the app starts.
        if (savedInstanceState == null) {
            openFragment(new HomeFragment(), false);
        }

        // Handle the Android Back button.
        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        FragmentManager fragmentManager =
                                getSupportFragmentManager();

                        if (fragmentManager.getBackStackEntryCount() > 0) {
                            fragmentManager.popBackStack();
                        } else {
                            finish();
                        }
                    }
                }
        );
    }

    // Open another screen and allow returning to the previous screen.
    public void openFragment(Fragment fragment) {
        openFragment(fragment, true);
    }

    // Open a screen with optional back-stack support.
    public void openFragment(
            Fragment fragment,
            boolean addToBackStack) {

        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
