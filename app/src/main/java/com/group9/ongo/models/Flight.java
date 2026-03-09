package com.group9.ongo.models;

import static com.group9.ongo.business.constants.FlightConstants.TIME_FORMAT_PATTERN;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.group9.ongo.business.constants.FlightConstants.DATE_FORMAT;

import java.time.LocalDate;

public class Flight {
    private final String airline;
    private final String origin;
    private final LocalTime departTime;
    private final LocalTime landTime;
    private final String destination;
    private final Aircraft aircraft;
    private final int flightId;
    private final double price;
    private final String flightNumber;
    private final LocalDate date;
    private int[] seats;

    public Flight(int flightId, String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, Aircraft aircraft, double price, String flightNumber, LocalDate date) {
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departTime = departTime;
        this.landTime = landTime;
        this.aircraft = aircraft;
        this.flightId = flightId;
        this.price = price;
        this.flightNumber = flightNumber;
        this.date = date;
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
        return aircraft.getCapacity();
    }

    public int getFlightId() {
        return flightId;
    }

    public double getPrice() { return price; }
    public LocalDate getDate() { return date; }
    public String getDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return date.format(formatter);
    }

    public String getPlaneType() { return aircraft.getModelName(); }
    public String getFlightNumber() { return flightNumber; }
    public Aircraft getAircraft() { return aircraft; }
}
