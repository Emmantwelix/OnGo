package com.group9.ongo.integration;

import static com.group9.ongo.business.constants.ErrorMessageConstants.AIRCRAFT_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_DELETE_ERROR;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_DTIME_NULL;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_DESTINATION;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_ORIGIN;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_TIME_SEQUENCE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_LTIME_NULL;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MAX_PRICE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MIN_PRICE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_SAME_ORIGIN_DESTINATION;
import static com.group9.ongo.business.constants.ErrorMessageConstants.NO_FLIGHTS_AVAILABLE;
import static com.group9.ongo.business.constants.FlightConstants.A320_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.A380_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.AIR_TRANSAT;
import static com.group9.ongo.business.constants.FlightConstants.B737_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_DATE;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_FLIGHT_NUM;
import static com.group9.ongo.business.constants.FlightConstants.MONTREAL;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.WESTJET;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.group9.ongo.business.services.Implementations.FlightServiceImpl;
import com.group9.ongo.business.services.Implementations.SeatServiceImpl;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.AircraftRepository;
import com.group9.ongo.persistence.FlightRepository;
import com.group9.ongo.persistence.PassengerRepository;
import com.group9.ongo.persistence.SeatRepository;
import com.group9.ongo.persistence.real.AppDbHelper;
import com.group9.ongo.persistence.real.SqlAircraftRepository;
import com.group9.ongo.persistence.real.SqlFlightRepository;
import com.group9.ongo.persistence.real.SqlPassengerRepository;
import com.group9.ongo.persistence.real.SqlSeatRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FlightServiceIntegrationTest {

    private final String INVALID_ORIGIN = "wrong origin";
    private final String INVALID_DESTINATION = "wrong destination";

    private final double VALID_PRICE = 500.34;
    private final double WEIRD_VALID_PRICE = 0.01;
    private final double INVALID_PRICE = 5001.01;
    private final double INVALID_PRICE2 = 0;
    private final LocalTime VALID_TIME = LocalTime.of(4, 56);
    private final LocalTime VALID_TIME2 = LocalTime.of(23, 59);

    private FlightRepository flightRepository;
    private AircraftRepository aircraftRepository;
    private SeatRepository seatRepository;
    private PassengerRepository passengerRepository;

    private FlightService service;
    private SeatService seatService;

    private int a320Id;
    private int a380Id;
    private int b737Id;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(AppDbHelper.DB_NAME);

        AppDbHelper dbHelper = new AppDbHelper(context, false);

        flightRepository = new SqlFlightRepository(dbHelper);
        aircraftRepository = new SqlAircraftRepository(dbHelper);
        seatRepository = new SqlSeatRepository(dbHelper);
        passengerRepository = new SqlPassengerRepository(dbHelper);

        seatService = new SeatServiceImpl(seatRepository);

        Generator fixedGenerator = new Generator() {
            @Override
            public String generateFlightNum() {
                return DEFAULT_FLIGHT_NUM;
            }

            @Override
            public LocalDate generateDate() {
                return DEFAULT_DATE;
            }
        };

        service = new FlightServiceImpl(
                flightRepository,
                fixedGenerator,
                seatService,
                aircraftRepository
        );

        a320Id = aircraftRepository.addAircraft(A320_DETAILS).getAircraftId();
        a380Id = aircraftRepository.addAircraft(A380_DETAILS).getAircraftId();
        b737Id = aircraftRepository.addAircraft(B737_DETAILS).getAircraftId();
    }

    @Test
    public void getAllFlights_initaillyEmpty() {
        List<Flight> flights = service.getAllFlights();

        assertNotNull(flights);
        assertEquals(0, flights.size());
    }

    @Test
    public void testSearchFlights_returnsFlights() throws ValidationException {
        service.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE);

        List<Flight> flights = service.searchFlights(WINNIPEG, TORONTO);

        assertEquals(1, flights.size());
        assertEquals(WINNIPEG, flights.get(0).getOrigin());
        assertEquals(TORONTO, flights.get(0).getDestination());
    }

    @Test
    public void testCreateFlightWithInvalidAircraft_throwsException() throws ValidationException
    {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, -1, VALID_PRICE)
        );

        assertEquals(AIRCRAFT_NOT_FOUND, exception.getMessage());
    }


    @Test
    public void testSearchFlights_whenInvalidOrigin_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.searchFlights("wakanda", WINNIPEG)
        );

        assertEquals("Invalid origin", exception.getMessage());
    }

    @Test
    public void testSearchFlights_whenInvalidDestination_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.searchFlights(WINNIPEG, "city of atlantis")
        );

        assertEquals("Invalid destination", exception.getMessage());
    }

    @Test
    public void testSearchFlights_whenSameLocation_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.searchFlights(WINNIPEG, WINNIPEG)
        );

        assertEquals(FLIGHT_SAME_ORIGIN_DESTINATION, exception.getMessage());
    }

    @Test
    public void testSearchFlights_withNonExistingFlight_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.searchFlights(WINNIPEG, TORONTO)
        );

        assertEquals(NO_FLIGHTS_AVAILABLE, exception.getMessage());
    }

    @Test
    public void addItem_addsValidItem() throws ValidationException {
        int flightID = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE
        );

        Flight flight = service.getFlightById(flightID);

        assertEquals(flightID, flight.getFlightId());
        assertEquals(AIR_CANADA, flight.getAirline());
        assertEquals(WINNIPEG, flight.getOrigin());
        assertEquals(TORONTO, flight.getDestination());
        assertEquals(VALID_TIME, flight.getDepartTime());
        assertEquals(VALID_TIME2, flight.getLandTime());
        assertEquals(VALID_PRICE, flight.getPrice(), 0.01);
    }

    @Test
    public void addItem_whenInvalidOrigin_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(
                        WESTJET, INVALID_ORIGIN, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE
                )
        );

        assertEquals(FLIGHT_INVALID_ORIGIN, exception.getMessage());
    }

    @Test
    public void addItem_whenInvalidDestination_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(
                        WESTJET, WINNIPEG, INVALID_DESTINATION, VALID_TIME2, VALID_TIME, a320Id, VALID_PRICE
                )
        );

        assertEquals(FLIGHT_INVALID_DESTINATION, exception.getMessage());
    }

    @Test
    public void addItem_whenSameLocation_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(
                        WESTJET, TORONTO, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE
                )
        );

        assertEquals(FLIGHT_SAME_ORIGIN_DESTINATION, exception.getMessage());
    }

    @Test
    public void addItem_whenDepartureTimeNull_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(
                        AIR_TRANSAT, MONTREAL, TORONTO, null, VALID_TIME2, a320Id, VALID_PRICE
                )
        );

        assertEquals(FLIGHT_DTIME_NULL, exception.getMessage());
    }

    @Test
    public void addItem_whenLandingTimeNull_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(
                        AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, null, a320Id, VALID_PRICE
                )
        );

        assertEquals(FLIGHT_LTIME_NULL, exception.getMessage());
    }

    @Test
    public void addItem_whenLandingBeforeDeparture_throwsException() {
        LocalTime depart = LocalTime.of(18, 0);
        LocalTime land = LocalTime.of(18, 0);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(
                        AIR_TRANSAT, MONTREAL, TORONTO, depart, land, a320Id, VALID_PRICE
                )
        );

        assertEquals(FLIGHT_INVALID_TIME_SEQUENCE, exception.getMessage());
    }

    @Test
    public void deleteItem_deletesItem_returnsTrue() throws ValidationException {
        int flightId = service.createFlight(
                AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE
        );

        boolean success = service.deleteFlight(flightId);

        assertTrue(success);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.getFlightById(flightId)
        );
        assertEquals(FLIGHT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void deleteItem_whenItemDoesNotExist_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.deleteFlight(20)
        );

        assertEquals(FLIGHT_DELETE_ERROR, exception.getMessage());
    }

    @Test
    public void getFlightById_whenItemExists_returnsItem() throws ValidationException {
        int flightId = service.createFlight(
                AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE
        );

        Flight returnedFlight = service.getFlightById(flightId);

        assertEquals(flightId, returnedFlight.getFlightId());
        assertEquals(AIR_TRANSAT, returnedFlight.getAirline());
        assertEquals(MONTREAL, returnedFlight.getOrigin());
        assertEquals(TORONTO, returnedFlight.getDestination());
        assertEquals(VALID_TIME, returnedFlight.getDepartTime());
        assertEquals(VALID_TIME2, returnedFlight.getLandTime());
        assertEquals(VALID_PRICE, returnedFlight.getPrice(), 0.01);
    }

    @Test
    public void getFlightByID_whenItemDoesNotExist_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.getFlightById(20)
        );

        assertEquals(FLIGHT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void getAllFlights_returnsAllFlights() throws ValidationException {
        int id1 = service.createFlight(
                AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE
        );
        int id2 = service.createFlight(
                AIR_CANADA, MONTREAL, TORONTO, LocalTime.of(5, 0), LocalTime.of(6, 0), a320Id, VALID_PRICE
        );
        int id3 = service.createFlight(
                WESTJET, MONTREAL, TORONTO, LocalTime.of(7, 0), LocalTime.of(8, 0), a320Id, VALID_PRICE
        );

        List<Flight> flights = service.getAllFlights();

        assertEquals(3, flights.size());
        assertTrue(containsFlightId(flights, id1));
        assertTrue(containsFlightId(flights, id2));
        assertTrue(containsFlightId(flights, id3));
    }

    @Test
    public void testWeirdValidPrice_returnsFlightId() throws ValidationException {
        int flightId = service.createFlight(
                AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, a320Id, WEIRD_VALID_PRICE
        );
        Flight flight = service.getFlightById(flightId);

        assertTrue(flightId > 0);
        assertEquals(WEIRD_VALID_PRICE, flight.getPrice(), 0.01);
    }

    @Test
    public void testInvalidFlightPrice_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(
                        AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, a320Id, INVALID_PRICE
                )
        );
        assertEquals(FLIGHT_MAX_PRICE, exception.getMessage());
    }

    @Test
    public void testInvalidFlightPrice2_throwsException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(
                        AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, a320Id, INVALID_PRICE2
                )
        );
        assertEquals(FLIGHT_MIN_PRICE, exception.getMessage());
    }

    @Test
    public void getDurationHoursAndMinutes_normalFlight() throws ValidationException {
        int flightId = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO,
                LocalTime.of(10, 0),
                LocalTime.of(12, 10),
                a320Id,
                VALID_PRICE
        );

        Flight flight = service.getFlightById(flightId);

        assertEquals(2, service.getDurationHours(flight));
        assertEquals(10, service.getDurationRemainingMinutes(flight));
    }

    @Test
    public void getDurationHoursAndMinutes_shortFlight() throws ValidationException {
        int flightId = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO,
                LocalTime.of(12, 0),
                LocalTime.of(12, 1),
                a320Id,
                VALID_PRICE
        );

        Flight flight = service.getFlightById(flightId);

        assertEquals(0, service.getDurationHours(flight));
        assertEquals(1, service.getDurationRemainingMinutes(flight));
    }

    @Test
    public void getDurationHoursAndMinutes_exactHours() throws ValidationException {
        int flightId = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO,
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                a320Id,
                VALID_PRICE
        );

        Flight flight = service.getFlightById(flightId);

        assertEquals(2, service.getDurationHours(flight));
        assertEquals(0, service.getDurationRemainingMinutes(flight));
    }

    @Test
    public void aircraft_wifiLogicTest() throws ValidationException {
        int flightIdA380 = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, a380Id, VALID_PRICE
        );
        int flightIdB737 = service.createFlight(
                WESTJET, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, b737Id, VALID_PRICE
        );

        Flight flight1 = service.getFlightById(flightIdA380);
        Flight flight2 = service.getFlightById(flightIdB737);

        assertTrue(service.getAircraft(flight1).hasWifi());
        assertFalse(service.getAircraft(flight2).hasWifi());
    }

    @Test
    public void aircraft_capacityStringTest() {
        assertEquals("150 seats", A320_DETAILS.getCapacityString());
        assertEquals("500 seats", A380_DETAILS.getCapacityString());
    }

    @Test
    public void flightService_getFormattedFlightIdTest() throws ValidationException {
        int flightId = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE
        );

        Flight flight = service.getFlightById(flightId);

        assertEquals("AC " + flightId, service.getFormattedFlightId(flight));
    }

    @Test
    public void testGetAvailableSeats_withUnbookedFlight() throws ValidationException {
        int flightId = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE
        );

        int count = service.getAvailableSeats(flightId);

        assertEquals(A320_DETAILS.getCapacity(), count);
    }

    @Test
    public void testSortFlightsByAvailSeats_returnsSortedList() throws ValidationException {
        int flightId = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, a320Id, VALID_PRICE
        );
        int flightId2 = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, a380Id, VALID_PRICE
        );
        int flightId3 = service.createFlight(
                AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, b737Id, VALID_PRICE
        );


        List<Flight> flights = service.sortFlightsByAvailSeats(service.getAllFlights());

        assertEquals(flightId, flights.get(0).getFlightId());
        assertEquals(flightId3, flights.get(1).getFlightId());
    }


    private boolean containsFlightId(List<Flight> flights, int flightId) {
        for (Flight flight : flights) {
            if (flight.getFlightId() == flightId) {
                return true;
            }
        }
        return false;
    }
}