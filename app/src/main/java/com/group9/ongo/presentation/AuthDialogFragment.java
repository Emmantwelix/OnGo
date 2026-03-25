package com.group9.ongo.presentation;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.group9.ongo.R;

public class AuthDialogFragment extends DialogFragment {

    private boolean isSignIn = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton buttonClose = view.findViewById(R.id.button_close);
        TextView textTitle = view.findViewById(R.id.text_auth_title);
        TextView textSubtitle = view.findViewById(R.id.text_auth_subtitle);
        TextInputLayout layoutName = view.findViewById(R.id.layout_name);
        TextInputLayout layoutPhone = view.findViewById(R.id.layout_phone);
        Button buttonSubmit = view.findViewById(R.id.button_auth_submit);
        TextView textSwitch = view.findViewById(R.id.text_switch_auth);

        buttonClose.setOnClickListener(v -> dismiss());

        textSwitch.setOnClickListener(v -> {
            isSignIn = !isSignIn;
            if (isSignIn) {
                textTitle.setText("Sign In");
                textSubtitle.setText("Welcome back! Please enter your details.");
                layoutName.setVisibility(View.GONE);
                layoutPhone.setVisibility(View.GONE);
                buttonSubmit.setText("Sign In");
                textSwitch.setText("Don't have an account? Sign Up");
            } else {
                textTitle.setText("Sign Up");
                textSubtitle.setText("Join Ongo today! Create your account.");
                layoutName.setVisibility(View.VISIBLE);
                layoutPhone.setVisibility(View.VISIBLE);
                buttonSubmit.setText("Sign Up");
                textSwitch.setText("Already have an account? Sign In");
            }
        });

        buttonSubmit.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
