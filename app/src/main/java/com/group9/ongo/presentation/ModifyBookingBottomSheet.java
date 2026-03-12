package com.group9.ongo.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.PassengerService;
import com.group9.ongo.models.Passenger;

public class ModifyBookingBottomSheet extends BottomSheetDialogFragment {

    private final Passenger passenger;
    private final Runnable onUpdateSuccess;

    public ModifyBookingBottomSheet(Passenger passenger, Runnable onUpdateSuccess) {
        this.passenger = passenger;
        this.onUpdateSuccess = onUpdateSuccess;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_modify_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText editFirstName = view.findViewById(R.id.edit_first_name);
        TextInputEditText editLastName = view.findViewById(R.id.edit_last_name);
        TextInputEditText editDob = view.findViewById(R.id.edit_dob);
        TextInputEditText editPassport = view.findViewById(R.id.edit_passport);
        Button btnSave = view.findViewById(R.id.btn_save_changes);

        // Pre-fill data
        editFirstName.setText(passenger.getFirstName());
        editLastName.setText(passenger.getLastName());
        editDob.setText(passenger.getDateOfBirth().toString()); // Using ISO format for editing
        editPassport.setText(passenger.getPassportNumber());

        PassengerService passengerService = ((OnGoApp) requireActivity().getApplication()).getPassengerService();

        btnSave.setOnClickListener(v -> {
            String fName = editFirstName.getText().toString();
            String lName = editLastName.getText().toString();
            String dob = editDob.getText().toString();
            String passport = editPassport.getText().toString();

            boolean success = passengerService.updatePassengerInfo(
                    String.valueOf(passenger.getPassengerId()),
                    fName,
                    lName,
                    dob,
                    passport
            );

            if (success) {
                Toast.makeText(getContext(), "Passenger updated successfully", Toast.LENGTH_SHORT).show();
                if (onUpdateSuccess != null) {
                    onUpdateSuccess.run();
                }
                dismiss();
            } else {
                Toast.makeText(getContext(), "Failed to update passenger. Check your inputs.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
