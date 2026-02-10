package com.group9.ongo.persistence;

import com.group9.ongo.models.Flight;

import java.util.List;

public interface FlightRepository {
    List<Flight> getAll();

    Flight getFlightById(int flightId);

    boolean createFlight(String airline, String destination, int departTime, int landTime, int capacity);

    boolean deleteFlight(int flightId);

}
