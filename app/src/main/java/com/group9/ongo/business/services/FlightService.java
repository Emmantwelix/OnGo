package com.group9.ongo.business.services;

import com.group9.ongo.models.Flight;

import java.util.List;

public interface FlightService {
    List<Flight> getAllFlights();

    Flight getFlightById(int flightId);

    //returns the id of the flight that was added, -1 if invalid
    int addFlight(String airline, String origin, String destination, String departTime, String landTime, int capacity);

    boolean deleteFlight(int flightId);

}
