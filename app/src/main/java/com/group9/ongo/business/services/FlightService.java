package com.group9.ongo.business.services;

import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Seat;

import java.time.LocalTime;
import java.util.List;

public interface FlightService {
    List<Flight> getAllFlights();
    List <Seat> getSeats(int flightId);

    Flight getFlightById(int flightId) throws ValidationException;

    //returns the id of the flight that was added, will throw if invalid
    int createFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int capacity, double price, String planeType) throws ValidationException;

    boolean deleteFlight(int flightId) throws ValidationException;

    int getDurationHours(Flight flight);

    int getDurationRemainingMinutes(Flight flight);

    String getOriginCode(Flight flight);

    String getDestinationCode(Flight flight);
}
