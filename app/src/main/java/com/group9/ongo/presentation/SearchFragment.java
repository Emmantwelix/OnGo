package com.group9.ongo.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.constants.FlightConstants;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Flight;

import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.flightRecyclerView);
        AutoCompleteTextView depart = view.findViewById(R.id.etDepartingFrom);
        AutoCompleteTextView going = view.findViewById(R.id.etGoingTo);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        FlightService flightService = ((OnGoApp) getActivity().getApplication()).getFlightService();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.item_suggestion, R.id.suggestion, FlightConstants.ARR_LOCATIONS);
        depart.setAdapter(adapter);
        going.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnSearch.setOnClickListener(v -> {
            String from = depart.getText().toString().trim();
            String to = going.getText().toString().trim();
            if (!from.isEmpty() && !to.isEmpty()){
                from = from.substring(0,1).toUpperCase(Locale.ROOT) + from.substring(1);
                to = to.substring(0,1).toUpperCase(Locale.ROOT) + to.substring(1);
            }
            try {
                List<Flight> flights = flightService.searchFlights(from, to);

                FlightAdapter flightadapter = new FlightAdapter(flights, flightService, flight -> {
                    // Navigate to FlightDetailsFragment when a flight is clicked, passing the flightId
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, FlightDetailsFragment.newInstance(flight.getFlightId()))
                            .addToBackStack(null)
                            .commit();
                });

                recyclerView.setAdapter(flightadapter);
            } catch (ValidationException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}