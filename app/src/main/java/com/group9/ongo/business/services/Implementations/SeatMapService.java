package com.group9.ongo.business.services.Implementations;

import static com.group9.ongo.business.constants.SeatConstants.NARROW_BODY;
import static com.group9.ongo.business.constants.SeatConstants.REGIONAL;
import static com.group9.ongo.business.constants.SeatConstants.WIDE_BODY;

import com.group9.ongo.models.Seat;
import com.group9.ongo.models.SeatMapConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatMapService {

    public static SeatMapConfig createFromCapacity(int capacity) {
        String layout;

        if (capacity >= 250) {
            layout = WIDE_BODY;
        } else if (capacity >= 100) {
            layout = NARROW_BODY;
        } else {
            layout = REGIONAL;
        }

        int rows = calculateRows(layout, capacity);

        return new SeatMapConfig(layout, capacity, rows);
    }

    private static int calculateRows(String layout, int capacity) {
        int seatsPerRow = layout.replace("_", "").length();
        return (int) Math.ceil((double) capacity / seatsPerRow);
    }

    public static List<Seat> generateSeats(SeatMapConfig config) {
        List<Seat> seats = new ArrayList<>();
        int physicalSeatCount = 0;
        String layout = config.getLayout();

        for (int r = 1; r <= config.getRows(); r++) {
            for (int i = 0; i < layout.length(); i++) {
                char c = layout.charAt(i);
                if (c == '_') {
                    // Physical aisle
                    seats.add(new Seat(r, "", Seat.Type.AISLE, Seat.Status.OCCUPIED));
                } else {
                    // Check if we still have physical seats left to place
                    if (physicalSeatCount < config.getCapacity()) {
                        seats.add(new Seat(r, String.valueOf(c), Seat.Type.SEAT, Seat.Status.AVAILABLE));
                        physicalSeatCount++;
                    } else {
                        // Empty space in the tail or last row where no seat exists
                        seats.add(new Seat(r, "", Seat.Type.AISLE, Seat.Status.OCCUPIED));
                    }
                }
            }
        }
        return seats;
    }

    public static void applyBookedSeats(List<Seat> gridSeats, List<Seat> realSeats) {
        Set<String> bookedSeats = new HashSet<>();

        // Build fast lookup of booked seats
        for (Seat realSeat : realSeats) {
            if (realSeat.getIsBooked()) {
                String key = realSeat.getRow() + "-" + realSeat.getLabel().toUpperCase();
                bookedSeats.add(key);
            }
        }

        // Mark matching grid seats as occupied
        for (Seat gridSeat : gridSeats) {
            if (gridSeat.getType() == Seat.Type.SEAT) {
                String key = gridSeat.getRow() + "-" + gridSeat.getLabel().toUpperCase();
                if (bookedSeats.contains(key)) {
                    gridSeat.setStatus(Seat.Status.OCCUPIED);
                }
            }
        }
    }
}
