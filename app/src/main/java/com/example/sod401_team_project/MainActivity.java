
package com.example.sod401_team_project;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the main activity layout.
        setContentView(R.layout.activity_main);

        // Show Home when MainActivity first opens.
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

    // Opens a Fragment inside the main screen.
    public void openFragment(Fragment fragment) {
        openFragment(fragment, true);
    }

    // Opens a Fragment and optionally adds it to the Back stack.
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
