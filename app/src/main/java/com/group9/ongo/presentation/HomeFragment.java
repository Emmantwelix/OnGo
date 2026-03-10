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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.models.BookingDetails;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements BookingAdapter.BookingActionListener {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private TextView textNoBookings;
    private BookingService bookingService;

    private FlightService flightService;

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_bookings);
        textNoBookings = view.findViewById(R.id.text_no_bookings);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(new ArrayList<>(), flightService, this);
        recyclerView.setAdapter(adapter);

        loadBookings();

        return view;
    }

    private void loadBookings() {
        List<BookingDetails> bookings = bookingService.getBookingDetailsForCurrentUser();

        if (bookings.isEmpty()) {
            textNoBookings.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoBookings.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setBookings(bookings);
        }
    }

    @Override
    public void onModify(BookingDetails b) {
        Toast.makeText(getContext(), "Under Construction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(BookingDetails b) {
        try {
            bookingService.cancelBooking(b.getBooking().getBookingId());
            loadBookings();
            Toast.makeText(getContext(), "Booking Cancelled", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error cancelling booking", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditInfo(BookingDetails b) {
        Toast.makeText(getContext(), "Under Construction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewDetails(BookingDetails b) {
        Toast.makeText(getContext(), "Under Construction", Toast.LENGTH_SHORT).show();
    }
}
