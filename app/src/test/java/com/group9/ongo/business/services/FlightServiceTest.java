package com.group9.ongo.business.services;

import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.AIR_TRANSAT;
import static com.group9.ongo.business.constants.FlightConstants.LARGE_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.MONTREAL;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.WESTJET;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.FlightRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;


import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class FlightServiceTest {
    //places
    private final String INVALID_ORIGIN = "wrong origin";
    private final String INVALID_DESTINATION = "wrong destination";

    //capacity
    private final int VALID_CAPACITY = LARGE_CAPACITY;
    private final int INVALID_CAPACITY = 0;
    private final int INVALID_CAPACITY2 = 501;
    //price
    private final double VALID_PRICE = 500.34;
    private final double WEIRD_VALID_PRICE = 0.01;
    private final double INVALID_PRICE = 5001.01;
    private final double INVALID_PRICE2 = 0;
    private final String INVALID_TIME = "9999";
    private final String INVALID_TIME2 = "";
    private final String VALID_TIME = "0456";
    private final String VALID_TIME2 = "2359";

    private FlightRepository repo;
    private FlightService service;

    @Before
    public void setup(){
        repo = new FakeFlightRepository();
        service = new FlightServiceImpl(repo);
    }

    @Test
    public void getAllFlights_initaillyEmpty(){
        //arrange + act
        List<Flight> flights = service.getAllFlights();
        //assert
        assertNotNull(flights);
        assertEquals(0, service.getAllFlights().size());
    }

    @Test
    public void addItem_addsValidItem() throws ValidationException {
        //arrange + act
        int flightID = service.createFlight(AIR_CANADA, WINNIPEG, TORONTO, VALID_TIME, VALID_TIME2, VALID_CAPACITY, VALID_PRICE);
        Flight flight = service.getFlightById(flightID);
        //assert
        assertEquals(1, service.getAllFlights().size());
        assertEquals(1, flightID);
        assertEquals(AIR_CANADA, flight.getAirline());
        assertEquals(WINNIPEG, flight.getOrigin());
        assertEquals(TORONTO, flight.getDestination());
        assertEquals(VALID_TIME, flight.getDepartTime());
        assertEquals(VALID_TIME2, flight.getLandTime());
        assertEquals(VALID_CAPACITY, flight.getCapacity());
        assertEquals(VALID_PRICE, flight.getPrice(), 0.01);
    }

    @Test
    public void addItem_whenInvalidOrigin_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(WESTJET, INVALID_ORIGIN, TORONTO, VALID_TIME, VALID_TIME, VALID_CAPACITY, VALID_PRICE)
        );
        assertEquals("Invalid origin", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalidDestination_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(WESTJET, WINNIPEG, INVALID_DESTINATION, VALID_TIME2, VALID_TIME, VALID_CAPACITY, VALID_PRICE)
        );
        assertEquals("Invalid destination", exception.getMessage());
    }

    @Test
    public void addItem_whenSameLocation_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(WESTJET, TORONTO, TORONTO, VALID_TIME, VALID_TIME2, VALID_CAPACITY, VALID_PRICE)
        );
        assertEquals("Origin and destination cannot be the same", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalidCapacity_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
            () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, INVALID_CAPACITY, VALID_PRICE)
        );
        assertEquals("Capacity must be greater than 0", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalidCapacity2_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, INVALID_CAPACITY2, VALID_PRICE)
        );
        assertEquals("Capacity must be less than 501", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalid_departTime_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, INVALID_TIME, VALID_TIME, VALID_CAPACITY, VALID_PRICE)
        );
        assertEquals("Invalid departure time", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalid_departTime2_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, INVALID_TIME2, VALID_TIME, VALID_CAPACITY, VALID_PRICE)
        );
        assertEquals("Invalid departure time", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalid_landTime_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, INVALID_TIME, VALID_CAPACITY, VALID_PRICE)
        );
        assertEquals("Invalid landing time", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalid_landTime2_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, INVALID_TIME2, VALID_CAPACITY, VALID_PRICE)
        );
        assertEquals("Invalid landing time", exception.getMessage());
    }




    @Test
    public void deleteItem_deletesItem_returnsTrue() throws ValidationException {
        //arrange
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME, VALID_CAPACITY, VALID_PRICE);
        //act
        boolean success = service.deleteFlight(flightId);
        //assert
        assertTrue(success);
        assertEquals(0, service.getAllFlights().size());
    }

    @Test
    public void deleteItem_whenItemDoesNotExist_throwsException(){
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.deleteFlight(20)
        );
        assertEquals("Flight could not be deleted, since flight does not exist", exception.getMessage());
    }

    @Test
    public void getFlightById_whenItemExists_returnsItem() throws ValidationException {
        //arrange
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_CAPACITY, VALID_PRICE);
        //act
        Flight returnedFlight = service.getFlightById(flightId);
        //assert
        assertEquals(flightId, returnedFlight.getFlightId());
        assertEquals(AIR_TRANSAT, returnedFlight.getAirline());
        assertEquals(MONTREAL, returnedFlight.getOrigin());
        assertEquals(TORONTO, returnedFlight.getDestination());
        assertEquals(VALID_TIME, returnedFlight.getDepartTime());
        assertEquals(VALID_TIME2, returnedFlight.getLandTime());
        assertEquals(VALID_CAPACITY, returnedFlight.getCapacity());
        assertEquals(VALID_PRICE, returnedFlight.getPrice(), 0.01);
    }

    @Test
    public void getFlightByID_whenItemDoesNotExist_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.getFlightById(20)
        );
        assertEquals("Flight not found", exception.getMessage());
    }

    @Test
    public void getAllFlights_returnsAllFlights() throws ValidationException {
        //arrange
        int id1 = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_CAPACITY, VALID_PRICE);
        int id2 = service.createFlight(AIR_CANADA, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_CAPACITY, VALID_PRICE);
        int id3 = service.createFlight(WESTJET, MONTREAL, TORONTO, VALID_TIME2, VALID_TIME2, VALID_CAPACITY, VALID_PRICE);
        //act
        List<Flight> flights = service.getAllFlights();
        //assert
        assertEquals(3, flights.size());
        assertEquals(id1, flights.get(0).getFlightId());
        assertEquals(id2, flights.get(1).getFlightId());
        assertEquals(id3, flights.get(2).getFlightId());
    }

    @Test
    public void testWeirdValidPrice_returnsFlightId() throws ValidationException {
        //arrange + act
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_CAPACITY, WEIRD_VALID_PRICE);
        Flight flight = service.getFlightById(flightId);
        //assert
        assertEquals(1, flightId);
        assertEquals(flight.getPrice(), WEIRD_VALID_PRICE, 0.01);
    }

    @Test
    public void testInvalidFlightPrice_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_CAPACITY, INVALID_PRICE)
        );
        assertEquals("Price must be less than 5001", exception.getMessage());
    }

    @Test
    public void testInvalidFlightPrice2_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, VALID_TIME, VALID_TIME2, VALID_CAPACITY, INVALID_PRICE2)
        );
        assertEquals("Price must be greater than 0", exception.getMessage());
    }
}



