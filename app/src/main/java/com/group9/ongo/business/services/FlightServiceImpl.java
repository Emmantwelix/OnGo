package com.group9.ongo.business.services;

import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.FlightRepository;

import java.util.List;

public class FlightServiceImpl implements FlightService {

    private final FlightRepository repo;

    public FlightServiceImpl(FlightRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Flight> getAllFlights() {
        return repo.getAll();
    }

    public Flight getFlightById(int flightId) {
        return repo.getFlightById(flightId);
    }
}
