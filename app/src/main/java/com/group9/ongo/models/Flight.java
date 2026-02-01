package com.group9.ongo.models;

public class Flight {
    private String airline;

    private String destination;

    public Flight(String airline, String destination) {
        this.airline = airline;
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public String getAirline() {
        return airline;
    }
}
