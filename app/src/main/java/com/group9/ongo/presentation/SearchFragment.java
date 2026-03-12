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
import com.group9.ongo.business.services.Interfaces.FlightService;

public class SearchFragment extends Fragment {

    private LinearLayout searchContainer;
    private RecyclerView recyclerView;
    private AutoCompleteTextView depart, going;
    private Button btnSearch;
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Setup RecyclerView here
//        RecyclerView recyclerView = view.findViewById(R.id.flightRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        FlightService flightService = ((OnGoApp) getActivity().getApplication()).getFlightService();
//
//        FlightAdapter adapter = new FlightAdapter(flightService.getAllFlights(), flightService, flight -> {
//            // Navigate to FlightDetailsFragment when a flight is clicked, passing the flightId
//            getParentFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, FlightDetailsFragment.newInstance(flight.getFlightId()))
//                    .addToBackStack(null)
//                    .commit();
//        });
//
//        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        searchContainer = view.findViewById(R.id.searchContainer);
        recyclerView = view.findViewById(R.id.flightRecyclerView);
        depart = view.findViewById(R.id.etDepartingFrom);
        going = view.findViewById(R.id.etGoingTo);
        btnSearch = view.findViewById(R.id.btnSearch);
        FlightService flightService = ((OnGoApp) getActivity().getApplication()).getFlightService();

        // TODO Needs an array of cities
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
//                android.R.layout.simple_dropdown_item_1line, );
//        depart.setAdapter(adapter);
//        going.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // flightRecyclerView.setAdapter(yourAdapter); // Set your flight adapter here

        btnSearch.setOnClickListener(v -> {
            String from = depart.getText().toString().trim();
            String to = going.getText().toString().trim();

            if (!from.isEmpty() && !to.isEmpty()) {
                executeSearchTransition(from, to);
            } else {
                Toast.makeText(getContext(), "Please enter both locations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void executeSearchTransition(String from, String to) {
        //hideKeyboard();

        searchContainer.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    searchContainer.setVisibility(View.GONE);

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAlpha(0f);
                    recyclerView.animate()
                            .alpha(1f)
                            .setDuration(300)
                            .start();

                    // TODO Needs search logic
                    // flightAdapter.filterByRoute(from, to);
                }).start();
    }

}