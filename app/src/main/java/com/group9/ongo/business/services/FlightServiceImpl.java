package com.group9.ongo.business.services;

import com.group9.ongo.business.validation.FlightValidator;
import com.group9.ongo.business.validation.ValidationException;
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

    @Override
    public Flight getFlightById(int flightId) {
        Flight flight = repo.getFlightById(flightId);
        FlightValidator.validate(flight);
        return flight;
    }

    @Override
    public int createFlight(String airline, String origin, String destination, String departTime, String landTime, int capacity) {
        FlightValidator.validateNewFlight(airline, origin, destination, departTime, landTime, capacity);

        return repo.createFlight(airline, origin, destination, departTime, landTime, capacity);
    }

    @Override
    public boolean deleteFlight(int flightId) {
        boolean success = repo.deleteFlight(flightId);
        if ( success )
        {
            return true;
        }
        else
        {
           throw new ValidationException("Flight could not be deleted, since flight does not exist");
        }
    }


}
