package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.FlightClass;
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




    private final List<FlightClass> flights = new ArrayList<>();

    public FakeFlightRepository() {
        flights.add(new FlightClass(AIR_CANADA, VANCOUVER, 1000, 1200, LARGE_CAPACITY));
        flights.add(new FlightClass(WESTJET, MONTREAL, 1100, 1300, MEDIUM_CAPACITY));
        flights.add(new FlightClass(AIR_TRANSAT, WINNIPEG, 1200, 1400, SMALL_CAPACITY));
        flights.add(new FlightClass(PORTER_AIRLINES, Toronto, 1300, 1500, LARGE_CAPACITY));
    }

    @Override
    public List<FlightClass> getAll() {
        return Collections.unmodifiableList(flights);
    }
}
