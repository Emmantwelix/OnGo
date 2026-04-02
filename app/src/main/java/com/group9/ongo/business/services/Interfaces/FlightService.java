package com.group9.ongo.business.services.Interfaces;

import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Seat;

import java.time.LocalTime;
import java.util.List;

public interface FlightService {
    List<Flight> getAllFlights();
    List<Flight> sortFlightsByDuration(List<Flight> flights); //lowest to highest
    List<Flight> sortFlightsByDateTime(List<Flight> flights); //lowest to highest
    List<Flight> sortFlightsByAvailSeats(List<Flight> flights); //lowest to highest
    List<Flight> searchFlights(String origin, String destination) throws ValidationException;

    Flight getFlightById(int flightId) throws ValidationException;

    //returns the id of the flight that was added, will throw if invalid
    int createFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int aircraftId, double price) throws ValidationException;

    boolean deleteFlight(int flightId) throws ValidationException;

    int getDurationHours(Flight flight);

    int getDurationRemainingMinutes(Flight flight);
    int getAvailableSeats(int flightId);
    String getFormattedFlightId(Flight flight);

    void updateFlightAvailability(int flightId);
    String getAirportCode(String city);
    Aircraft getAircraft(Flight flight);
}
