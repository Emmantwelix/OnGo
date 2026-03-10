package com.group9.ongo.persistence.fake;

import static com.group9.ongo.business.constants.FlightConstants.A320_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.A380_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.AIR_TRANSAT;
import static com.group9.ongo.business.constants.FlightConstants.B737_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.B787_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.DEFUALT_DATE;
import static com.group9.ongo.business.constants.FlightConstants.DEFUALT_FLIGHT_NUM;
import static com.group9.ongo.business.constants.FlightConstants.MONTREAL;
import static com.group9.ongo.business.constants.FlightConstants.PORTER_AIRLINES;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.VANCOUVER;
import static com.group9.ongo.business.constants.FlightConstants.WESTJET;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;

import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.FlightRepository;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeFlightRepository implements FlightRepository {
    private int nextId = 1;

    private final List<Flight> flights = new ArrayList<>();

    public FakeFlightRepository() {

    }

    public FakeFlightRepository(boolean populate) {
        if (populate) {
            populate_with_sample_data();
        }
    }

    @Override
    public List<Flight> getAll() {
        return Collections.unmodifiableList(flights);
    }

    @Override
    public Flight getFlightById(int flightId) {
        for (Flight flight : flights) {
            if (flight.getFlightId() == flightId) {
                return flight;
            }
        }
        return null;
    }

    @Override
    public int createFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int aircraftId, double price, String flightNumber, LocalDate date) {
        Flight flight = new Flight(nextId, airline, origin, destination, departTime, landTime, aircraftId, price, flightNumber, date);
        flights.add(flight);
        nextId++;
        return nextId - 1;
    }

    @Override
    public boolean deleteFlight(int flightId) {
        for (Flight flight : flights) {
            if (flight.getFlightId() == flightId) {
                return flights.remove(flight);
            }
        }
        return false;
    }
    private void populate_with_sample_data() {
        this.createFlight(AIR_CANADA, TORONTO, WINNIPEG, LocalTime.of(10, 0), LocalTime.of(12, 0), 1, 603.49, DEFUALT_FLIGHT_NUM, DEFUALT_DATE);
        this.createFlight(PORTER_AIRLINES, TORONTO, MONTREAL, LocalTime.of(12, 0), LocalTime.of(14, 0), 2, 979.52, DEFUALT_FLIGHT_NUM, DEFUALT_DATE);
        this.createFlight(AIR_TRANSAT, WINNIPEG, VANCOUVER, LocalTime.of(14, 0), LocalTime.of(16, 0), 3, 200.01, DEFUALT_FLIGHT_NUM, DEFUALT_DATE);
        this.createFlight(WESTJET, MONTREAL, WINNIPEG, LocalTime.of(16, 0), LocalTime.of(18, 0), 4, 417.38, DEFUALT_FLIGHT_NUM, DEFUALT_DATE);
    }

}
