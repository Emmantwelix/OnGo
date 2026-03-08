package com.group9.ongo.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.FlightService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Flight;

import java.util.Locale;

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

        // Bind views
        TextView textAirline = view.findViewById(R.id.text_airline);
        TextView textFlightId = view.findViewById(R.id.text_flight_id);
        TextView textOriginCode = view.findViewById(R.id.text_origin_code);
        TextView textOriginName = view.findViewById(R.id.text_origin_name);
        TextView textDestCode = view.findViewById(R.id.text_dest_code);
        TextView textDestName = view.findViewById(R.id.text_dest_name);
        TextView textDepartTime = view.findViewById(R.id.text_depart_time);
        TextView textDuration = view.findViewById(R.id.text_duration);
        TextView textCapacity = view.findViewById(R.id.text_capacity);
        TextView textPrice = view.findViewById(R.id.text_price);
        MaterialButton btnNext = view.findViewById(R.id.btn_next);

        FlightService flightService = ((OnGoApp) requireActivity().getApplication()).getFlightService();

        try {
            Flight flight = flightService.getFlightById(flightId);

            textAirline.setText(flight.getAirline());
            textFlightId.setText(String.format("AC %d", flight.getFlightId())); // Mocking prefix
            
            String origin = flight.getOrigin();
            String destination = flight.getDestination();
            
            textOriginCode.setText(flightService.getOriginCode(flight));
            textOriginName.setText(origin);
            
            textDestCode.setText(flightService.getDestinationCode(flight));
            textDestName.setText(destination);

            textDepartTime.setText(flight.getDepartTimeString());

            int hours = flightService.getDurationHours(flight);
            int mins = flightService.getDurationRemainingMinutes(flight);
            textDuration.setText(String.format(Locale.ROOT, "%dh %dm", hours, mins));

            textCapacity.setText(String.valueOf(flight.getCapacity()));
            textPrice.setText(String.format(Locale.ROOT, "$%.2f", flight.getPrice()));

            btnNext.setOnClickListener(v -> {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, UserInfoFragment.newInstance(flightId))
                        .addToBackStack(null)
                        .commit();
            });

        } catch (ValidationException e) {
            Toast.makeText(getContext(), "Error loading flight details", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }

        return view;
    }
}
