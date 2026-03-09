package com.group9.ongo.persistence.fake;

import static com.group9.ongo.business.constants.FlightConstants.AIRBUS_A320;
import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.AIR_TRANSAT;
import static com.group9.ongo.business.constants.FlightConstants.BOEING_737;
import static com.group9.ongo.business.constants.FlightConstants.COMAC_C919;
import static com.group9.ongo.business.constants.FlightConstants.CONVAIR_880;
import static com.group9.ongo.business.constants.FlightConstants.DEFUALT_DATE;
import static com.group9.ongo.business.constants.FlightConstants.DEFUALT_FLIGHT_NUM;
import static com.group9.ongo.business.constants.FlightConstants.LARGE_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.MEDIUM_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.MONTREAL;
import static com.group9.ongo.business.constants.FlightConstants.PORTER_AIRLINES;
import static com.group9.ongo.business.constants.FlightConstants.SMALL_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.VANCOUVER;
import static com.group9.ongo.business.constants.FlightConstants.WESTJET;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;

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
    public int createFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int capacity, double price, String planeType, String flightNumber, LocalDate date) {
        Flight flight = new Flight(nextId, airline, origin, destination, departTime, landTime, capacity, price, planeType, flightNumber, date);
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
        this.createFlight(AIR_CANADA, TORONTO, WINNIPEG, LocalTime.of(10, 0), LocalTime.of(12, 0), LARGE_CAPACITY, 603.49, AIRBUS_A320, DEFUALT_FLIGHT_NUM, DEFUALT_DATE);
        this.createFlight(PORTER_AIRLINES, TORONTO, MONTREAL, LocalTime.of(12, 0), LocalTime.of(14, 0), MEDIUM_CAPACITY, 979.52, BOEING_737, DEFUALT_FLIGHT_NUM,DEFUALT_DATE);
        this.createFlight(AIR_TRANSAT, WINNIPEG, VANCOUVER, LocalTime.of(14, 0), LocalTime.of(16, 0), SMALL_CAPACITY, 200.01, CONVAIR_880, DEFUALT_FLIGHT_NUM, DEFUALT_DATE);
        this.createFlight(WESTJET, MONTREAL, WINNIPEG, LocalTime.of(16, 0), LocalTime.of(18, 0), LARGE_CAPACITY, 417.38, COMAC_C919, DEFUALT_FLIGHT_NUM, DEFUALT_DATE);
    }

}
