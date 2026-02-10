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
    private final String TORONTO = "Toronto";
    private final String MONTREAL = "Montreal";
    private final String VANCOUVER = "Vancouver";
    private final String WINNIPEG = "Winnipeg";

    //capacity
    private final int LARGE_CAPACITY = 200;
    private final int MEDIUM_CAPACITY = 150;
    private final int SMALL_CAPACITY = 100;


    private final List<Flight> flights = new ArrayList<>();

    public FakeFlightRepository() {
        flights.add(new Flight(1,AIR_CANADA, TORONTO, VANCOUVER, "10:00", "12:00", LARGE_CAPACITY));
        flights.add(new Flight(2,WESTJET, WINNIPEG, MONTREAL, "11:00", "13:00", MEDIUM_CAPACITY));
        flights.add(new Flight(3,AIR_TRANSAT,VANCOUVER, WINNIPEG, "12:00", "14:00", SMALL_CAPACITY));
        flights.add(new Flight(4,PORTER_AIRLINES,MONTREAL, TORONTO, "13:00", "15:00", LARGE_CAPACITY));
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
}
