package com.group9.ongo.business.services.Interfaces;

import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Seat;

import java.util.List;

public interface SeatService {
    List<Seat> getAllSeatsByFlightId(int flight_id);
    int createSeat(int flight_id, int row, String column) throws ValidationException; //return seat id.
    void createSeats(int flightId, int capacity) throws ValidationException;
    Seat getSeatById(int flight_id, int seat_id) throws ValidationException;
    int bookSeat(int flight_id, int seatRow, String seatColumn) throws ValidationException;
    void unbookSeat(int flight_id, int seat_id) throws ValidationException;
    Seat findSeat(int flight_id, int seatRow, String seatColumn) throws ValidationException;
    String getFormattedSeatById(int flight_id, int seatId);
}
