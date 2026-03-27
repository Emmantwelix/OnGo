package com.group9.ongo.presentation;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Airline;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.BookingStatus;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Passenger;

public class BookingDetailsActivity extends AppCompatActivity {

    private TextView textPassengerName;
    private TextView textStatus;
    private TextView textAirline;
    private TextView textFlightNumber;
    private TextView textOrigin;
    private TextView textDestination;
    private TextView textOriginTime;
    private TextView textDestinationTime;
    private TextView textOriginCode;
    private TextView textDestinationCode;

    private TextView textFlightDate;
    private TextView textPassportNumber;
    private TextView textBirthdate;
    private TextView textSeatNumber;
    private ShapeableImageView airlineLogo;
    private MaterialButton btnModifyBooking;
    private MaterialButton btnCancelBooking;

    private BookingService bookingService;

    private FlightService flightService;
    private int bookingId = -1;
    private BookingDetails bookingDetails;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        getWindow().setStatusBarColor(getColor(R.color.white));

        bookingService = ((OnGoApp) getApplication()).getBookingService();
        flightService =  ((OnGoApp) getApplication()).getFlightService();

        bookingId = getIntent().getIntExtra("booking_id", -1);

        if (bookingId == -1) {
            Toast.makeText(this, "Invalid booking", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        bindData();

        btnModifyBooking.setOnClickListener(v -> {
                ModifyBookingBottomSheet bottomSheet = new ModifyBookingBottomSheet(
                        bookingDetails.getPassenger(),
                        this::bindData
                );
                bottomSheet.show(getSupportFragmentManager(), "ModifyBookingBottomSheet");
        });

        btnCancelBooking.setOnClickListener(v -> {
            try {
                bookingService.cancelBooking(bookingId);
                Toast.makeText(this, "Booking cancelled", Toast.LENGTH_SHORT).show();
                finish();
            } catch (ValidationException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        textPassengerName = findViewById(R.id.text_passenger_name);
        textStatus = findViewById(R.id.text_status);
        textAirline = findViewById(R.id.text_airline);
        textFlightNumber = findViewById(R.id.text_flight_number);
        textFlightDate = findViewById(R.id.text_flight_date);
        textOrigin = findViewById(R.id.text_origin);
        textDestination = findViewById(R.id.text_destination);
        textOriginTime = findViewById(R.id.text_origin_time);
        textDestinationTime = findViewById(R.id.text_destination_time);
        textOriginCode = findViewById(R.id.text_origin_rcode);
        textDestinationCode = findViewById(R.id.text_destination_rcode);
        textPassportNumber = findViewById(R.id.text_passport_number);
        textSeatNumber = findViewById(R.id.text_seat_number);
        textBirthdate = findViewById(R.id.text_birthdate);
        airlineLogo = findViewById(R.id.airlineLogo);
        btnModifyBooking = findViewById(R.id.btn_modify_booking);
        btnCancelBooking = findViewById(R.id.btn_cancel_booking);
        btnBack = findViewById(R.id.btn_back);
    }

    private void bindData() {
        try {
            bookingDetails = bookingService.getBookingDetailsById(bookingId);
            Booking booking = bookingDetails.getBooking();
            Flight flight = bookingDetails.getFlight();
            Passenger passenger = bookingDetails.getPassenger();
            String formattedSeat = bookingDetails.getFormattedSeat();

            textPassengerName.setText(passenger.getFirstName() + " " + passenger.getLastName());
            textStatus.setText(booking.getBookingStatus());
            textAirline.setText(flight.getAirline());
            textFlightNumber.setText(flight.getFlightNumber());
            textOrigin.setText(flight.getOrigin());
            textDestination.setText(flight.getDestination());
            textOriginTime.setText(flight.getDepartTimeString());
            textDestinationTime.setText(flight.getLandTimeString());
            textFlightDate.setText(flight.getDateString());

            textOriginCode.setText(flightService.getAirportCode(flight.getOrigin()));
            textDestinationCode.setText(flightService.getAirportCode(flight.getDestination()));

            textPassportNumber.setText(passenger.getPassportNumber());
            textBirthdate.setText(passenger.getDateOfBirthFormatted());
            textSeatNumber.setText(formattedSeat);

            airlineLogo.setImageResource(
                    Airline.fromName(flight.getAirline()).getLogoResId()
            );

            GradientDrawable background = (GradientDrawable) textStatus.getBackground();
            if (booking.getStatus() == BookingStatus.CANCELLED) {
                background.setColor(Color.parseColor("#DC2626"));
                btnCancelBooking.setVisibility(View.GONE);
                btnModifyBooking.setVisibility(View.GONE);
            } else {
                background.setColor(Color.parseColor("#2F6FED"));
            }
        } catch(ValidationException e) {
            Toast.makeText(this, "Booking details unavailable", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
