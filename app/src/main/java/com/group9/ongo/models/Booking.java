package com.group9.ongo.models;

public class Booking {

    private int userId;
    private int bookingId;

    private int flightId;

    public Booking(int id, int userId, int flightId) {
        this.bookingId = id;
        this.userId = userId;
        this.flightId = flightId;
    }

    public Booking(Booking other) {
        this.bookingId = other.bookingId;
        this.userId = other.userId;
        this.flightId = other.flightId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getFlightId() {
        return flightId;
    }

    public int getUserId() {
        return userId;
    }
}
