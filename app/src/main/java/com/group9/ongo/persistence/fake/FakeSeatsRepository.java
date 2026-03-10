package com.group9.ongo.persistence.fake;

import static com.group9.ongo.business.constants.FlightConstants.COLUMN_1;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_2;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_3;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_4;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_5;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_6;
import static com.group9.ongo.business.constants.FlightConstants.MAX_COLUMNS;
import static com.group9.ongo.business.constants.FlightConstants.MAX_ROWS;

import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.SeatRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeSeatsRepository implements SeatRepository {
    private final List<Seat> seats = new ArrayList<>();
    private int nextId = 1;

    public FakeSeatsRepository() {}
    public FakeSeatsRepository(boolean populate) {
        if (populate) {
            populate_with_sample_data();
        }
    }

    @Override
    public List<Seat> getSeatsByFlightId(int flight_id) {
        List<Seat> result = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.getFlightId() == flight_id) {
                result.add(seat);
            }
        }
        return result;
    }

    @Override
    public Seat getSeatById(int flight_id, int seat_id) {
        for (Seat seat : seats) {
            if (seat.getFlightId() == flight_id && seat.getSeatId() == seat_id)
                return seat;
        }
        return null;
    }

    @Override
    public Seat findSeat(int flightId, int seatRow, String seatColumn) {
        for (Seat seat : seats)
        {
            if(seat.getFlightId() == flightId && seat.getSeatRow() == seatRow && seat.getSeatColumn().equals(seatColumn))
            {
                return seat;
            }
        }

        return null;
    }

    @Override
    public int createSeat(int flight_id, int row, String column) {
        for (Seat seat : seats) {
            if (seat.getFlightId() == flight_id) {
                if (seat.getSeatRow() == row && seat.getSeatColumn().equals(column)) {//found dupe
                    return -1;
                }
            }
        }

        Seat seat = new Seat(nextId, flight_id, row, column, false);
        seats.add(seat);
        nextId++;
        return seat.getSeatId();
    }

    private void populate_with_sample_data() {
        createSeats(1, 150);
        createSeats(2, 160);
        createSeats(3, 250);
        createSeats(4,500);
        createSeats(5, 1);
    }

    private void createSeats(int flightId, int capacity){
        String letter = " ";
        int amountRows = capacity / MAX_COLUMNS;
        int extraRow = capacity % MAX_COLUMNS;

        for (int i = 0; i < amountRows; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if (j == 0) {
                    letter = COLUMN_1;
                } else if (j == 1) {
                    letter = COLUMN_2;
                } else if (j == 2) {
                    letter = COLUMN_3;
                } else if (j == 3) {
                    letter = COLUMN_4;
                } else if (j == 4) {
                    letter = COLUMN_5;
                } else {
                    letter = COLUMN_6;
                }
                Seat seat = new Seat(nextId, flightId, i+1, letter, false);
                seats.add(seat);
                nextId++;
            }
        }

        //extra row
            for (int j = 0; j < extraRow; j++) {
                if (j == 0) {
                    letter = COLUMN_1;
                } else if (j == 1) {
                    letter = COLUMN_2;
                } else if (j == 2) {
                    letter = COLUMN_3;
                } else if (j == 3) {
                    letter = COLUMN_4;
                } else {
                    letter = COLUMN_5;
                }

                Seat seat = new Seat(nextId, flightId, amountRows+1, letter, false);
                seats.add(seat);
                nextId++;
            }
    }

}
