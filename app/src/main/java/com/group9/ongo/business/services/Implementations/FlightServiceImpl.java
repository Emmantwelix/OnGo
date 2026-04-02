package com.group9.ongo.business.services.Implementations;

import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_DELETE_ERROR;
import static com.group9.ongo.business.constants.ErrorMessageConstants.NO_AVAILABLE_SEAT;
import static com.group9.ongo.business.constants.ErrorMessageConstants.NO_FLIGHTS_AVAILABLE;
import static com.group9.ongo.business.constants.FlightConstants.BC;
import static com.group9.ongo.business.constants.FlightConstants.CALGARY;
import static com.group9.ongo.business.constants.FlightConstants.CALGARY_CODE;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_CODE;
import static com.group9.ongo.business.constants.FlightConstants.MONTREAL;
import static com.group9.ongo.business.constants.FlightConstants.MONTREAL_CODE;
import static com.group9.ongo.business.constants.FlightConstants.QUEBEC_CITY;
import static com.group9.ongo.business.constants.FlightConstants.QUEBEC_CITY_CODE;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO_CODE;
import static com.group9.ongo.business.constants.FlightConstants.VANCOUVER;
import static com.group9.ongo.business.constants.FlightConstants.VANCOUVER_CODE;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG_CODE;
import static com.group9.ongo.business.validation.FlightValidator.validateLocation;

import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.AircraftValidator;
import com.group9.ongo.business.validation.FlightValidator;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.AircraftRepository;
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
    private final AircraftRepository aircraftRepo;


    public FlightServiceImpl(FlightRepository repo, Generator fnGenerator, SeatService seatService, AircraftRepository aircraftRepo) {
        this.repo = repo;
        this.fnGenerator = fnGenerator;
        this.seatService = seatService;
        this.aircraftRepo = aircraftRepo;
    }

    @Override
    public List<Flight> searchFlights(String origin, String destination) throws ValidationException {

        String from = formatSearchLocation(origin);
        String to  = formatSearchLocation(destination);

        validateLocation(from, to);
        List<Flight> flights = repo.searchFlights(from, to);

        if (flights.isEmpty()){
            throw new ValidationException(NO_FLIGHTS_AVAILABLE);
        }
        return sortByPrice(flights);
    }

    @Override
    public List<Flight> getAllFlights() {
        return repo.getAll();
    }

    @Override
    public List<Flight> sortFlightsByDuration(List<Flight> flights) {
        List<Flight> sortedFlight = new ArrayList<>(flights);
        sortedFlight.sort(Comparator.comparingInt(this::calculateDuration));
        return sortedFlight;
    }

    @Override
    public List<Flight> sortFlightsByDateTime(List<Flight> flights) {
        List<Flight> sortedFlight = new ArrayList<>(flights);
        sortedFlight.sort(Comparator.comparing(Flight::getDate).thenComparing(Flight::getDepartTime));
        return sortedFlight;
    }

    @Override
    public List<Flight> sortFlightsByAvailSeats(List<Flight> flights) {
        List<Flight> sortedFlight = new ArrayList<>(flights);
        sortedFlight.sort(Comparator.comparingInt(this::getAvailableSeats));
        return sortedFlight;
    }



    @Override
    public Flight getFlightById(int flightId) throws ValidationException {
        Flight flight = repo.getFlightById(flightId);
        FlightValidator.validate(flight);
        return flight;
    }

    @Override
    public int createFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int aircraftId, double price) throws ValidationException {

        Aircraft aircraft = aircraftRepo.getAircraftById(aircraftId);

        AircraftValidator.validate(aircraft);

        FlightValidator.validateNewFlight(airline, origin, destination, departTime, landTime, price);

        String flightNumber = fnGenerator.generateFlightNum();
        LocalDate date = fnGenerator.generateDate();

        int newFlightId = repo.createFlight(airline, origin, destination, departTime, landTime, aircraftId, price, flightNumber, date);
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

    private int getAvailableSeats(Flight flight){
        return getAvailableSeats(flight.getFlightId());
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

    @Override
    public String getFormattedFlightId(Flight flight) {
        return String.format("AC %d", flight.getFlightId());
    }

    @Override
    public void updateFlightAvailability(int flightId)
    {
        int availSeats = getAvailableSeats(flightId);
        if (availSeats <= 0)
        {
            repo.deScheduleFlight(flightId);
        }
        else
        {
            repo.scheduleFlight(flightId);
        }
    }
    
    @Override
    public String getAirportCode(String city) {
        return switch (city) {
            case TORONTO -> TORONTO_CODE;
            case WINNIPEG -> WINNIPEG_CODE;
            case MONTREAL -> MONTREAL_CODE;
            case VANCOUVER, BC -> VANCOUVER_CODE;
            case CALGARY -> CALGARY_CODE;
            case QUEBEC_CITY -> QUEBEC_CITY_CODE;
            default -> DEFAULT_CODE;
        };
    }

    @Override
    public Aircraft getAircraft(Flight flight)
    {
        return aircraftRepo.getAircraftById(flight.getAircraftId());
    }

    private String formatSearchLocation(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        String[] words = value.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) continue;

            result.append(word.substring(0,1).toUpperCase(Locale.ROOT))
                    .append(word.substring(1).toLowerCase(Locale.ROOT))
                    .append(" ");
        }

        return result.toString().trim();
    }

    private List<Flight> sortByPrice(List<Flight> flight) {
        List<Flight> sortedFlight = new ArrayList<>(flight);
        sortedFlight.sort(Comparator.comparingDouble(Flight::getPrice));
        return sortedFlight;
    }


}
