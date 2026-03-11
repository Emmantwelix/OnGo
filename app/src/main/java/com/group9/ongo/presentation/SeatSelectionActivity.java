package com.group9.ongo.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Seat;
import com.group9.ongo.models.SeatMapConfig;

import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

    public static final String EXTRA_SELECTED_SEAT = "SELECTED_SEAT";

    private SeatMapView seatMapView;
    private TextView seatInfoText;
    private Button confirmButton;
    private Seat selectedSeat;
    private int flightId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        flightId = getIntent().getIntExtra("FLIGHT_ID", -1);
        
        seatMapView = findViewById(R.id.seat_map_view);
        seatInfoText = findViewById(R.id.text_seat_info);
        confirmButton = findViewById(R.id.btn_confirm_seat);

        OnGoApp app = (OnGoApp) getApplication();
        FlightService flightService = app.getFlightService();
        SeatService seatService = app.getSeatService();
        
        try {
            Flight flight = flightService.getFlightById(flightId);
            Aircraft aircraft = flightService.getAircraft(flight);
            
            if (aircraft != null) {
                // 1. Generate the standard map grid for this aircraft
                SeatMapConfig config = SeatMapConfig.createFromCapacity(aircraft.getModelName(), aircraft.getCapacity());
                List<Seat> gridSeats = config.generateSeats();
                
                // 2. Fetch actual booked status from the database
                List<Seat> realSeats = seatService.getAllSeatsByFlightId(flightId);
                
                // 3. Mark matching seats as OCCUPIED in our grid
                for (Seat gridSeat : gridSeats) {
                    if (gridSeat.getType() == Seat.Type.SEAT) {
                        for (Seat realSeat : realSeats) {
                            if (realSeat.getRow() == gridSeat.getRow() && 
                                realSeat.getLabel().equalsIgnoreCase(gridSeat.getLabel())) {
                                
                                if (realSeat.getIsBooked()) {
                                    gridSeat.setStatus(Seat.Status.OCCUPIED);
                                }
                                break;
                            }
                        }
                    }
                }
                
                seatMapView.setSeatData(gridSeats, config.getLayout().length());
                Toast.makeText(this, "Loading map for " + aircraft.getModelName(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading seat data", Toast.LENGTH_SHORT).show();
        }

        seatMapView.setSeatSelectionListener(seat -> {
            selectedSeat = seat;
            if (seat.getStatus() == Seat.Status.SELECTED) {
                seatInfoText.setText("Selected Seat: " + seat.getRow() + seat.getLabel());
                confirmButton.setEnabled(true);
            } else {
                seatInfoText.setText("Select a seat");
                confirmButton.setEnabled(false);
            }
        });

        confirmButton.setOnClickListener(v -> {
            if (selectedSeat != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_SEAT, selectedSeat.getRow() + selectedSeat.getLabel());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
