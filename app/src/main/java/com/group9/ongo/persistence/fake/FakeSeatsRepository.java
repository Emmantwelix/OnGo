package com.group9.ongo.persistence.fake;

import com.group9.ongo.business.services.Implementations.SeatMapService;
import com.group9.ongo.models.Seat;
import com.group9.ongo.models.SeatMapConfig;
import com.group9.ongo.persistence.SeatRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeSeatsRepository implements SeatRepository {
    private final List<Seat> seats = new ArrayList<>();
    private int nextId = 1;
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
            if(seat.getFlightId() == flightId && seat.getSeatRow() == seatRow && seat.getLabel().equals(seatColumn))
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
                if (seat.getSeatRow() == row && seat.getLabel().equals(column)) {//found dupe
                    return -1;
                }
            }
        }

        Seat seat = new Seat(nextId, flight_id, row, column, false);
        seats.add(seat);
        nextId++;
        return seat.getSeatId();
    }

    @Override
    public void bookSeat(int seatId)
    {
        for (Seat seat : seats) {
            if (seat.getSeatId() == seatId) {
                seat.bookSeat();
                return;
            }
        }
    }

    @Override
    public void unBookSeat(int seatId) {
        for (Seat seat : seats) {
            if (seat.getSeatId() == seatId) {
                seat.unbookSeat();
                return;
            }
        }
    }

    private void populate_with_sample_data() {
        createSeats(1, 150);
        createSeats(2, 160);
        createSeats(3, 250);
        createSeats(4,500);
    }

    private void createSeats(int flightId, int capacity){
        //use centralized seat generation logic
        SeatMapConfig config = SeatMapService.createFromCapacity(capacity);
        List<Seat> generatedSeats = SeatMapService.generateSeats(config);

        for(Seat seat : generatedSeats) {
            if(seat.getType() == Seat.Type.SEAT) {
                Seat addedSeat = new Seat(nextId, flightId, seat.getRow(), seat.getLabel(),false);
                seats.add(addedSeat);
                nextId ++;
            }
        }
    }

}
