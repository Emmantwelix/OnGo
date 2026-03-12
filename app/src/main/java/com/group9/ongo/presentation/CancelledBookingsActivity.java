package com.group9.ongo.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.models.BookingDetails;

import java.util.ArrayList;
import java.util.List;

public class CancelledBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private TextView textNoBookings;
    private BookingService bookingService;
    private FlightService flightService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_bookings);

        bookingService = ((OnGoApp) getApplication()).getBookingService();
        flightService = ((OnGoApp) getApplication()).getFlightService();

        recyclerView = findViewById(R.id.recycler_cancelled_bookings);
        textNoBookings = findViewById(R.id.text_no_cancelled_bookings);
        ImageButton btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingAdapter(new ArrayList<>(), flightService);
        recyclerView.setAdapter(adapter);

        loadCancelledBookings();
    }

    private void loadCancelledBookings() {
        List<BookingDetails> bookings = bookingService.getCancelledBookingDetailsForCurrentUser();

        if (bookings.isEmpty()) {
            textNoBookings.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoBookings.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setBookings(bookings);
        }
    }
}
