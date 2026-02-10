package com.group9.ongo.models;

public class BookingDetails {

    private final Booking booking;
    private final Flight flight;
    private final Passenger passenger;

    public BookingDetails(Booking booking, Flight flight, Passenger passenger) {
        this.booking = booking;
        this.flight = flight;
        this.passenger = passenger;
    }

    public Booking getBooking() {
        return booking;
    }

    public Flight getFlight() {
        return flight;
    }

    public Passenger getPassenger() {
        return passenger;
    }
}
