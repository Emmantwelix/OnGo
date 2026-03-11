package com.group9.ongo.presentation;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.BookingException;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.PassengerInput;

public class UserInfoFragment extends Fragment {

    private static final String TAG = "UserInfoFragment";

    public interface OnBookingSuccessListener {
        void onBookingSuccess();
    }

    private static final String ARG_FLIGHT_ID = "flight_id";
    private static final String ARG_SELECTED_SEAT = "selected_seat";
    private int flightId;
    private String selectedSeat;

    private TextInputEditText editFirstName, editLastName, editBirthDate, editPassportNumber;
    private TextView textFlightId;
    private Button btnConfirm;
    private MaterialButton btnSelectSeat;
    private OnBookingSuccessListener listener;

    private ActivityResultLauncher<Intent> seatSelectionLauncher;

    public static UserInfoFragment newInstance(int flightId, String selectedSeat) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FLIGHT_ID, flightId);
        args.putString(ARG_SELECTED_SEAT, selectedSeat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnBookingSuccessListener) {
            listener = (OnBookingSuccessListener) context;
        } else {
            Log.e(TAG, context.toString() + " must implement OnBookingSuccessListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flightId = getArguments().getInt(ARG_FLIGHT_ID);
            selectedSeat = getArguments().getString(ARG_SELECTED_SEAT);
        }

        // Handle seat selection result
        seatSelectionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedSeat = result.getData().getStringExtra(SeatSelectionActivity.EXTRA_SELECTED_SEAT);
                        updateSeatDisplay();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        textFlightId = view.findViewById(R.id.text_flight_id);
        editFirstName = view.findViewById(R.id.edit_first_name);
        editLastName = view.findViewById(R.id.edit_last_name);
        editBirthDate = view.findViewById(R.id.edit_birth_date);
        editPassportNumber = view.findViewById(R.id.edit_passport_number);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnSelectSeat = view.findViewById(R.id.btn_select_seat_user_info);

        textFlightId.setText("Booking for Flight: " + flightId);
        updateSeatDisplay();

        btnSelectSeat.setOnClickListener(v -> {
            FlightService flightService = ((OnGoApp) requireActivity().getApplication()).getFlightService();
            try {
                Flight flight = flightService.getFlightById(flightId);
                Aircraft aircraft = flightService.getAircraft(flight);
                if (aircraft != null) {
                    Intent intent = new Intent(getContext(), SeatSelectionActivity.class);
                    intent.putExtra("FLIGHT_ID", flightId);
                    intent.putExtra("AIRCRAFT_ID", aircraft.getAircraftId());
                    seatSelectionLauncher.launch(intent);
                }
            } catch (ValidationException e) {
                Toast.makeText(getContext(), "Error opening seat map", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirm.setOnClickListener(v -> validateAndConfirm());

        return view;
    }

    private void updateSeatDisplay() {
        if (selectedSeat != null && !selectedSeat.isEmpty()) {
            btnSelectSeat.setText("Change Seat (" + selectedSeat + ")");
        } else {
            btnSelectSeat.setText("Choose Your Seat");
        }
    }

    private void validateAndConfirm() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String birthDateStr = editBirthDate.getText().toString().trim();
        String passportNumber = editPassportNumber.getText().toString().trim();

        PassengerInput input = new PassengerInput(firstName, lastName, birthDateStr, passportNumber);

        BookingService bookingService = ((OnGoApp) getActivity().getApplication()).getBookingService();
        FlightService flightService = ((OnGoApp) getActivity().getApplication()).getFlightService();

        try {
            int seatRow;
            String seatCol;
            
            if (selectedSeat != null && !selectedSeat.isEmpty()) {
                seatRow = Integer.parseInt(selectedSeat.replaceAll("[^0-9]", ""));
                seatCol = selectedSeat.replaceAll("[0-9]", "");
            } else {
                Toast.makeText(getContext(), "Please select a seat before confirming", Toast.LENGTH_SHORT).show();
                return;
            }
            
            bookingService.createBooking(flightId, input, seatRow, seatCol);

            Toast.makeText(getContext(), "Booking confirmed for seat " + selectedSeat, Toast.LENGTH_LONG).show();

            if (listener != null) {
                listener.onBookingSuccess();
            }

        } catch (ValidationException e) {
            String field = e.getField();
            if (field == null) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (field) {
                case "firstName": editFirstName.setError(e.getMessage()); break;
                case "lastName": editLastName.setError(e.getMessage()); break;
                case "birthDate": editBirthDate.setError(e.getMessage()); break;
                case "passport": editPassportNumber.setError(e.getMessage()); break;
                default: Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show(); break;
            }
        } catch (BookingException e) {
            Toast.makeText(getContext(), "Booking Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Error parsing seat selection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
