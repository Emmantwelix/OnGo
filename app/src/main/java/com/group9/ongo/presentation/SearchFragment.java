package com.group9.ongo.presentation;

import static com.group9.ongo.business.constants.FlightConstants.ARR_LOCATIONS;
import static com.group9.ongo.business.constants.FlightConstants.ARR_SORT_FUNCTION;
import static com.group9.ongo.business.constants.FlightConstants.DATE;
import static com.group9.ongo.business.constants.FlightConstants.DURATION;
import static com.group9.ongo.business.constants.FlightConstants.AVAILABLE_SEAT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Flight;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.flightRecyclerView);
        AutoCompleteTextView depart = view.findViewById(R.id.etDepartingFrom);
        AutoCompleteTextView going = view.findViewById(R.id.etGoingTo);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        View noFlightsContainer = view.findViewById(R.id.noFlightsContainer);
        TextView textNoFlightsError = view.findViewById(R.id.textNoFlightsError);
        FlexboxLayout citiesContainer = view.findViewById(R.id.citiesContainer);
        TextView toggleAdvanced = view.findViewById(R.id.toggleAdvanced);
        LinearLayout advancedContainer = view.findViewById(R.id.advancedContainer);
        Spinner sortSpinner = view.findViewById(R.id.sortSpinner);

        FlightService flightService =
                ((OnGoApp) requireActivity().getApplication()).getFlightService();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_suggestion,
                R.id.suggestion,
                ARR_LOCATIONS
        );

        toggleAdvanced.setOnClickListener(v -> {
            if (advancedContainer.getVisibility() == View.GONE) {
                advancedContainer.setVisibility(View.VISIBLE);
            } else {
                advancedContainer.setVisibility(View.GONE);
            }
        });

        String[] sortOptions = ARR_SORT_FUNCTION;

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                sortOptions
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        depart.setAdapter(adapter);
        going.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final FlightAdapter flightAdapter = new FlightAdapter(
                List.of(),
                flightService,
                flight -> getParentFragmentManager().beginTransaction()
                        .replace(
                                R.id.fragment_container,
                                FlightDetailsFragment.newInstance(flight.getFlightId())
                        )
                        .addToBackStack(null)
                        .commit()
        );

        recyclerView.setAdapter(flightAdapter);
        populateAvailableCities(citiesContainer);

        recyclerView.setVisibility(View.VISIBLE);
        noFlightsContainer.setVisibility(View.GONE);

        final List<Flight> currentFlights = new ArrayList<>();
        final boolean[] ignoreFirstSpinnerEvent = {true};

        Runnable applySort = () -> {
            if (currentFlights.isEmpty()) {
                return;
            }

            List<Flight> flightsToDisplay = new ArrayList<>(currentFlights);
            String selectedSort = sortSpinner.getSelectedItem().toString();

            flightsToDisplay = switch (selectedSort) {
                case DURATION -> flightService.sortFlightsByDuration(flightsToDisplay);
                case AVAILABLE_SEAT -> flightService.sortFlightsByAvailSeats(flightsToDisplay);
                case DATE -> flightService.sortFlightsByDateTime(flightsToDisplay);
                default -> flightsToDisplay;
            };

            flightAdapter.updateFlights(flightsToDisplay);
            recyclerView.setVisibility(View.VISIBLE);
            noFlightsContainer.setVisibility(View.GONE);
        };

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ignoreFirstSpinnerEvent[0]) {
                    ignoreFirstSpinnerEvent[0] = false;
                    return;
                }

                applySort.run();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        btnSearch.setOnClickListener(v -> {
            String from = depart.getText().toString().trim();
            String to = going.getText().toString().trim();

            if (from.isEmpty() || to.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both cities", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                List<Flight> flights = flightService.searchFlights(from, to);

                currentFlights.clear();
                currentFlights.addAll(flights);

                applySort.run();

            } catch (ValidationException e) {
                currentFlights.clear();
                flightAdapter.updateFlights(List.of());
                recyclerView.setVisibility(View.GONE);
                noFlightsContainer.setVisibility(View.VISIBLE);
                textNoFlightsError.setText("No flights available from " + from + " to " + to);
            }
        });
    }

    private void populateAvailableCities(FlexboxLayout citiesContainer) {
        citiesContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (String city : ARR_LOCATIONS) {
            View cityChip = inflater.inflate(R.layout.item_city_chip, citiesContainer, false);
            TextView textCity = cityChip.findViewById(R.id.textCity);
            textCity.setText(city);
            citiesContainer.addView(cityChip);
        }
    }
}