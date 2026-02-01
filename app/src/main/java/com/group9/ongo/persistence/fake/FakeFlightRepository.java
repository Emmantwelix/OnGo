package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.FlightRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeFlightRepository implements FlightRepository {

    private final List<Flight> flights = new ArrayList<>();

    public FakeFlightRepository() {
        flights.add(new Flight("Air Canada", "Toronto"));
        flights.add(new Flight("WestJet", "Montreal"));
    }

    @Override
    public List<Flight> getAll() {
        return Collections.unmodifiableList(flights);
    }
}
