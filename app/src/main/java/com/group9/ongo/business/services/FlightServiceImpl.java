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
    public int createFlight(String airline, String origin, String destination, String departTime, String landTime, int capacity, double price) {
        FlightValidator.validateNewFlight(airline, origin, destination, departTime, landTime, capacity, price);

        return repo.createFlight(airline, origin, destination, departTime, landTime, capacity, price, calculateDuration(departTime, landTime));
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

    private int calculateDuration(String departTime, String landTime) {
        int departMinutes = toMinutes(departTime);
        int landMinutes = toMinutes(landTime);
        //handle overnight flights
        if (landMinutes < departMinutes) {
            landMinutes += 24 * 60;
        }
        //handle 24hr flights
        if ( landMinutes == departMinutes )
        {
            return 24;
        }

        float result = (float) (landMinutes - departMinutes) / 60;

        if (result < 1) {
            return 1;
        }

        return Math.round(result);
    }
    private static int toMinutes(String time) {
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(2, 4));
        return hours * 60 + minutes;
    }


}
