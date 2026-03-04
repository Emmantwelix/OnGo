package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.FlightRepository;

import java.time.LocalTime;
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
    private final String TORONTO = "Toronto";
    private final String MONTREAL = "Montreal";
    private final String VANCOUVER = "Vancouver";
    private final String WINNIPEG = "Winnipeg";

    //capacity
    private final int LARGE_CAPACITY = 200;
    private final int MEDIUM_CAPACITY = 150;
    private final int SMALL_CAPACITY = 100;

    private int nextId = 1;

    private final List<Flight> flights = new ArrayList<>();

    public FakeFlightRepository() {

    }

    public FakeFlightRepository(boolean populate) {
        if (populate) {
            populate_with_sample_data();
        }
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
    public int createFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int capacity, double price) {
        Flight flight = new Flight(nextId, airline, origin, destination, departTime, landTime, capacity, price);
        flights.add(flight);
        nextId++;
        return nextId - 1;
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
    private void populate_with_sample_data() {
        this.createFlight(AIR_CANADA, TORONTO, WINNIPEG, LocalTime.of(10, 0), LocalTime.of(12, 0), LARGE_CAPACITY, 603.49);
        this.createFlight(PORTER_AIRLINES, TORONTO, MONTREAL, LocalTime.of(12, 0), LocalTime.of(14, 0), MEDIUM_CAPACITY, 979.52);
        this.createFlight(AIR_TRANSAT, WINNIPEG, VANCOUVER, LocalTime.of(14, 0), LocalTime.of(16, 0), SMALL_CAPACITY, 200.01);
        this.createFlight(WESTJET, MONTREAL, WINNIPEG, LocalTime.of(16, 0), LocalTime.of(18, 0), LARGE_CAPACITY, 417.38);
    }
}
