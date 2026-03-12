package com.group9.ongo.models;

public class Seat {
    public enum Type { SEAT, AISLE }
    public enum Status { AVAILABLE, OCCUPIED, SELECTED }

    private int seat_id;
    private int flight_id;
    private final int row;
    private final String label;
    private final Type type;
    private Status status;

    // Original constructor for compatibility
    public Seat(int seat_id, int flight_id, int seat_row, String seat_column, boolean is_booked) {
        this.seat_id = seat_id;
        this.flight_id = flight_id;
        this.row = seat_row;
        this.label = seat_column;
        this.type = Type.SEAT;
        this.status = is_booked ? Status.OCCUPIED : Status.AVAILABLE;
    }

    // New constructor for the seat selection feature
    public Seat(int row, String label, Type type, Status status) {
        this.row = row;
        this.label = label;
        this.type = type;
        this.status = status;
    }

    public int getSeatId() {
        return seat_id;
    }

    public int getFlightId() {
        return flight_id;
    }

    public int getRow() {
        return row;
    }

    public int getSeatRow() { // Compatibility
        return row;
    }

    public String getLabel() {
        return label;
    }

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean getIsBooked() { // Compatibility
        return status == Status.OCCUPIED;
    }

    public void bookSeat() { // Compatibility
        this.status = Status.OCCUPIED;
    }

    public void unbookSeat() { // Compatibility
        this.status = Status.AVAILABLE;
    }

    @Override
    public String toString() {
        return type == Type.AISLE ? "Aisle" : row + label;
    }
}
