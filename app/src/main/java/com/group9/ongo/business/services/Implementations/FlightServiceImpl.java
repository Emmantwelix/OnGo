package com.group9.ongo.business.services.Implementations;

import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_DELETE_ERROR;
import static com.group9.ongo.business.constants.ErrorMessageConstants.NO_AVAILABLE_SEAT;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_NOT_FOUND;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_1;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_2;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_3;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_4;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_5;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_6;
import static com.group9.ongo.business.constants.FlightConstants.MAX_COLUMNS;
import static com.group9.ongo.business.constants.FlightConstants.MAX_ROWS;

import static java.lang.Math.ceil;

import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.FlightValidator;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.FlightRepository;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.List;

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
    public int createFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, Aircraft aircraft, double price) throws ValidationException {
        FlightValidator.validateNewFlight(airline, origin, destination, departTime, landTime, aircraft, price);

        String flightNumber = fnGenerator.generateFlightNum();
        LocalDate date = fnGenerator.generateDate();

        int newFlightId = repo.createFlight(airline, origin, destination, departTime, landTime, aircraft, price, flightNumber, date);
        seatService.createSeats(newFlightId, aircraft.getCapacity());

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

    @Override
    public int getAvailableSeats(int flightId) {
        int availableSeats = 0;
        List <Seat> Seats = seatService.getAllSeatsByFlightId(flightId);
        for (Seat seat : Seats) {
            if (!seat.getIsBooked()) {
                availableSeats++;
            }
        }
        return availableSeats;
    }

    @Override
    public Seat getAnAvailableSeat(int flight_id) throws ValidationException {
        boolean availSeat = false;
        Seat seat1 = null;

        while (!availSeat) {
            List<Seat> seats = seatService.getAllSeatsByFlightId(flight_id);
            for (Seat seat : seats) {
                if (!seat.getIsBooked()) {
                    availSeat = true;
                    seat1 = seat;
                }
            }
        }

        if (!availSeat)
        {
            throw new ValidationException(NO_AVAILABLE_SEAT);
        }
        return seat1;
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

    @Override
    public int getDurationHours(Flight flight) {
        int totalMinutes = calculateDuration(flight);
        return totalMinutes / 60;
    }


    @Override
    public int getDurationRemainingMinutes(Flight flight) {
        int totalMinutes = calculateDuration(flight);
        return totalMinutes % 60;
    }

    private String getLocationCode(String location)
    {
        return location.length() >= 3 ? location.substring(0, 3).toUpperCase() : location.toUpperCase();
    }

    @Override
    public String getOriginCode(Flight flight)
    {
        return getLocationCode(flight.getOrigin());
    }

    @Override
    public String getDestinationCode(Flight flight)
    {
        return getLocationCode(flight.getDestination());
    }

    @Override
    public String getFormattedFlightId(Flight flight) {
        return String.format("AC %d", flight.getFlightId());
    }

    public void isFlightFull(int flightId)
    {
        int availSeats = getAvailableSeats(flightId);
        if (availSeats <= 0)
        {
            repo.deScheduleFlight(flightId);
        }
        else
        {
            repo.schedualeFlight(flightId);
        }
    }

}
