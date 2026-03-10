package com.group9.ongo.persistence;

import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Flight;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

public interface FlightRepository {
    List<Flight> getAll();

    Flight getFlightById(int flightId);

    //return the id of the flight that was added, -1 if invalid
    int createFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int aircraftId, double price, String flightNum, LocalDate date);

    boolean deleteFlight(int flightId);

    void schedualeFlight(int flightId);

    void deScheduleFlight(int flightId);

}
