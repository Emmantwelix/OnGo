package com.group9.ongo.models;

public class Booking {

    private int userId;
    private int bookingId;

    private int flightId;
    private int seatId;
    private BookingStatus status;
    private boolean isCancelled;

    public Booking(int id, int userId, int flightId, int seatId) {
        this.bookingId = id;
        this.userId = userId;
        this.flightId = flightId;
        this.seatId = seatId;
        this.status = BookingStatus.UPCOMING;
        this.isCancelled = false;
    }

    public Booking(Booking other) {
        this.bookingId = other.bookingId;
        this.userId = other.userId;
        this.flightId = other.flightId;
        this.isCancelled = other.isCancelled;
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
    public String getBookingStatus() { 
        if (isCancelled) {
            return "CANCELLED";
        }
        return status.name(); 
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
