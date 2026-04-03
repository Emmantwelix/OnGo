package com.group9.ongo.presentation;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_EMAIL;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_PASSWORD;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_PHONE_NUMBER;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.LoginService;
import com.group9.ongo.business.services.Interfaces.UserService;
import com.group9.ongo.business.validation.ValidationException;

public class AuthDialogFragment extends DialogFragment {

    private boolean isSignIn = true;
    private LoginService loginService;
    private UserService userService;

    public interface AuthListener {
        void onAuthSuccess();
    }

    private AuthListener listener;

    private TextInputEditText editName, editPhone, editEmail, editPassword;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof AuthListener) {
            listener = (AuthListener) getParentFragment();
        } else if (context instanceof AuthListener) {
            listener = (AuthListener) context;
        }
    }

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

        OnGoApp app = (OnGoApp) requireActivity().getApplication();
        loginService = app.getLoginService();
        userService = app.getUserService();

        ImageButton buttonClose = view.findViewById(R.id.button_close);
        TextView textTitle = view.findViewById(R.id.text_auth_title);
        TextView textSubtitle = view.findViewById(R.id.text_auth_subtitle);
        
        TextInputLayout layoutName = view.findViewById(R.id.layout_name);
        TextInputLayout layoutPhone = view.findViewById(R.id.layout_phone);
        
        editName = view.findViewById(R.id.edit_name);
        editPhone = view.findViewById(R.id.edit_phone);
        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);

        Button buttonSubmit = view.findViewById(R.id.button_auth_submit);
        TextView textSwitch = view.findViewById(R.id.text_switch_auth);

        buttonClose.setOnClickListener(v -> dismiss());

        textSwitch.setOnClickListener(v -> {
            isSignIn = !isSignIn;
            updateUI(textTitle, textSubtitle, layoutName, layoutPhone, buttonSubmit, textSwitch);
        });

        buttonSubmit.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (isSignIn) {
                handleSignIn(email, password);
            } else {
                String name = editName.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();
                handleSignUp(name, email, phone, password);
            }
        });
    }

    private void updateUI(TextView title, TextView subtitle, TextInputLayout name, TextInputLayout phone, Button submit, TextView switchText) {
        if (isSignIn) {
            title.setText("Sign In");
            subtitle.setText("Welcome back! Please enter your details.");
            name.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
            submit.setText("Sign In");
            switchText.setText("Don't have an account? Sign Up");
        } else {
            title.setText("Sign Up");
            subtitle.setText("Join Ongo today! Create your account.");
            name.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            submit.setText("Sign Up");
            switchText.setText("Already have an account? Sign In");
        }
    }

    private void handleSignIn(String email, String password) {
        int userId = loginService.login(email, password);
        
        if (userId != -1) {
            saveUserSession(userId);
            OnGoApp app = (OnGoApp) requireActivity().getApplication();
            app.updateBookingServiceUser(userId);
            
            Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onAuthSuccess();
            }
            dismiss();
        } else {
            Toast.makeText(getContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSignUp(String name, String email, String phone, String password) {
        try {
            int newUserId = userService.createUser(name, email, phone, password);
            if (newUserId != -1) {
                Toast.makeText(getContext(), "Account created! Please Sign In.", Toast.LENGTH_LONG).show();
                // Toggle back to sign in
                isSignIn = true;
                if (getView() != null) {
                    TextView textTitle = getView().findViewById(R.id.text_auth_title);
                    TextView textSubtitle = getView().findViewById(R.id.text_auth_subtitle);
                    TextInputLayout layoutName = getView().findViewById(R.id.layout_name);
                    TextInputLayout layoutPhone = getView().findViewById(R.id.layout_phone);
                    Button buttonSubmit = getView().findViewById(R.id.button_auth_submit);
                    TextView textSwitch = getView().findViewById(R.id.text_switch_auth);
                    updateUI(textTitle, textSubtitle, layoutName, layoutPhone, buttonSubmit, textSwitch);
                }
            }
        } catch (ValidationException e) {
            String field = e.getField();
            if (field == null) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (field) {
                case USER_NAME: editName.setError(e.getMessage()); break;
                case USER_PASSWORD: editPassword.setError(e.getMessage()); break;
                case USER_EMAIL: editEmail.setError(e.getMessage()); break;
                case USER_PHONE_NUMBER: editPhone.setError(e.getMessage()); break;
                default: Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show(); break;
            }
        }
    }

    private void saveUserSession(int userId) {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("OngoPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("current_user_id", userId);
        editor.apply();
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
