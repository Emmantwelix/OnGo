package com.group9.ongo.presentation;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.FlightService;
import com.group9.ongo.models.Flight;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FlightService flightService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        flightService = ((OnGoApp) getApplication()).getFlightService();

        RecyclerView recyclerView = findViewById(R.id.flightRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Flight> flights = flightService.getAllFlights();

        FlightAdapter adapter = new FlightAdapter(flights);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_search) {
                Toast.makeText(this, "Search selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_settings) {
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

    }
}