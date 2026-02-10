package com.group9.ongo.business.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.FlightRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class FlightServiceTest {
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
    public void addItem_addsItem(){
        //arrange
        Flight flight = new Flight(1, "Air Canada", "Toronto", 1000, 1200, 200);
        //act
        boolean success = service.addFlight(flight);
        Flight addedFlight = service.getFlightById(1);
        //assert
        assertTrue(success);
        assertEquals(1, service.getAllFlights().size());
        assertEquals(flight, addedFlight);
    }

    @Test
    public void addSameItemID_returnsFalse(){
        //arrange
        Flight flight = new Flight(1, "Air Canada", "Toronto", 1000, 1200, 200);
        Flight flight2 = new Flight(1, "Air Canada", "Winnipeg", 1000, 1200, 200);
        //act
        service.addFlight(flight);
        boolean fail = service.addFlight(flight2);
        //assert
        assertFalse(fail);
        assertEquals(1, service.getAllFlights().size());
    }


    @Test
    public void deleteItem_deletesItem(){
        //arrange
        Flight flight = new Flight(1, "Air Canada", "Toronto", 1000, 1200, 200);
        service.addFlight(flight);
        //act
        boolean success = service.deleteFlight(1);
        //assert
        assertEquals(true, success);
        assertEquals(0, service.getAllFlights().size());
    }

    @Test
    public void deleteItem_whenItemDoesNotExist_returnsFalse(){
        //arrange
        //act
        boolean success = service.deleteFlight(1);
        //assert
        assertFalse(success);
        assertEquals(0, service.getAllFlights().size());
    }

    @Test
    public void getFlightById_whenItemExists_returnsItem() {
        //arrange
        Flight flight = new Flight(1, "Air Canada", "Toronto", 1000, 1200, 2);
        service.addFlight(flight);
        //act
        Flight returnedFlight = service.getFlightById(1);
        //assert
        assertEquals(flight, returnedFlight);
    }

    @Test
    public void getFlightByID_whenItemDoesNotExist_returnsNull() {
        //arrange
        Flight flight = new Flight(1, "Air Canada", "Toronto", 1000, 1200, 2);
        service.addFlight(flight);
        //act
        Flight returnedFlight = service.getFlightById(1);
        //assert
        assertNull(returnedFlight);
    }

    @Test
    public void getAllFlights_returnsAllFlights() {
        //arrange
        Flight flight1 = new Flight(1, "Air Canada", "Toronto", 1000, 1200, 2);
        Flight flight2 = new Flight(2, "Air Canada", "Toronto", 1000, 1200, 20);
        Flight flight3 = new Flight(3, "Air Canada", "Toronto", 1000, 1200, 48);
        //act
        service.addFlight(flight1);
        service.addFlight(flight2);
        service.addFlight(flight3);
        List<Flight> flights = service.getAllFlights();
        //assert
        assertEquals(3, flights.size());
        assertTrue(flights.contains(flight1));
        assertTrue(flights.contains(flight2));
        assertTrue(flights.contains(flight3));
    }
}



