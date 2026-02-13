package com.group9.ongo.models;

public class Flight {
    private String airline;
    private String origin;
    private String departTime;
    private String landTime;
    private String destination;
    private int capacity;
    private int flightId;

    public Flight(int flightId, String airline, String origin, String destination, String departTime, String landTime, int capacity) {
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departTime = departTime;
        this.landTime = landTime;
        this.capacity = capacity;
        this.flightId = flightId;
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

    public String getDepartTime() {
        return departTime;
    }

    public String getLandTime() {
        return landTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFlightId() {
        return flightId;
    }

    public String getOrigin() { return origin; }


}
