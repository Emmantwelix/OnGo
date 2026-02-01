package com.group9.ongo.application;

import android.app.Application;

import com.group9.ongo.business.services.FlightService;
import com.group9.ongo.business.services.FlightServiceImpl;
import com.group9.ongo.persistence.FlightRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;

public class OnGoApp extends Application {

    private FlightService flightService;

    @Override
    public void onCreate() {
        super.onCreate();

        FlightRepository repo = new FakeFlightRepository();

        flightService = new FlightServiceImpl(repo);
    }

    public FlightService getFlightService() {
        return flightService;
    }
}