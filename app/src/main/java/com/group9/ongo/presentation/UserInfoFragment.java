package com.group9.ongo.presentation;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.BookingException;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.PassengerInput;

public class UserInfoFragment extends Fragment {

    private static final String TAG = "UserInfoFragment";

    public interface OnBookingSuccessListener {
        void onBookingSuccess();
    }

    private static final String ARG_FLIGHT_ID = "flight_id";
    private int flightId;

    private TextInputEditText editFirstName, editLastName, editBirthDate, editPassportNumber;
    private TextView textFlightId;
    private Button btnConfirm;
    private OnBookingSuccessListener listener;

    public static UserInfoFragment newInstance(int flightId) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FLIGHT_ID, flightId);
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
        }
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

        textFlightId.setText("Booking for Flight: " + flightId);

        btnConfirm.setOnClickListener(v -> validateAndConfirm());

        return view;
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
            // This calls the business layer which does the validation
            int availSeatRow = flightService.getAnAvailableSeat(flightId).getSeatRow();
            String availSeatCol = flightService.getAnAvailableSeat(flightId).getSeatColumn();
            bookingService.createBooking(flightId, input, availSeatRow, availSeatCol);

            Toast.makeText(getContext(), "Booking confirmed for flight " + flightService.getFlightById(flightId).getFlightNumber() + " for " + firstName + " " + lastName, Toast.LENGTH_LONG).show();

            // Notify the activity that booking was successful
            if (listener != null) {
                listener.onBookingSuccess();
            }

        } catch (ValidationException e) {
            String message = e.getMessage();
            String field = e.getField();

            if (field == null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                return;
            }

            switch (field) {
                case "firstName":
                    editFirstName.setError(message);
                    break;
                case "lastName":
                    editLastName.setError(message);
                    break;
                case "birthDate":
                    editBirthDate.setError(message);
                    break;
                case "passport":
                    editPassportNumber.setError(message);
                    break;
                default:
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (BookingException e) {
            // Catch specific business logic exceptions
            Toast.makeText(getContext(), "Booking Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
