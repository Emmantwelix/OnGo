package com.group9.ongo.business.services.UnitTests;

import static com.group9.ongo.business.constants.SeatConstants.NARROW_BODY;
import static com.group9.ongo.business.constants.SeatConstants.REGIONAL;
import static com.group9.ongo.business.constants.SeatConstants.WIDE_BODY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.group9.ongo.business.services.Implementations.SeatMapService;
import com.group9.ongo.models.Seat;
import com.group9.ongo.models.SeatMapConfig;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SeatMapServiceTest {

    @Test
    public void createFromCapacity_shouldReturnWideBodyFor250AndAbove() {
        SeatMapConfig config = SeatMapService.createFromCapacity(250);

        assertNotNull(config);
        assertEquals(WIDE_BODY, config.getLayout());
        assertEquals(250, config.getCapacity());

        int expectedRows = (int) Math.ceil(250.0 / WIDE_BODY.replace("_", "").length());
        assertEquals(expectedRows, config.getRows());
    }

    @Test
    public void createFromCapacity_shouldReturnNarrowBodyFor100To249() {
        SeatMapConfig config = SeatMapService.createFromCapacity(150);

        assertNotNull(config);
        assertEquals(NARROW_BODY, config.getLayout());
        assertEquals(150, config.getCapacity());

        int expectedRows = (int) Math.ceil(150.0 / NARROW_BODY.replace("_", "").length());
        assertEquals(expectedRows, config.getRows());
    }

    @Test
    public void createFromCapacity_shouldReturnRegionalForBelow100() {
        SeatMapConfig config = SeatMapService.createFromCapacity(75);

        assertNotNull(config);
        assertEquals(REGIONAL, config.getLayout());
        assertEquals(75, config.getCapacity());

        int expectedRows = (int) Math.ceil(75.0 / REGIONAL.replace("_", "").length());
        assertEquals(expectedRows, config.getRows());
    }

    @Test
    public void generateSeats_shouldCreateCorrectNumberOfPhysicalSeats() {
        SeatMapConfig config = new SeatMapConfig(NARROW_BODY, 10, 2);

        List<Seat> seats = SeatMapService.generateSeats(config);

        int physicalSeats = 0;
        for (Seat seat : seats) {
            if (seat.getType() == Seat.Type.SEAT) {
                physicalSeats++;
            }
        }

        assertEquals(10, physicalSeats);
    }

    @Test
    public void generateSeats_shouldCreateAislesFromLayout() {
        SeatMapConfig config = new SeatMapConfig(NARROW_BODY, 6, 1);

        List<Seat> seats = SeatMapService.generateSeats(config);

        int aisleCount = 0;
        for (Seat seat : seats) {
            if (seat.getType() == Seat.Type.AISLE) {
                aisleCount++;
            }
        }

        // NARROW_BODY = "ABC_DEF" has 1 underscore, so 1 aisle placeholder per row
        assertEquals(1, aisleCount);
        assertEquals(NARROW_BODY.length(), seats.size());
    }

    @Test
    public void generateSeats_shouldPadUnusedTailSpotsAsAisles() {
        SeatMapConfig config = new SeatMapConfig(NARROW_BODY, 8, 2);

        List<Seat> seats = SeatMapService.generateSeats(config);

        int seatCount = 0;
        int aisleCount = 0;

        for (Seat seat : seats) {
            if (seat.getType() == Seat.Type.SEAT) {
                seatCount++;
            } else if (seat.getType() == Seat.Type.AISLE) {
                aisleCount++;
            }
        }

        // 2 rows * 7 chars each = 14 total positions for "ABC_DEF"
        assertEquals(14, seats.size());
        assertEquals(8, seatCount);

        // 2 actual aisle positions from layout + 4 padded tail positions = 6 aisles
        assertEquals(6, aisleCount);
    }

    @Test
    public void generateSeats_shouldMarkGeneratedSeatsAsAvailable() {
        SeatMapConfig config = new SeatMapConfig(REGIONAL, 4, 1);

        List<Seat> seats = SeatMapService.generateSeats(config);

        for (Seat seat : seats) {
            if (seat.getType() == Seat.Type.SEAT) {
                assertEquals(Seat.Status.AVAILABLE, seat.getStatus());
            }
        }
    }

    @Test
    public void applyBookedSeats_shouldMarkMatchingGridSeatsAsOccupied() {
        List<Seat> gridSeats = new ArrayList<>();
        gridSeats.add(new Seat(1, "A", Seat.Type.SEAT, Seat.Status.AVAILABLE));
        gridSeats.add(new Seat(1, "B", Seat.Type.SEAT, Seat.Status.AVAILABLE));
        gridSeats.add(new Seat(1, "", Seat.Type.AISLE, Seat.Status.OCCUPIED));
        gridSeats.add(new Seat(1, "C", Seat.Type.SEAT, Seat.Status.AVAILABLE));

        List<Seat> realSeats = new ArrayList<>();
        Seat bookedSeat = new Seat(1, "B", Seat.Type.SEAT, Seat.Status.AVAILABLE);
        bookedSeat.bookSeat();
        realSeats.add(bookedSeat);

        Seat unbookedSeat = new Seat(1, "C", Seat.Type.SEAT, Seat.Status.AVAILABLE);
        unbookedSeat.unbookSeat();
        realSeats.add(unbookedSeat);

        SeatMapService.applyBookedSeats(gridSeats, realSeats);

        assertEquals(Seat.Status.AVAILABLE, gridSeats.get(0).getStatus()); // 1A
        assertEquals(Seat.Status.OCCUPIED, gridSeats.get(1).getStatus());  // 1B
        assertEquals(Seat.Status.AVAILABLE, gridSeats.get(3).getStatus()); // 1C
    }

    @Test
    public void applyBookedSeats_shouldIgnoreCaseWhenMatchingLabels() {
        List<Seat> gridSeats = new ArrayList<>();
        gridSeats.add(new Seat(2, "A", Seat.Type.SEAT, Seat.Status.AVAILABLE));

        List<Seat> realSeats = new ArrayList<>();
        Seat bookedSeat = new Seat(2, "a", Seat.Type.SEAT, Seat.Status.AVAILABLE);
        bookedSeat.bookSeat();
        realSeats.add(bookedSeat);

        SeatMapService.applyBookedSeats(gridSeats, realSeats);

        assertEquals(Seat.Status.OCCUPIED, gridSeats.get(0).getStatus());
    }

    @Test
    public void applyBookedSeats_shouldNotChangeAisleSeats() {
        List<Seat> gridSeats = new ArrayList<>();
        gridSeats.add(new Seat(1, "", Seat.Type.AISLE, Seat.Status.OCCUPIED));

        List<Seat> realSeats = new ArrayList<>();
        Seat bookedSeat = new Seat(1, "", Seat.Type.AISLE, Seat.Status.AVAILABLE);
        bookedSeat.bookSeat();
        realSeats.add(bookedSeat);

        SeatMapService.applyBookedSeats(gridSeats, realSeats);

        assertEquals(Seat.Type.AISLE, gridSeats.get(0).getType());
        assertEquals(Seat.Status.OCCUPIED, gridSeats.get(0).getStatus());
    }

    @Test
    public void applyBookedSeats_shouldLeaveNonMatchingSeatsAvailable() {
        List<Seat> gridSeats = new ArrayList<>();
        gridSeats.add(new Seat(3, "D", Seat.Type.SEAT, Seat.Status.AVAILABLE));

        List<Seat> realSeats = new ArrayList<>();
        Seat bookedSeat = new Seat(3, "E", Seat.Type.SEAT, Seat.Status.AVAILABLE);
        bookedSeat.bookSeat();
        realSeats.add(bookedSeat);

        SeatMapService.applyBookedSeats(gridSeats, realSeats);

        assertEquals(Seat.Status.AVAILABLE, gridSeats.get(0).getStatus());
    }
}