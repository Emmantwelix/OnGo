package com.group9.ongo.models;

public class Flight {
    private String airline;
    private int departTime;
    private int landTime;
    private String destination;

    public Flight(String airline, String destination, int departTime, int landTime) {
        this.airline = airline;
        this.destination = destination;
        this.departTime = departTime;
        this.landTime = landTime;
    }

    public String getDestination() {
        return destination;
    }

    public String getAirline() {
        return airline;
    }

    public int getDepartTime() { return departTime; }
    public int getLandTime() { return landTime; }
}
