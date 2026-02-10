package com.group9.ongo.models;

public class Flight {
    private String airline;
    private int departTime;
    private int landTime;
    private String destination;
    private int capacity;
    private int flightId;

    public Flight(int flightId, String airline, String destination, int departTime, int landTime, int capacity) {
        this.airline = airline;
        this.destination = destination;
        this.departTime = departTime;
        this.landTime = landTime;
        this.capacity = capacity;
        this.flightId = flightId;
    }

    public String getDestination() {
        return destination;
    }

    public String getAirline() {
        return airline;
    }

    public int getDepartTime() { return departTime; }
    public int getLandTime() { return landTime; }

    public int getCapacity () { return  capacity; }

    public int getFlightId () { return flightId; }
}
