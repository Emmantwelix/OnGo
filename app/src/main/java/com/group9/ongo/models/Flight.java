package com.group9.ongo.models;

import static com.group9.ongo.business.constants.FlightConstants.TIME_FORMAT_PATTERN;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.group9.ongo.business.constants.FlightConstants.DATE_FORMAT;

import java.time.LocalDate;
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
    private int[] seats;
    private String planeType;
    private String flightNumber;
    private LocalDate date;

    public Flight(int flightId, String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int capacity, double price, String planeType, String flightNumber, LocalDate date) {
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departTime = departTime;
        this.landTime = landTime;
        this.capacity = capacity;
        this.flightId = flightId;
        this.price = price;
        this.planeType = planeType;
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
        return capacity;
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

    public String getPlaneType() { return planeType; }
    public String getFlightNumber() { return flightNumber; }
}
