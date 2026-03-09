package com.group9.ongo.models;

public class Seat {
    int seat_id;
    int flight_id;
    int seat_row;
    String seat_column;
    boolean isBooked;

    public Seat(int seat_id, int flight_id, int seat_row, String seat_column, boolean is_booked) {
        this.seat_id = seat_id;
        this.flight_id = flight_id;
        this.seat_row = seat_row;
        this.seat_column = seat_column;
        this.isBooked = is_booked;
    }

    public int getSeatId() {
        return seat_id;
    }

    public int getFlightId() {
        return flight_id;
    }

    public int getSeatRow() {
        return seat_row;
    }

    public String getSeatColumn() {
        return seat_column;
    }

    public boolean getIsBooked() {
        return isBooked;
    }

    public void bookSeat() {
        isBooked = true;
    }

    public void unbookSeat() {
        isBooked = false;
    }

}
