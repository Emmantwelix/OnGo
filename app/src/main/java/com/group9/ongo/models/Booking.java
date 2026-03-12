package com.group9.ongo.models;

public class Booking {

    private int userId;
    private int bookingId;

    private int flightId;
    private int seatId;
    private BookingStatus status;

    public Booking(int id, int userId, int flightId, int seatId) {
        this(id, userId, flightId, seatId, BookingStatus.UPCOMING);
    }

    public Booking(int id, int userId, int flightId, int seatId, BookingStatus status) {
        this.bookingId = id;
        this.userId = userId;
        this.flightId = flightId;
        this.seatId = seatId;
        this.status = status;
    }

    public Booking(Booking other) {
        this.bookingId = other.bookingId;
        this.userId = other.userId;
        this.flightId = other.flightId;
        this.seatId = other.seatId;
        this.status = other.status;
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
    public int getSeatId() { return seatId; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public BookingStatus getStatus() { return status; }
    public String getBookingStatus() { return status.name(); }
}
