package com.group9.ongo.models;

public class Flight {
    private String airline;
    private String origin;
    private String departTime;
    private String landTime;
    private String destination;
    private int capacity;
    private int flightId;
    private double price;
    private int duration;

    public Flight(int flightId, String airline, String origin, String destination, String departTime, String landTime, int capacity, double price) {
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departTime = departTime;
        this.landTime = landTime;
        this.capacity = capacity;
        this.flightId = flightId;
        this.price = price;
        this.duration = calculateDuration();
    }

    private int calculateDuration() {
        int departMinutes = toMinutes(departTime);
        int landMinutes = toMinutes(landTime);
        //handle overnight flights
        if (landMinutes < departMinutes) {
            landMinutes += 24 * 60;
        }
        //handle 24hr flights
        if ( landMinutes == departMinutes )
        {
            return 24;
        }

        float result = (float) (landMinutes - departMinutes) / 60;

        if (result < 1) {
            return 1;
        }

        return Math.round(result);
    }
    private static int toMinutes(String time) {
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(2, 4));
        return hours * 60 + minutes;
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

    public String getLandTime() { return landTime; }

    public int getCapacity() {
        return capacity;
    }

    public int getFlightId() {
        return flightId;
    }

    public double getPrice() { return price; }
    public int getDuration() { return duration; }


}

