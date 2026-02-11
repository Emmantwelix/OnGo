package com.group9.ongo.business.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    private final String AIR_CANADA = "Air Canada";
    private final String WESTJET = "Westjet";
    private final String AIR_TRANSAT = "Air Transat";
    private final String PORTER_AIRLINES = "Porter Airlines";
    //places
    private final String TORONTO = "Toronto";
    private final String MONTREAL = "Montreal";
    private final String VANCOUVER = "Vancouver";
    private final String WINNIPEG = "Winnipeg";

    //capacity
    private final int LARGE_CAPACITY = 200;
    private final int MEDIUM_CAPACITY = 150;
    private final int SMALL_CAPACITY = 100;
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
    public void addItem_addsValidItem(){
        //arrange + act
        int flightID = service.createFlight(AIR_CANADA, WINNIPEG, TORONTO, "1000", "1200", 200);
        Flight flight = service.getFlightById(flightID);
        //assert
        assertEquals(1, service.getAllFlights().size());
        assertEquals(1, flightID);
        assertEquals(AIR_CANADA, flight.getAirline());
        assertEquals(WINNIPEG, flight.getOrigin());
        assertEquals(TORONTO, flight.getDestination());
        assertEquals("1000", flight.getDepartTime());
        assertEquals("1200", flight.getLandTime());
        assertEquals(200, flight.getCapacity());
    }

    @Test
    public void addItem_whenInvalidOrigin_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(WESTJET, "wrong origin", TORONTO, "1111", "1200", 200)
        );
        assertEquals("Invalid origin", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalidDestination_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(WESTJET, WINNIPEG, "wrong destination", "1111", "1200", 200)
        );
        assertEquals("Invalid destination", exception.getMessage());
    }

    @Test
    public void addItem_whenSameLocation_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(WESTJET, TORONTO, TORONTO, "1111", "1200", 200)
        );
        assertEquals("Origin and destination cannot be the same", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalidCapacity_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, "1111", "1200", 0)
        );
        assertEquals("Capacity must be greater than 0", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalidCapacity2_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, "1111", "1200", 501)
        );
        assertEquals("Capacity must be less than 501", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalid_departTime_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, "11911", "1200", 500)
        );
        assertEquals("Invalid departure time", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalid_departTime2_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, "", "1200", 23)
        );
        assertEquals("Invalid departure time", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalid_landTime_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, "2340", "124333", 300)
        );
        assertEquals("Invalid landing time", exception.getMessage());
    }

    @Test
    public void addItem_whenInvalid_landTime2_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, "2340", "", 10)
        );
        assertEquals("Invalid landing time", exception.getMessage());
    }




    @Test
    public void deleteItem_deletesItem_returnsTrue(){
        //arrange
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, "2340", "0500", 100);
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
    public void getFlightById_whenItemExists_returnsItem() {
        //arrange
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, "2340", "0500", 100);
        //act
        Flight returnedFlight = service.getFlightById(flightId);
        //assert
        assertEquals(flightId, returnedFlight.getFlightId());
        assertEquals(AIR_TRANSAT, returnedFlight.getAirline());
        assertEquals(MONTREAL, returnedFlight.getOrigin());
        assertEquals(TORONTO, returnedFlight.getDestination());
        assertEquals("2340", returnedFlight.getDepartTime());
        assertEquals("0500", returnedFlight.getLandTime());
        assertEquals(100, returnedFlight.getCapacity());
    }

    @Test
    public void getFlightByID_whenItemDoesNotExist_returnsNull() {
        //arrange
        int flightId = service.createFlight(AIR_TRANSAT, MONTREAL, TORONTO, "2340", "0500", 100);
        //act
        Flight returnedFlight = service.getFlightById(flightId + 1);
        //assert
        assertNull(returnedFlight);
    }

    @Test
    public void getAllFlights_returnsAllFlights() {
        //arrange
        int id1 = service.createFlight(AIR_TRANSAT, MONTREAL, VANCOUVER, "0930", "0600", 200);
        int id2 = service.createFlight(AIR_CANADA, MONTREAL, WINNIPEG, "2359", "1000", 100);
        int id3 = service.createFlight(PORTER_AIRLINES, VANCOUVER, WINNIPEG, "2000", "1100", 450);
        //act
        List<Flight> flights = service.getAllFlights();
        //assert
        assertEquals(3, flights.size());
        assertEquals(id1, flights.get(0).getFlightId());
        assertEquals(id2, flights.get(1).getFlightId());
        assertEquals(id3, flights.get(2).getFlightId());
    }
}



