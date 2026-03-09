package com.group9.ongo.business.services;

import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_DELETE_ERROR;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_1;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_2;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_3;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_4;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_5;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_6;
import static com.group9.ongo.business.constants.FlightConstants.MAX_COLUMNS;
import static com.group9.ongo.business.constants.FlightConstants.MAX_ROWS;

import com.group9.ongo.business.validation.FlightValidator;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.FlightRepository;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class FlightServiceImpl implements FlightService {

    private final FlightRepository repo;
    private final Generator fnGenerator;
    private final SeatService seatService;


    public FlightServiceImpl(FlightRepository repo, Generator fnGenerator, SeatService seatService) {
        this.repo = repo;
        this.fnGenerator = fnGenerator;
        this.seatService = seatService;
    }

    private List<Flight> sortByPrice(List<Flight> flight) {
        List<Flight> sortedFlight = new ArrayList<>(flight);
        sortedFlight.sort(Comparator.comparingDouble(Flight::getPrice));
        return sortedFlight;
    }

    @Override
    public List<Flight> getAllFlights() {
        return sortByPrice(repo.getAll());
    }

    @Override
    public List<Seat> getSeats(int flightId) {
        return seatService.getAllSeatsByFlightId(flightId);
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
        LocalDate date = fnGenerator.generateDate();

        int newFlightId = repo.createFlight(airline, origin, destination, departTime, landTime, capacity, price, flightNumber, planeType, date);
        createSeats(newFlightId);

        return newFlightId;
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

    private void createSeats(int flightId) throws ValidationException {
        String letter = " ";
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if ( j  == 0) {
                    letter = COLUMN_1;
                } else if ( j == 1) {
                    letter = COLUMN_2;
                } else if ( j == 2) {
                    letter = COLUMN_3;
                } else if ( j == 3) {
                    letter = COLUMN_4;
                } else if ( j == 4) {
                    letter = COLUMN_5;
                } else {
                    letter = COLUMN_6;
                }
                seatService.createSeat(flightId, i+1, letter);
            }
        }
    }

    private String getLocationCode(String location)
    {
        return location.length() >= 3 ? location.substring(0, 3).toUpperCase() : location.toUpperCase();
    }

    public String getOriginCode(Flight flight)
    {
        return getLocationCode(flight.getOrigin());
    }

    public String getDestinationCode(Flight flight)
    {
        return getLocationCode(flight.getDestination());
    }


}
