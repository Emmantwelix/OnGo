package com.group9.ongo.presentation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.group9.ongo.R;
import com.group9.ongo.business.services.Implementations.SeatServiceImplementation;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Seat;
import com.group9.ongo.models.SeatMapConfig;

import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

    private SeatMapView seatMapView;
    private TextView seatInfoText;
    private Button confirmButton;
    private Seat selectedSeat;
    private int flightId;

    // In a real app, you'd use Dependency Injection or a Service Locator
    private SeatService seatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        flightId = getIntent().getIntExtra("FLIGHT_ID", -1);
        int aircraftId = getIntent().getIntExtra("AIRCRAFT_ID", 1);

        seatMapView = findViewById(R.id.seat_map_view);
        seatInfoText = findViewById(R.id.text_seat_info);
        confirmButton = findViewById(R.id.btn_confirm_seat);

        // Initialize SeatMapConfig based on aircraftId
        SeatMapConfig config;
        switch (aircraftId % 5) {
            case 0: config = SeatMapConfig.createBoeing737(); break;
            case 1: config = SeatMapConfig.createAirbusA320(); break;
            case 2: config = SeatMapConfig.createBoeing777(); break;
            case 3: config = SeatMapConfig.createBombardierCRJ900(); break;
            default: config = SeatMapConfig.createEmbraer190(); break;
        }

        List<Seat> seats = config.generateSeats();
        // Here you would typically fetch current occupancy from seatService and update status
        seatMapView.setSeatData(seats, config.generateSeats().size() / 30 > 6 ? 11 : 7); // Simplified column logic
        // The column count should match the layout string in config. ABC_DEF is 7 chars.

        // Fix column count logic based on layout string
        String layout = "ABC_DEF"; // Default for 737/A320
        if (aircraftId % 5 == 2) layout = "ABC_DEFG_HJK";
        else if (aircraftId % 5 >= 3) layout = "AC_DF";
        seatMapView.setSeatData(seats, layout.length());

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
                // In a real implementation, you'd call seatService.bookSeat
                // For this step, we'll just show a success message and finish
                Toast.makeText(this, "Seat " + selectedSeat.getRow() + selectedSeat.getLabel() + " confirmed!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
