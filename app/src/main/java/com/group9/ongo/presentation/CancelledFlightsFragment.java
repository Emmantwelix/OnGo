package com.group9.ongo.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.models.BookingDetails;

import java.util.ArrayList;
import java.util.List;

public class CancelledFlightsFragment extends Fragment implements BookingAdapter.BookingActionListener {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private TextView textNoBookings;
    private BookingService bookingService;
    private FlightService flightService;

    public static CancelledFlightsFragment newInstance() {
        return new CancelledFlightsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingService = ((OnGoApp) requireActivity().getApplication()).getBookingService();
        flightService = ((OnGoApp) requireActivity().getApplication()).getFlightService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Reuse fragment_home layout which has recycler_bookings, text_home_title, and text_no_bookings
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Update title for this specific fragment
        TextView title = view.findViewById(R.id.text_home_title);
        if (title != null) {
            title.setText("Cancelled Flights");
        }

        recyclerView = view.findViewById(R.id.recycler_bookings);
        textNoBookings = view.findViewById(R.id.text_no_bookings);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Use the hideActions = true flag to hide buttons for cancelled flights
        adapter = new BookingAdapter(new ArrayList<>(), flightService, this, true);
        recyclerView.setAdapter(adapter);

        loadCancelledBookings();

        return view;
    }

    private void loadCancelledBookings() {
        List<BookingDetails> bookings = bookingService.getCancelledBookingsForCurrentUser();

        if (bookings.isEmpty()) {
            textNoBookings.setText("No cancelled flights found");
            textNoBookings.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoBookings.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setBookings(bookings);
        }
    }

    // Since hideActions is true, these won't be triggered from the UI
    @Override
    public void onModify(BookingDetails b) {}

    @Override
    public void onCancel(BookingDetails b) {}

    @Override
    public void onEditInfo(BookingDetails b) {}

    @Override
    public void onViewDetails(BookingDetails b) {}
}
