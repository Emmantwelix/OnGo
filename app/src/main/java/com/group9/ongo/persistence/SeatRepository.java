package com.group9.ongo.persistence;

import com.group9.ongo.models.Seat;

import java.util.List;

public interface SeatRepository {
    List<Seat> getSeatsByFlightId(int flight_id);
    Seat getSeatById(int flight_id, int seat_id);
    int createSeat(int flight_id, int row, String column); //return seat id.
}
