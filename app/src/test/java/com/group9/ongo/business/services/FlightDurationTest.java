package com.group9.ongo.business.services;

import static org.junit.Assert.assertEquals;

import com.group9.ongo.models.Flight;

import org.junit.Test;

public class FlightDurationTest {
    private final String AIR_CANADA = "Air Canada";
    private final String TORONTO = "Toronto";
    private final String VANCOUVER = "Vancouver";
    private final int VALID_CAPACITY = 200;
    private final double VALID_PRICE = 500.34;

    @Test
    public void testCalculateDuration_easy() {
        Flight flight = new Flight(1, AIR_CANADA, TORONTO, VANCOUVER, "1000", "1200", VALID_CAPACITY, VALID_PRICE);
        assertEquals(2, flight.getDuration());
    }

    @Test
    public void testCalculateDuration_easy2() {
        Flight flight = new Flight(1, AIR_CANADA, TORONTO, VANCOUVER, "0930", "0930", VALID_CAPACITY, VALID_PRICE);
        assertEquals(24, flight.getDuration());
    }

    @Test
    public void testCalculateDuration_easy3() {
        Flight flight = new Flight(1, AIR_CANADA, TORONTO, VANCOUVER, "1200", "1201", VALID_CAPACITY, VALID_PRICE);
        assertEquals(1, flight.getDuration());
    }

    @Test
    public void testCalculateDuration_Overnight() {
        Flight flight = new Flight(1, AIR_CANADA, TORONTO, VANCOUVER, "2230", "0415", VALID_CAPACITY, VALID_PRICE);
        assertEquals(6, flight.getDuration());
    }

    @Test
    public void testCalculateDuration_Overnight2() {
        Flight flight = new Flight(1, AIR_CANADA, TORONTO, VANCOUVER, "0700", "0330", VALID_CAPACITY, VALID_PRICE);
        assertEquals(21, flight.getDuration());
    }

    @Test
    public void testCalculateDuration_Overnight3() {
        Flight flight = new Flight(1, AIR_CANADA, TORONTO, VANCOUVER, "2359", "0100", VALID_CAPACITY, VALID_PRICE);
        assertEquals(1, flight.getDuration());
    }

}
