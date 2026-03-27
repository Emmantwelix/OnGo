package com.group9.ongo.models;

public class BookingDetails {

    private final Booking booking;
    private final Flight flight;
    private final Passenger passenger;
    private final String formattedSeat;

    public BookingDetails(Booking booking, Flight flight, Passenger passenger, String formattedSeat) {
        this.booking = booking;
        this.flight = flight;
        this.passenger = passenger;
        this.formattedSeat = formattedSeat;
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

    public String getFormattedSeat() { return formattedSeat; }
}
