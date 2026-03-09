package com.group9.ongo.persistence.fake;

import static com.group9.ongo.business.constants.FlightConstants.AIRBUS_A320;
import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.AIR_TRANSAT;
import static com.group9.ongo.business.constants.FlightConstants.BOEING_737;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_1;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_2;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_3;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_4;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_5;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_6;
import static com.group9.ongo.business.constants.FlightConstants.COMAC_C919;
import static com.group9.ongo.business.constants.FlightConstants.CONVAIR_880;
import static com.group9.ongo.business.constants.FlightConstants.DEFUALT_DATE;
import static com.group9.ongo.business.constants.FlightConstants.DEFUALT_FLIGHT_NUM;
import static com.group9.ongo.business.constants.FlightConstants.LARGE_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.MAX_COLUMNS;
import static com.group9.ongo.business.constants.FlightConstants.MAX_ROWS;
import static com.group9.ongo.business.constants.FlightConstants.MEDIUM_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.MONTREAL;
import static com.group9.ongo.business.constants.FlightConstants.PORTER_AIRLINES;
import static com.group9.ongo.business.constants.FlightConstants.SMALL_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.VANCOUVER;
import static com.group9.ongo.business.constants.FlightConstants.WESTJET;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;

import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.SeatRepository;

import java.time.LocalTime;
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
        createSeats(1);
        createSeats(2);
        createSeats(3);
        createSeats(4);
    }

    private void createSeats(int flightId){
        String letter = " ";
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if ( j  == 0) {
                    letter = COLUMN_1;
                } else if ( j == 1) {
                    letter = COLUMN_2;
                } else if ( j == 2) {
                    letter = COLUMN_3;
                } else if ( j == 3) {
                    letter = COLUMN_4;
                } else if ( j == 4) {
                    letter = COLUMN_5;
                } else {
                    letter = COLUMN_6;
                }
                Seat seat = new Seat(nextId, flightId, i+1, letter, false);
                seats.add(seat);
                nextId++;
            }
        }
    }

}
