package com.group9.ongo.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.User;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

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

    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_bookings);
        textNoBookings = view.findViewById(R.id.text_no_bookings);
        Button buttonSignIn = view.findViewById(R.id.button_signin);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(new ArrayList<>(), flightService);
        recyclerView.setAdapter(adapter);

        buttonSignIn.setOnClickListener(v -> {
            AuthDialogFragment authDialog = new AuthDialogFragment();
            authDialog.show(getChildFragmentManager(), "AuthDialog");
        });

        loadBookings();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 1. Get User ID from SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("OngoPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("current_user_id", -1);

        if (userId != -1) {
            // 2. Get User details from service
            OnGoApp app = (OnGoApp) requireActivity().getApplication();
            try {
                User currentUser = app.getUserService().getUserById(userId); 
                
                if (currentUser != null) {
                    // 3. Update UI
                    TextView welcomeText = view.findViewById(R.id.text_welcome);
                    welcomeText.setText("Welcome, " + currentUser.getName());
                }
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadBookings() {
        // Refresh booking service in case user changed
        bookingService = ((OnGoApp) requireActivity().getApplication()).getBookingService();
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
}
