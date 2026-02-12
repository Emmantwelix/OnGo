package com.group9.ongo.presentation;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UserInfoFragment extends Fragment {

    private static final String ARG_FLIGHT_ID = "flight_id";
    private int flightId;

    private TextInputEditText editFirstName, editLastName, editBirthDate, editPassportNumber;
    private TextView textFlightId;
    private Button btnConfirm;

    public static UserInfoFragment newInstance(int flightId) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FLIGHT_ID, flightId);
        fragment.setArguments(args);
        return fragment;
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
        String birthDate = editBirthDate.getText().toString().trim();
        String passportNumber = editPassportNumber.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            editFirstName.setError("First name is required");
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            editLastName.setError("Last name is required");
            return;
        }

        if (TextUtils.isEmpty(birthDate)) {
            editBirthDate.setError("Birth date is required");
            return;
        }

        if (!isValidDate(birthDate)) {
            editBirthDate.setError("Use format YYYY-MM-DD (e.g., 1990-01-01)");
            return;
        }

        if (TextUtils.isEmpty(passportNumber)) {
            editPassportNumber.setError("Passport number is required");
            return;
        }

        // Validation passed
        Toast.makeText(getContext(), "Booking confirmed for flight " + flightId + " for " + firstName + " " + lastName, Toast.LENGTH_LONG).show();
        
        // Go back to Home
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }

    private boolean isValidDate(String dateStr) {
        if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
