package com.group9.ongo.business.services;

import com.group9.ongo.models.Flight;

import java.util.List;

public interface FlightService {
    List<Flight> getAllFlights();

    Flight getFlightById(int flightId);

    boolean addFlight(String airline, String destination, int departTime, int landTime, int capacity);

    boolean deleteFlight(int flightId);

}
