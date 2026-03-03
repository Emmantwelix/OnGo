package com.group9.ongo.persistence;

import com.group9.ongo.models.Flight;

import java.util.List;

public interface FlightRepository {
    List<Flight> getAll();

    Flight getFlightById(int flightId);

    //return the id of the flight that was added, -1 if invalid
    int createFlight(String airline, String origin, String destination, String departTime, String landTime, int capacity, double price, int duration);

    boolean deleteFlight(int flightId);

}
