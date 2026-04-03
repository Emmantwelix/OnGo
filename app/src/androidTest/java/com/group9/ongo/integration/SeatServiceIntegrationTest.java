package com.group9.ongo.integration;

import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_BOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_UNBOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_NOT_FOUND;
import static com.group9.ongo.business.constants.FlightConstants.A380_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_TIME;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_TIME2;
import static com.group9.ongo.business.constants.FlightConstants.LARGE_PRICE;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;
import static com.group9.ongo.business.constants.SeatConstants.UNAVAILABLE_SEAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.group9.ongo.business.services.Implementations.SeatServiceImpl;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Seat;
import com.group9.ongo.models.SeatMapConfig;
import com.group9.ongo.persistence.AircraftRepository;
import com.group9.ongo.persistence.FlightRepository;
import com.group9.ongo.persistence.SeatRepository;
import com.group9.ongo.persistence.real.AppDbHelper;
import com.group9.ongo.persistence.real.SqlAircraftRepository;
import com.group9.ongo.persistence.real.SqlFlightRepository;
import com.group9.ongo.persistence.real.SqlSeatRepository;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SeatServiceIntegrationTest {

    private SeatRepository seatRepository;
    private SeatService seatService;

    private AircraftRepository aircraftRepository;

    private FlightRepository flightRepository;
    private int testFlightId;

    @Before
    public void setUp() {

        Context context = ApplicationProvider.getApplicationContext();

        // fresh db without seed data
        context.deleteDatabase(AppDbHelper.DB_NAME);
        AppDbHelper dbHelper = new AppDbHelper(context, false);

        seatRepository = new SqlSeatRepository(dbHelper);
        seatService = new SeatServiceImpl(seatRepository);
        flightRepository = new SqlFlightRepository(dbHelper);
        aircraftRepository = new SqlAircraftRepository(dbHelper);

        testFlightId = createTestFlight();
    }


    @Test
    public void getAllSeatsByFlightId_returnsSeatList() throws ValidationException {
        seatService.createSeat(testFlightId, 1, "A");
        seatService.createSeat(testFlightId, 1, "B");
        int bookedSeatId = seatService.createSeat(testFlightId, 1, "C");
        seatService.bookSeat(testFlightId, 1, "C");

        List<Seat> allResult = seatService.getAllSeatsByFlightId(testFlightId);
        List<Seat> result= new ArrayList<>();

        for(Seat seat: allResult)
        {
            if(seat.getType() == Seat.Type.SEAT)
            {
                result.add(seat);
            }
        }

        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getLabel());
        assertEquals("B", result.get(1).getLabel());
        assertEquals("C", result.get(2).getLabel());
        assertEquals(bookedSeatId, result.get(2).getSeatId());
        assertTrue(result.get(2).getIsBooked());
    }

    @Test
    public void createSeat_whenSeatDoesNotExist_returnsSeatId() throws ValidationException {
        int result = seatService.createSeat(testFlightId, 1, "A");

        assertTrue(result > 0);

        Seat seat = seatService.findSeat(testFlightId, 1, "A");
        assertNotNull(seat);
        assertEquals(result, seat.getSeatId());
        assertEquals(testFlightId, seat.getFlightId());
        assertEquals(1, seat.getSeatRow());
        assertEquals("A", seat.getLabel());
    }

    @Test
    public void createSeats_createsSeatsForCapacity() throws Exception {
        seatService.createSeats(testFlightId, 500);

        List<Seat> seats = seatService.getAllSeatsByFlightId(testFlightId);

        int seat_count = 0;
        for(Seat seat: seats)
        {
            if(seat.getType() == Seat.Type.SEAT)
            {
                seat_count ++;
            }
        }
        assertEquals(seat_count, seats.size());
    }

    @Test
    public void findSeat_whenSeatExists_returnsSeat() throws ValidationException {
        int seatId = seatService.createSeat(testFlightId, 1, "A");

        Seat result = seatService.findSeat(testFlightId, 1, "A");

        assertEquals(seatId, result.getSeatId());
        assertEquals(testFlightId, result.getFlightId());
        assertEquals(1, result.getSeatRow());
        assertEquals("A", result.getLabel());
    }

    @Test
    public void findSeat_whenSeatMissing_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.findSeat(testFlightId, 100, "Z")
        );

        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void getSeatById_whenSeatExists_returnsSeat() throws ValidationException {
        int seatId = seatService.createSeat(testFlightId, 1, "A");

        Seat result = seatService.getSeatById(testFlightId, seatId);

        assertEquals(seatId, result.getSeatId());
        assertEquals(testFlightId, result.getFlightId());
        assertEquals(1, result.getSeatRow());
        assertEquals("A", result.getLabel());
    }

    @Test
    public void getSeatById_whenSeatMissing_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.getSeatById(testFlightId, 999999)
        );

        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void bookSeat_whenSeatExistsAndAvailable_booksSeatAndReturnsSeatId() throws ValidationException {
        int seatId = seatService.createSeat(testFlightId, 1, "A");

        int result = seatService.bookSeat(testFlightId, 1, "A");

        assertEquals(seatId, result);

        Seat updatedSeat = seatService.getSeatById(testFlightId, seatId);
        assertTrue(updatedSeat.getIsBooked());
    }

    @Test
    public void bookSeat_whenSeatMissing_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.bookSeat(testFlightId, 100, "Z")
        );

        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void bookSeat_whenSeatAlreadyBooked_throwsValidationException() throws ValidationException {
        seatService.createSeat(testFlightId, 1, "A");
        seatService.bookSeat(testFlightId, 1, "A");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.bookSeat(testFlightId, 1, "A")
        );

        assertEquals(SEAT_ALREADY_BOOKED, exception.getMessage());
    }

    @Test
    public void unbookSeat_whenSeatExistsAndBooked_unbooksSeat() throws Exception {
        int seatId = seatService.createSeat(testFlightId, 1, "A");
        seatService.bookSeat(testFlightId, 1, "A");

        seatService.unbookSeat(testFlightId, seatId);

        Seat updatedSeat = seatService.getSeatById(testFlightId, seatId);
        assertEquals(false, updatedSeat.getIsBooked());
    }

    @Test
    public void unbookSeat_whenSeatMissing_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.unbookSeat(testFlightId, 999999)
        );

        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void unbookSeat_whenSeatAlreadyUnbooked_throwsValidationException() throws ValidationException {
        int seatId = seatService.createSeat(testFlightId, 1, "A");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.unbookSeat(testFlightId, seatId)
        );

        assertEquals(SEAT_ALREADY_UNBOOKED, exception.getMessage());
    }

    @Test
    public void getFormattedSeatById_whenSeatExists_returnsFormattedSeat() throws ValidationException {
        int seatId = seatService.createSeat(testFlightId, 1, "A");

        String result = seatService.getFormattedSeatById(testFlightId, seatId);

        assertEquals("1A", result);
    }

    @Test
    public void getFormattedSeatById_whenSeatMissing_returnsUnavailableSeat() {
        String result = seatService.getFormattedSeatById(testFlightId, 999999);

        assertEquals(UNAVAILABLE_SEAT, result);
    }

    @Test
    public void getSeatsForDisplay_marksBookedSeatsInGeneratedGrid() throws Exception {
        int capacity = 8;

        // Create seats using the same mapping logic the service uses
        seatService.createSeats(testFlightId, capacity);

        List<Seat> realSeats = seatService.getAllSeatsByFlightId(testFlightId);

        assertTrue(realSeats.size() > 0);

        
        Seat bookedSeat = realSeats.get(0);
        seatService.bookSeat(testFlightId, bookedSeat.getSeatRow(), bookedSeat.getLabel());

        SeatMapConfig config = seatService.getSeatMapConfiguration(capacity);

        List<Seat> displaySeats = seatService.getSeatsForDisplay(testFlightId, config);

        assertNotNull(displaySeats);
        assertTrue(displaySeats.size() > 0);

        Seat matchingDisplaySeat = null;
        for (Seat seat : displaySeats) {
            if (seat.getType() == Seat.Type.SEAT
                    && seat.getSeatRow() == bookedSeat.getSeatRow()
                    && bookedSeat.getLabel().equals(seat.getLabel())) {
                matchingDisplaySeat = seat;
                break;
            }
        }

        assertNotNull(matchingDisplaySeat);
        assertTrue(matchingDisplaySeat.getIsBooked());
    }


    private int createTestFlight() {
        Aircraft aircraft = aircraftRepository.addAircraft(A380_DETAILS);

        return flightRepository.createFlight(
                AIR_CANADA,
                WINNIPEG,
                TORONTO,
                DEFAULT_TIME,
                DEFAULT_TIME2,
                aircraft.getAircraftId(),
                LARGE_PRICE,
                "TEST100",
                LocalDate.of(2026, 3, 13)
        );
    }


}