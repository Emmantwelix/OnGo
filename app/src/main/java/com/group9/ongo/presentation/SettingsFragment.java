package com.group9.ongo.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.group9.ongo.R;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        MaterialCardView cardViewCancelled = view.findViewById(R.id.card_view_cancelled);
        cardViewCancelled.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CancelledBookingsActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
