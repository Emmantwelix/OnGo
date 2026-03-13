package com.group9.ongo.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.group9.ongo.business.constants.FlightConstants;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Flight;

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

        FlightService flightService =
                ((OnGoApp) requireActivity().getApplication()).getFlightService();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_suggestion,
                R.id.suggestion,
                FlightConstants.ARR_LOCATIONS
        );

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

        btnSearch.setOnClickListener(v -> {
            String from = depart.getText().toString().trim();
            String to = going.getText().toString().trim();

            if (from.isEmpty() || to.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both cities", Toast.LENGTH_SHORT).show();
                return;
            }


            try {
                List<Flight> flights = flightService.searchFlights(from, to);

                flightAdapter.updateFlights(flights);
                recyclerView.setVisibility(View.VISIBLE);
                noFlightsContainer.setVisibility(View.GONE);

            } catch (ValidationException e) {
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

        for (String city : FlightConstants.ARR_LOCATIONS) {
            View cityChip = inflater.inflate(R.layout.item_city_chip, citiesContainer, false);
            TextView textCity = cityChip.findViewById(R.id.textCity);
            textCity.setText(city);
            citiesContainer.addView(cityChip);
        }
    }

}