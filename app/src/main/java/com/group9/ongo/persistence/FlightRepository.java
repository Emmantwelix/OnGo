package com.group9.ongo.persistence;

import com.group9.ongo.models.Flight;

import java.util.List;

public interface FlightRepository {
    List<Flight> getAll();

    Flight getFlightById(int flightId);
}
