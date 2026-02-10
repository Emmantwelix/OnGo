package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.FlightRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeFlightRepository implements FlightRepository {
    //airlines
    private final String AIR_CANADA = "Air Canada";
    private final String WESTJET = "Westjet";
    private final String AIR_TRANSAT = "Air Transat";
    private final String PORTER_AIRLINES = "Porter Airlines";
    //places
    private final String Toronto = "Toronto";
    private final String MONTREAL = "Montreal";
    private final String VANCOUVER = "Vancouver";
    private final String WINNIPEG = "Winnipeg";

    //capacity
    private final int LARGE_CAPACITY = 200;
    private final int MEDIUM_CAPACITY = 150;
    private final int SMALL_CAPACITY = 100;


    private final List<Flight> flights = new ArrayList<>();

    public FakeFlightRepository() {
        flights.add(new Flight(1, AIR_CANADA, VANCOUVER, 1000, 1200, LARGE_CAPACITY));
        flights.add(new Flight(2, WESTJET, MONTREAL, 1100, 1300, MEDIUM_CAPACITY));
        flights.add(new Flight(3, AIR_TRANSAT, WINNIPEG, 1200, 1400, SMALL_CAPACITY));
        flights.add(new Flight(4, PORTER_AIRLINES, Toronto, 1300, 1500, LARGE_CAPACITY));
    }

    @Override
    public List<Flight> getAll() {
        return Collections.unmodifiableList(flights);
    }

    @Override
    public Flight getFlightById(int flightId) {
        for (Flight flight : flights) {
            if (flight.getFlightId() == flightId) {
                return flight;
            }
        }
        return null;
    }

    @Override
    public boolean addFlight(Flight flight) {
        for (Flight f : flights) {
            if (f.getFlightId() == flight.getFlightId()) {
                return false;
            }
        }
        return flights.add(flight);
    }

    @Override
    public boolean deleteFlight(int flightId) {
        for (Flight flight : flights) {
            if (flight.getFlightId() == flightId) {
                return flights.remove(flight);
            }
        }
        return false;
    }
}
