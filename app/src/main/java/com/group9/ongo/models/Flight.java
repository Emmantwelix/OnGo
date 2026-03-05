package com.group9.ongo.models;

import static com.group9.ongo.business.constants.FlightConstants.TIME_FORMAT_PATTERN;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Flight {
    private String airline;
    private String origin;
    private LocalTime departTime;
    private LocalTime landTime;
    private String destination;
    private int capacity;
    private int flightId;
    private double price;

    public Flight(int flightId, String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int capacity, double price) {
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departTime = departTime;
        this.landTime = landTime;
        this.capacity = capacity;
        this.flightId = flightId;
        this.price = price;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrigin() {
        return origin;
    }


    public String getAirline() {
        return airline;
    }


    public LocalTime getDepartTime() {
        return departTime;
    }

    public LocalTime getLandTime() { return landTime; }

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN);
    public String getDepartTimeString() {
        return departTime.format(TIME_FORMATTER);
    }

    public String getLandTimeString() { return landTime.format(TIME_FORMATTER); }

    public int getCapacity() {
        return capacity;
    }

    public int getFlightId() {
        return flightId;
    }

    public double getPrice() { return price; }
}

