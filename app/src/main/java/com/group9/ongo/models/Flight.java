package com.group9.ongo.models;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Flight {
    private final String airline;
    private final String origin;
    private final LocalTime departTime;
    private final LocalTime landTime;
    private final String destination;
    private final int aircraftId;
    private final int flightId;
    private final double price;
    private final String flightNumber;
    private final LocalDate date;
    private boolean availability;
    private final String DATE_FORMAT = "yyyy-MM-dd";
    private final String TIME_FORMAT_PATTERN = "hh:mm a";
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN);
    private final DateTimeFormatter DATE_FORMATTER =  DateTimeFormatter.ofPattern(DATE_FORMAT);

    public Flight(int flightId, String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, int aircraftId, double price, String flightNumber, LocalDate date) {
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departTime = departTime;
        this.landTime = landTime;
        this.aircraftId = aircraftId;
        this.flightId = flightId;
        this.price = price;
        this.flightNumber = flightNumber;
        this.date = date;
        this.availability = true;
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

    public String getDepartTimeString() {
        return departTime.format(TIME_FORMATTER);
    }

    public String getLandTimeString() { return landTime.format(TIME_FORMATTER); }

    public int getFlightId() {
        return flightId;
    }

    public double getPrice() { return price; }
    public LocalDate getDate() { return date; }
    public String getDateString() {
        return date.format(DATE_FORMATTER);
    }
    public String getFlightNumber() { return flightNumber; }
    public int getAircraftId() { return aircraftId; }
    public boolean getAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }
}
