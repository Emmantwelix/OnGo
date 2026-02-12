package com.group9.ongo.presentation;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group9.ongo.R;

public class MainActivity extends AppCompatActivity implements UserInfoFragment.OnBookingSuccessListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set default fragment (Home) on launch
        if (savedInstanceState == null) {
            navigateToHome();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = HomeFragment.newInstance();
            } else if (itemId == R.id.navigation_search) {
                selectedFragment = SearchFragment.newInstance();
            } else if (itemId == R.id.navigation_settings) {
                selectedFragment = SettingsFragment.newInstance();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }

    @Override
    public void onBookingSuccess() {
        navigateToHome();
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private void navigateToHome() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance())
                .commit();
    }
}
