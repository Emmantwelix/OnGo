package com.group9.ongo.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Airline;
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
        TextView textOriginCity = view.findViewById(R.id.text_origin_city);
        TextView textDestCity = view.findViewById(R.id.text_dest_city);
        TextView textDateSubheader = view.findViewById(R.id.text_date_subheader);
        TextView textDepartTime = view.findViewById(R.id.text_depart_time);
        TextView textLandTime = view.findViewById(R.id.text_land_time);
        ImageView imageAirlineLogo = view.findViewById(R.id.image_airline_logo);
        TextView textAirline = view.findViewById(R.id.text_airline);
        TextView textFlightId = view.findViewById(R.id.text_flight_id);
        TextView textOriginCode = view.findViewById(R.id.text_origin_code);
        TextView textDestCode = view.findViewById(R.id.text_dest_code);
        TextView textDuration = view.findViewById(R.id.text_duration);
        TextView textAircraft = view.findViewById(R.id.text_aircraft);
        TextView textCapacity = view.findViewById(R.id.text_capacity);
        ImageView iconWifi = view.findViewById(R.id.icon_wifi);
        TextView textPrice = view.findViewById(R.id.text_price);
        TextView textAvailableSeats = view.findViewById(R.id.text_available_seats);
        MaterialButton btnNext = view.findViewById(R.id.btn_next);

        FlightService flightService = ((OnGoApp) requireActivity().getApplication()).getFlightService();

        try {
            Flight flight = flightService.getFlightById(flightId);

            // Set the airline logo and name using the Airline enum
            Airline airline = Airline.fromName(flight.getAirline());
            imageAirlineLogo.setImageResource(airline.getLogoResId());
            textAirline.setText(flight.getAirline());
            textFlightId.setText(flight.getFlightNumber());

            // Set city names separately for the new centered layout
            textOriginCity.setText(flight.getOrigin());
            textDestCity.setText(flight.getDestination());

            // Sub-header: Only keeping the text as date is not available in the model
            textDateSubheader.setText(String.format(Locale.ROOT, "%s", flight.getDateString()));

            // Timeline & Codes
            textDepartTime.setText(flight.getDepartTimeString());
            textLandTime.setText(flight.getLandTimeString());
            textOriginCode.setText(flightService.getOriginCode(flight));
            textDestCode.setText(flightService.getDestinationCode(flight));

            // Flight Details
            int hours = flightService.getDurationHours(flight);
            int mins = flightService.getDurationRemainingMinutes(flight);
            textDuration.setText(String.format(Locale.ROOT, "%dh %dm", hours, mins));
            textAvailableSeats.setText(String.format(Locale.ROOT, "%d Seats Available", flightService.getAvailableSeats(flightId)));

            // Aircraft Info
            Aircraft aircraft = flight.getAircraft();
            if (aircraft != null) {
                textAircraft.setText(aircraft.getModelName());
                textCapacity.setText(aircraft.getCapacityString());
                iconWifi.setVisibility(aircraft.hasWifi() ? View.VISIBLE : View.GONE);
            }

            // Footer
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
