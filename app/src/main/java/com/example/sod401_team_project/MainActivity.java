
package com.example.sod401_team_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the main activity layout.
        setContentView(R.layout.activity_main);

        // Show the Home screen when the app first opens MainActivity.
        if (savedInstanceState == null) {
            openFragment(new HomeFragment(), false);
        }
    }

    // Opens a Fragment inside the main screen.
    public void openFragment(Fragment fragment) {
        openFragment(fragment, true);
    }

    // Opens a Fragment and optionally adds it to the Back stack.
    private void openFragment(Fragment fragment, boolean addToBackStack) {

        androidx.fragment.app.FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction();

        transaction.replace(
                R.id.fragment_container,
                fragment
        );

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        // Return to the previous Fragment if one exists.
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
