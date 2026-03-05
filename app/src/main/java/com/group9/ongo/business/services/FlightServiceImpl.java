package com.group9.ongo.business.services;

import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_DELETE_ERROR;

import com.group9.ongo.business.validation.FlightValidator;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.FlightRepository;

import java.time.Duration;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class FlightServiceImpl implements FlightService {

    private final FlightRepository repo;
    private final Generator fnGenerator;

    public FlightServiceImpl(FlightRepository repo, Generator fnGenerator) {
        this.repo = repo;
        this.fnGenerator = fnGenerator;
    }

    @Override
    public List<Flight> getAllFlights() {
        return repo.getAll();
    }

    @Override
    public Flight getFlightById(int flightId) throws ValidationException {
        Flight flight = repo.getFlightById(flightId);
        FlightValidator.validate(flight);
        return flight;
    }

    @Override
    public int createFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int capacity, double price, String planeType) throws ValidationException {
        FlightValidator.validateNewFlight(airline, origin, destination, departTime, landTime, capacity, price, planeType);

        String flightNumber = fnGenerator.generateFlightNum();
        int[] seats = fnGenerator.generateSeats(capacity);
        int occupation = calculate_occupation(seats);
        LocalDate date = fnGenerator.generateDate();


        return repo.createFlight(airline, origin, destination, departTime, landTime, capacity, price, flightNumber, planeType, occupation, seats, date);
    }

    @Override
    public boolean deleteFlight(int flightId) throws ValidationException {
        boolean success = repo.deleteFlight(flightId);
        if ( success )
        {
            return true;
        }
        else
        {
           throw new ValidationException(FLIGHT_DELETE_ERROR);
        }
    }


    //calculates the total minutes
    private int calculateDuration(Flight flight) {
        Duration duration = Duration.between(flight.getDepartTime(), flight.getLandTime());

        // handle overnight flights
        if (duration.isNegative()) {
            duration = duration.plusHours(24);
        }

        return (int) duration.toMinutes();
    }

    public int getDurationHours(Flight flight) {
        int totalMinutes = calculateDuration(flight);
        return totalMinutes / 60;
    }


    public int getDurationRemainingMinutes(Flight flight) {
        int totalMinutes = calculateDuration(flight);
        return totalMinutes % 60;
    }

    private int calculate_occupation(int[] seats)
    {
        int count = 0;
        for ( int i = 0; i < seats.length; i++ )
        {
            if ( seats[i] == 1 )
            {
                count++;
            }
        }
        return count;
    }
}
