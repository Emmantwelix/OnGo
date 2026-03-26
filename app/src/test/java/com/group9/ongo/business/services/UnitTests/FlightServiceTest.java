package com.group9.ongo.business.services.UnitTests;

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
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_1;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_DATE;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_DATE2;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_FLIGHT_NUM;
import static com.group9.ongo.business.constants.FlightConstants.MONTREAL;
import static com.group9.ongo.business.constants.FlightConstants.ROW_ONE;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.TSU;
import static com.group9.ongo.business.constants.FlightConstants.WESTJET;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import com.group9.ongo.business.services.Implementations.FlightServiceImpl;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.AircraftRepository;
import com.group9.ongo.persistence.FlightRepository;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class FlightServiceTest {
    //places
    private final String INVALID_ORIGIN = "wrong origin";
    private final String INVALID_DESTINATION = "wrong destination";

    //aircraft
    private final int VALID_AIRCRAFT_ID = 1;
    //price
    private final double VALID_PRICE = 500.34;
    private final double WEIRD_VALID_PRICE = 0.01;
    private final double INVALID_PRICE = 5001.01;
    private final double INVALID_PRICE2 = 0;
    private final LocalTime VALID_TIME = LocalTime.of(4,56);
    private final LocalTime VALID_TIME2 = LocalTime.of(23,59);

    @Mock
    private FlightRepository repo;
    private FlightService service;
    @Mock
    private Generator generator;
    @Mock
    private SeatService seatService;

    @Mock
    private AircraftRepository aircraftRepo;


    @Before
    public void setup(){
        service = new FlightServiceImpl(repo, generator, seatService, aircraftRepo);
    }

    @Test
    public void getAllFlights_initaillyEmpty(){

        List<Flight> flights = service.getAllFlights();
        when(repo.getAll()).thenReturn(List.of());

        //assert
        assertNotNull(flights);
        assertEquals(0, service.getAllFlights().size());
    }

    @Test
    public void testSearchFlights_returnsFlights() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.searchFlights(WINNIPEG, TORONTO)).thenReturn(List.of(sampleFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE)));
        //arrange
        service.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE);
        //act
        List<Flight> flights = service.searchFlights(WINNIPEG, TORONTO);
        //assert
        assertEquals(1, flights.size());
        assertEquals(WINNIPEG, flights.get(0).getOrigin());
        assertEquals(TORONTO, flights.get(0).getDestination());
    }

    @Test
    public void testSearchFlights_whenInvalidOrigin_throwsException() throws ValidationException{

        //arrange
        // act
        //assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.searchFlights("wakanda", WINNIPEG)
        );
        assertEquals("Invalid origin", exception.getMessage());
    }

    @Test
    public void testSearchFlights_whenInvalidDestination_throwsException() throws ValidationException{
        //arrange
        // act
        //assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.searchFlights(WINNIPEG, "city of atlantis")
        );
        assertEquals("Invalid destination", exception.getMessage());
    }

    @Test
    public void testSearchFlights_whenSameLocation_throwsException() throws ValidationException {
        //assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.searchFlights(WINNIPEG, WINNIPEG)
        );
        assertEquals(FLIGHT_SAME_ORIGIN_DESTINATION, exception.getMessage());
    }

    @Test
    public void testSearchFlights_withNonExistingFlight_throwsException() throws ValidationException {
        //assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.searchFlights(WINNIPEG, TORONTO)
        );
        assertEquals(NO_FLIGHTS_AVAILABLE, exception.getMessage());
    }

        @Test
    public void addItem_addsValidItem() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        //arrange + act
        int flightID = service.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE);
        when(repo.getFlightById(flightID)).thenReturn(sampleFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE));
        Flight flight = service.getFlightById(flightID);
        //assert
        assertEquals(1, flight.getFlightId());
        assertEquals(AIR_CANADA, flight.getAirline());
        assertEquals(WINNIPEG, flight.getOrigin());
        assertEquals(TORONTO, flight.getDestination());
        assertEquals(VALID_TIME, flight.getDepartTime());
        assertEquals(VALID_TIME2, flight.getLandTime());
        assertEquals(VALID_PRICE, flight.getPrice(), 0.01);
    }

    @Test
    public void addItem_whenInvalidOrigin_throwsException() {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(WESTJET, INVALID_ORIGIN, TORONTO, VALID_TIME, VALID_TIME, VALID_AIRCRAFT_ID, VALID_PRICE)
        );
        assertEquals(FLIGHT_INVALID_ORIGIN, exception.getMessage());
    }

    @Test
    public void addItem_whenInvalidDestination_throwsException() {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(WESTJET, WINNIPEG, INVALID_DESTINATION, VALID_TIME2, VALID_TIME, VALID_AIRCRAFT_ID, VALID_PRICE)
        );
        assertEquals(FLIGHT_INVALID_DESTINATION, exception.getMessage());
    }

    @Test
    public void addItem_whenSameLocation_throwsException() {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(WESTJET, TORONTO, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE)
        );
        assertEquals(FLIGHT_SAME_ORIGIN_DESTINATION, exception.getMessage());
    }

    @Test
    public void addItem_whenDepartureTimeNull_throwsException() {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, null, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE)
        );

        assertEquals(FLIGHT_DTIME_NULL, exception.getMessage());
    }

    @Test
    public void addItem_whenLandingTimeNull_throwsException() {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, null, VALID_AIRCRAFT_ID, VALID_PRICE)
        );

        assertEquals(FLIGHT_LTIME_NULL, exception.getMessage());
    }

    @Test
    public void addItem_whenLandingBeforeDeparture_throwsException() {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));

        //arrange
        LocalTime depart = LocalTime.of(18, 0);
        LocalTime land = LocalTime.of(18, 0);

        //act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, depart, land, VALID_AIRCRAFT_ID, VALID_PRICE)
        );

        assertEquals(FLIGHT_INVALID_TIME_SEQUENCE, exception.getMessage());
    }

    @Test
    public void deleteItem_deletesItem_returnsTrue() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.deleteFlight(1)).thenReturn(true);
        //arrange
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE);
        //act
        boolean success = service.deleteFlight(flightId);
        //assert
        assertTrue(success);
    }

    @Test
    public void deleteItem_whenItemDoesNotExist_throwsException(){
        when(repo.deleteFlight(20)).thenReturn(false);

        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.deleteFlight(20)
        );
        assertEquals(FLIGHT_DELETE_ERROR, exception.getMessage());
    }

    @Test
    public void getFlightById_whenItemExists_returnsItem() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE));
        //arrange
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE);
        //act
        Flight returnedFlight = service.getFlightById(flightId);
        //assert
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
        when(repo.getFlightById(20)).thenReturn(null);
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.getFlightById(20)
        );
        assertEquals(FLIGHT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void getAllFlights_returnsAllFlights() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        //arrange
        when(repo.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        int id1 = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE);

        when(repo.createFlight(AIR_CANADA, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        int id2 = service.createFlight(AIR_CANADA, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE);

        when(repo.createFlight(WESTJET, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        int id3 = service.createFlight(WESTJET, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE);

        //act
        when(repo.getAll()).thenReturn(List.of(sampleFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE),
                sampleFlight(AIR_CANADA, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE),
                sampleFlight(WESTJET, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE)));
        List<Flight> flights = service.getAllFlights();
        //assert
        assertEquals(3, flights.size());
        assertEquals(id1, flights.get(0).getFlightId());
        assertEquals(id2, flights.get(1).getFlightId());
        assertEquals(id3, flights.get(2).getFlightId());
    }

    @Test
    public void testWeirdValidPrice_returnsFlightId() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, WEIRD_VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, WEIRD_VALID_PRICE));

        //arrange + act
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, WEIRD_VALID_PRICE);
        Flight flight = service.getFlightById(flightId);
        //assert
        assertEquals(1, flightId);
        assertEquals(flight.getPrice(), WEIRD_VALID_PRICE, 0.01);
    }

    @Test
    public void testInvalidFlightPrice_throwsException() {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, INVALID_PRICE)
        );
        assertEquals(FLIGHT_MAX_PRICE, exception.getMessage());
    }

    @Test
    public void testInvalidFlightPrice2_throwsException() {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, INVALID_PRICE2)
        );
        assertEquals(FLIGHT_MIN_PRICE, exception.getMessage());
    }

    @Test
    public void getDurationHoursAndMinutes_normalFlight() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 10), VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 10), VALID_AIRCRAFT_ID, VALID_PRICE));
        // arrange
        int flightId = service.createFlight(AIR_CANADA, WINNIPEG, TORONTO,
                LocalTime.of(10, 0),
                LocalTime.of(12, 10),
                VALID_AIRCRAFT_ID,
                VALID_PRICE);

        Flight flight = service.getFlightById(flightId);

        // act
        int hours = service.getDurationHours(flight);
        int minutes = service.getDurationRemainingMinutes(flight);

        // assert
        assertEquals(2, hours);
        assertEquals(10, minutes);
    }

    @Test
    public void getDurationHoursAndMinutes_shortFlight() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(12, 0), LocalTime.of(12, 1), VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(12, 0), LocalTime.of(12, 1), VALID_AIRCRAFT_ID, VALID_PRICE));
        // arrange
        int flightId = service.createFlight(AIR_CANADA, WINNIPEG, TORONTO,
                LocalTime.of(12, 0),
                LocalTime.of(12, 1),
                VALID_AIRCRAFT_ID,
                VALID_PRICE);

        Flight flight = service.getFlightById(flightId);

        // act
        int hours = service.getDurationHours(flight);
        int minutes = service.getDurationRemainingMinutes(flight);

        // assert
        assertEquals(0, hours);
        assertEquals(1, minutes);
    }

    @Test
    public void getDurationHoursAndMinutes_overnightFlight() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, LocalTime.of(22, 30), LocalTime.of(4, 15), VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_TRANSAT, MONTREAL, TORONTO, LocalTime.of(22, 30), LocalTime.of(4, 15), VALID_AIRCRAFT_ID, VALID_PRICE));

        // arrange
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO,
                LocalTime.of(22, 30),
                LocalTime.of(4, 15),
                VALID_AIRCRAFT_ID,
                VALID_PRICE);

        Flight flight = service.getFlightById(flightId);

        // act
        int hours = service.getDurationHours(flight);
        int minutes = service.getDurationRemainingMinutes(flight);

        // assert
        assertEquals(5, hours);
        assertEquals(45, minutes);
    }

    @Test
    public void getDurationHoursAndMinutes_exactHours() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 0), VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 0), VALID_AIRCRAFT_ID, VALID_PRICE));
        // arrange
        int flightId = service.createFlight(AIR_CANADA, WINNIPEG, TORONTO,
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                VALID_AIRCRAFT_ID,
                VALID_PRICE);

        Flight flight = service.getFlightById(flightId);

        // act
        int hours = service.getDurationHours(flight);
        int minutes = service.getDurationRemainingMinutes(flight);

        // assert
        assertEquals(2, hours);
        assertEquals(0, minutes);
    }

    @Test
    public void getOriginCode_locationLongerThanThreeCharacters_returnsFirstThreeUppercase() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 0), VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 0), VALID_AIRCRAFT_ID, VALID_PRICE));
        // arrange
        int flightId = service.createFlight(AIR_CANADA, "Winnipeg", "Toronto",
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                VALID_AIRCRAFT_ID,
                VALID_PRICE);

        Flight flight = service.getFlightById(flightId);

        // act
        String originCode = service.getOriginCode(flight);

        // assert
        assertEquals("WIN", originCode);
    }

    @Test
    public void getDestinationCode_locationLongerThanThreeCharacters_returnsFirstThreeUppercase() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 0), VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_CANADA, WINNIPEG, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 0), VALID_AIRCRAFT_ID, VALID_PRICE));
        // arrange
        int flightId = service.createFlight(AIR_CANADA, "Winnipeg", "Toronto",
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                VALID_AIRCRAFT_ID,
                VALID_PRICE);

        Flight flight = service.getFlightById(flightId);

        // act
        String destinationCode = service.getDestinationCode(flight);

        // assert
        assertEquals("TOR", destinationCode);
    }

    @Test
    public void getOriginCode_locationExactlyThreeCharacters_returnsUppercase() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, TSU, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 0), VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_CANADA, TSU, TORONTO, LocalTime.of(10, 0), LocalTime.of(12, 0), VALID_AIRCRAFT_ID, VALID_PRICE));
        // arrange
        int flightId = service.createFlight(AIR_CANADA, "Tsu", "Toronto",
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                VALID_AIRCRAFT_ID,
                VALID_PRICE);

        Flight flight = service.getFlightById(flightId);

        // act
        String originCode = service.getOriginCode(flight);

        // assert
        assertEquals("TSU", originCode);
    }

    @Test
    public void aircraft_wifiLogicTest() throws ValidationException {
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        // A380 has wifi, B737 does not
        when(aircraftRepo.getAircraftById(1)).thenReturn(A380_DETAILS);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, 1,VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        int flightIdA380 = service.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, 1,VALID_PRICE);

        when(aircraftRepo.getAircraftById(2)).thenReturn(B737_DETAILS);
        when(repo.createFlight(WESTJET, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, 2, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(2);
        int flightIdB737 = service.createFlight(WESTJET, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, 2, VALID_PRICE);

        when(repo.getFlightById(1)).thenReturn(new Flight(1, AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, 1, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE));
        Flight flight1 = service.getFlightById(flightIdA380);
        when(repo.getFlightById(2)).thenReturn(new Flight(2, WESTJET, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, 2, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE));
        Flight flight2 = service.getFlightById(flightIdB737);

        when(aircraftRepo.getAircraftById(1)).thenReturn(A380_DETAILS);
        when(aircraftRepo.getAircraftById(2)).thenReturn(B737_DETAILS);

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
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(repo.getFlightById(1)).thenReturn(sampleFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE));

        int flightId = service.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE);
        Flight flight = service.getFlightById(flightId);

        assertEquals("AC " + flightId, service.getFormattedFlightId(flight));
    }

    @Test
    public void testGetAvailableSeats_withUnbookedFlight() throws ValidationException {
        when(aircraftRepo.getAircraftById(VALID_AIRCRAFT_ID)).thenReturn(sampleAircraft(VALID_AIRCRAFT_ID));
        when(generator.generateFlightNum()).thenReturn(DEFAULT_FLIGHT_NUM);
        when(generator.generateDate()).thenReturn(DEFAULT_DATE);
        when(repo.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)).thenReturn(1);
        when(seatService.getAllSeatsByFlightId(1)).thenReturn(createSeat(A320_DETAILS.getCapacity()));

        int flightId = service.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE);
        int count = service.getAvailableSeats(flightId);
        assertEquals(A320_DETAILS.getCapacity(), count);
    }

    @Test
    public void testSortFlightsByDuration_returnsSortedList() throws ValidationException {

        List<Flight> flights = service.sortFlightsByDuration(List.of(new Flight(1, AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME2, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE),
                new Flight(2, AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME2, VALID_TIME, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE2)));
        assertEquals(1, flights.get(0).getFlightId());
        assertEquals(2, flights.get(1).getFlightId());
    }

    @Test
    public void testSortFlightsByDateTime_returnsSortedList() throws ValidationException{
        List<Flight> flights = service.sortFlightsByDateTime(List.of(new Flight(1, AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME2, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE),
                new Flight(2, AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME2, VALID_TIME, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE2)));
        assertEquals(1, flights.get(0).getFlightId());
        assertEquals(2, flights.get(1).getFlightId());
    }

    @Test
    public void testSortFlightsByDateTime_withSameDateDifferentTime_returnsSortedList() throws ValidationException {
        List<Flight> flights = service.sortFlightsByDateTime(List.of(new Flight(1, AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE),
                new Flight(2, AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME2, VALID_TIME, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE)));
        assertEquals(1, flights.get(0).getFlightId());
        assertEquals(2, flights.get(1).getFlightId());
    }

    @Test
    public void testSortFlightsByAvailSeats_returnsSortedList() throws ValidationException {
        when(seatService.getAllSeatsByFlightId(1)).thenReturn(List.of(new Seat(1, 1, 1, COLUMN_1, false)));
        when(seatService.getAllSeatsByFlightId(2)).thenReturn(List.of(new Seat(2, 2,2, COLUMN_1, false), new Seat(3, 2,2, COLUMN_1, false)));
        List<Flight> flights = service.sortFlightsByAvailSeats(List.of(new Flight(1, AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME2, VALID_TIME2, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE),
                new Flight(2, AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME2, VALID_TIME, VALID_AIRCRAFT_ID, VALID_PRICE, DEFAULT_FLIGHT_NUM, DEFAULT_DATE2)));
        assertEquals(1, flights.get(0).getFlightId());
        assertEquals(2, flights.get(1).getFlightId());
    }



    private PassengerInput samplePassengerInput(String tag) {
        return new PassengerInput(
                "First" + tag,
                "Last" + tag,
                "2000-01-01",
                "P" + tag + "12345"
        );
    }

    private Aircraft sampleAircraft(int id)
    {
        return new Aircraft(id, "A320", 150, true);
    }

    private Flight sampleFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int aircraftId, double price)
    {
        return new Flight(1, airline, origin, destination, departTime, landTime, aircraftId, price, "12345", LocalDate.now());
    }

    private List<Seat> createSeat(int amount)
    {
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            seats.add(new Seat(i, 1, 1, "A", false));
        }
        return seats;
    }
}
