package com.group9.ongo.presentation;

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

        TextView textFlightId = view.findViewById(R.id.text_flight_id);
        TextView textAirline = view.findViewById(R.id.text_airline);
        TextView textRoute = view.findViewById(R.id.text_route);
        TextView textTimes = view.findViewById(R.id.text_times);
        TextView textDuration = view.findViewById(R.id.text_duration);
        TextView textCapacity = view.findViewById(R.id.text_capacity);
        TextView textPrice = view.findViewById(R.id.text_price);
        Button btnNext = view.findViewById(R.id.btn_next);

        FlightService flightService = ((OnGoApp) requireActivity().getApplication()).getFlightService();

        try {
            Flight flight = flightService.getFlightById(flightId);

            textFlightId.setText(String.format(Locale.ROOT, "Flight Number: %d", flight.getFlightId()));
            textAirline.setText(String.format("Airline: %s", flight.getAirline()));
            textRoute.setText(String.format("Route: %s to %s", flight.getOrigin(), flight.getDestination()));
            textTimes.setText(String.format("Time: %s - %s", flight.getDepartTimeString(), flight.getLandTimeString()));

            int hours = flightService.getDurationHours(flight);
            int mins = flightService.getDurationRemainingMinutes(flight);
            textDuration.setText(String.format(Locale.ROOT, "Duration: %dhr %dmin", hours, mins));

            textCapacity.setText(String.format(Locale.ROOT, "Capacity: %d seats", flight.getCapacity()));
            textPrice.setText(String.format(Locale.ROOT, "Price: $%.2f", flight.getPrice()));

            // Show the NEXT button once details are displayed
            btnNext.setVisibility(View.VISIBLE);

            btnNext.setOnClickListener(v -> {
                // Navigate to UserInfoFragment (Booking Information Fragment)
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
