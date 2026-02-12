package com.group9.ongo.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.FlightService;

public class SearchFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Setup RecyclerView here
        RecyclerView recyclerView = view.findViewById(R.id.flightRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FlightService flightService = ((OnGoApp) getActivity().getApplication()).getFlightService();
        
        FlightAdapter adapter = new FlightAdapter(flightService.getAllFlights(), flight -> {
            // Navigate to UserInfoFragment when a flight is clicked, passing the flightId
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, UserInfoFragment.newInstance(flight.getFlightId()))
                    .addToBackStack(null)
                    .commit();
        });
        
        recyclerView.setAdapter(adapter);

        return view;
    }
}
