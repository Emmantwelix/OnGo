package com.group9.ongo.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group9.ongo.R;

public class FlightDetailsFragment extends Fragment {

    private static final String ARG_FLIGHT_ID = "flight_id";
    private int flightId;

    public static FlightDetailsFragment newInstance(int flightId) {
        FlightDetailsFragment fragment = new FlightDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FLIGHT_ID, flightId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flightId = getArguments().getInt(ARG_FLIGHT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_details, container, false);
        TextView textView = view.findViewById(R.id.text_flight_details);
        textView.setText("DEtail fragment and the id of the flight " + flightId);
        return view;
    }
}
