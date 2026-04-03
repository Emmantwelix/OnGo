package com.group9.ongo.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialCardView cardViewCancelled = view.findViewById(R.id.card_view_cancelled);
        cardViewCancelled.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CancelledBookingsActivity.class);
            startActivity(intent);
        });

        Button signOutBtn = view.findViewById(R.id.button_sign_out);
        TextView loginPrompt = view.findViewById(R.id.text_login_prompt);
        
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("OngoPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("current_user_id", -1);
        
        if (userId == -1) {
            signOutBtn.setVisibility(View.GONE);
            cardViewCancelled.setVisibility(View.GONE);
            loginPrompt.setVisibility(View.VISIBLE);
        } else {
            signOutBtn.setVisibility(View.VISIBLE);
            cardViewCancelled.setVisibility(View.VISIBLE);
            loginPrompt.setVisibility(View.GONE);
        }

        signOutBtn.setOnClickListener(v -> {
            sharedPref.edit().remove("current_user_id").apply();

            OnGoApp app = (OnGoApp) requireActivity().getApplication();
            app.updateBookingServiceUser(-1);

            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.navigation_home);
            }
        });
    }
}
