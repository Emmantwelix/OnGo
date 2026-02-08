package com.group9.ongo.business.services;

import com.group9.ongo.models.FlightClass;
import com.group9.ongo.persistence.FlightRepository;

import java.util.List;

public class FlightServiceImpl implements FlightService {

    private final FlightRepository repo;

    public FlightServiceImpl(FlightRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<FlightClass> getAllFlights() {
        return repo.getAll();
    }

    public FlightClass getFlightById(int flightId) {
        return repo.getFlightById(flightId);
    }
}
