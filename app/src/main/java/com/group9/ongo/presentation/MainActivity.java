package com.group9.ongo.presentation;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.FlightService;
import com.group9.ongo.models.FlightClass;

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

        List<FlightClass> flights = flightService.getAllFlights();

        FlightAdapter adapter = new FlightAdapter(flights);
        recyclerView.setAdapter(adapter);

    }
}