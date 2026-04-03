package com.group9.ongo.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;

public class MainActivity extends AppCompatActivity implements UserInfoFragment.OnBookingSuccessListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // RESTORE USER SESSION HERE
        OnGoApp app = (OnGoApp) getApplication();
        SharedPreferences sharedPref = getSharedPreferences("OngoPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("current_user_id", -1);

        if (userId != -1) {
            app.updateBookingServiceUser(userId);
        }

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
