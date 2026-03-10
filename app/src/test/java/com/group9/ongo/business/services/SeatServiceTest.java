//package com.group9.ongo.business.services;
//
//import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_NOT_FOUND;
//import static com.group9.ongo.business.constants.FlightConstants.A320_DETAILS;
//import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
//import static com.group9.ongo.business.constants.FlightConstants.COLUMN_1;
//import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_TIME;
//import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_TIME2;
//import static com.group9.ongo.business.constants.FlightConstants.LARGE_PRICE;
//import static com.group9.ongo.business.constants.FlightConstants.ROW_ONE;
//import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
//import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertThrows;
//import static org.junit.Assert.assertTrue;
//
//import com.group9.ongo.business.services.Implementations.FlightDetailGen;
//import com.group9.ongo.business.services.Implementations.FlightServiceImpl;
//import com.group9.ongo.business.services.Implementations.SeatServiceImplementation;
//import com.group9.ongo.business.services.Interfaces.FlightService;
//import com.group9.ongo.business.services.Interfaces.Generator;
//import com.group9.ongo.business.services.Interfaces.SeatService;
//import com.group9.ongo.business.validation.ValidationException;
//import com.group9.ongo.models.Seat;
//import com.group9.ongo.persistence.SeatRepository;
//import com.group9.ongo.persistence.fake.FakeFlightRepository;
//import com.group9.ongo.persistence.fake.FakeSeatsRepository;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//import java.util.Random;
//
//public class SeatServiceTest {
//    private FlightService flightService;
//    private SeatService seatService;
//    private Generator fdgen;
//    private SeatRepository seatRepository;
//
//    @Before
//    public void setup() {
//        seatRepository = new FakeSeatsRepository();
//        fdgen = new FlightDetailGen(new Random());
//        seatService = new SeatServiceImplementation(seatRepository);
//        flightService = new FlightServiceImpl(new FakeFlightRepository(), fdgen, seatService);
//    }
//
//    @Test
//    public void testCreateSeat_throwsValidationException_whenSeatAlreadyExists() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        ValidationException exception = assertThrows(
//                ValidationException.class,
//                () -> seatService.createSeat(flightId, ROW_ONE, COLUMN_1)
//        );
//
//        assertEquals("Seat already exists for this flight", exception.getMessage());
//    }
//
//    @Test
//    public void testGetSeatById_throwsValidationException_whenSeatNotFound() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//
//        ValidationException exception = assertThrows(
//                ValidationException.class,
//                () -> seatService.getSeatById(flightId, A320_DETAILS.getCapacity() + 1)
//        );
//
//        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
//    }
//
//    @Test
//    public void testGetSeatById_returnsSeat() throws ValidationException {
//        seatService.createSeat(1, ROW_ONE, COLUMN_1);
//        Seat seat = seatService.getSeatById(1, 1);
//        assertEquals(1, seat.getFlightId());
//        assertEquals(ROW_ONE, seat.getSeatRow());
//        assertEquals(COLUMN_1, seat.getSeatColumn());
//    }
//
//
//    @Test
//    public void testGetAllSeatsByFlightId_returnsList() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        List<Seat> seats = seatService.getAllSeatsByFlightId(flightId);
//        assertEquals(A320_DETAILS.getCapacity(), seats.size());
//    }
//
//    @Test
//    public void testBookSeat_throwsValidationException_whenSeatNotFound() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        ValidationException exception = assertThrows(
//                ValidationException.class,
//                () -> seatService.bookSeat(flightId, A320_DETAILS.getCapacity() + 1)
//        );
//
//        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
//    }
//
//    @Test
//    public void testBookSeat_marksSeatAsBooked() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        seatService.bookSeat(flightId, 1);
//        Seat seat = seatService.getSeatById(flightId, 1);
//        assertTrue(seat.getIsBooked());
//    }
//
//    @Test
//    public void testUnbookSeat_throwsValidationException_whenSeatNotFound() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        ValidationException exception = assertThrows(
//                ValidationException.class,
//                () -> seatService.unbookSeat(flightId, A320_DETAILS.getCapacity() + 1)
//        );
//
//        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
//    }
//
//    @Test
//    public void testUnbookSeat_marksSeatAsUnbooked() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        seatService.bookSeat(flightId, 1);
//        seatService.unbookSeat(flightId, 1);
//        Seat seat = seatService.getSeatById(flightId, 1);
//        assertTrue(!seat.getIsBooked());
//    }
//
//    @Test
//    public void testIsSeatBooked_returnsTrue_whenSeatIsBooked() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        seatService.bookSeat(flightId, 1);
//        boolean isBooked = seatService.isSeatBooked(flightId, 1);
//        assertTrue(isBooked);
//    }
//
//    @Test
//    public void testIsSeatBooked_returnsFalse_whenSeatIsNotBooked() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        boolean isBooked = seatService.isSeatBooked(flightId, 1);
//        assertFalse(isBooked);
//    }
//
//    @Test
//    public void testFindSeat_throwsValidationException_whenSeatNotFound() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        ValidationException exception = assertThrows(
//                ValidationException.class,
//                () -> seatService.findSeat(flightId, A320_DETAILS.getCapacity() + 1, "X")
//        );
//
//        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
//    }
//
//    @Test
//    public void testFindSeat_returnsSeat() throws ValidationException {
//        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
//        Seat seat = seatService.findSeat(flightId, ROW_ONE, COLUMN_1);
//        assertEquals(1, seat.getFlightId());
//        assertEquals(ROW_ONE, seat.getSeatRow());
//        assertEquals(COLUMN_1, seat.getSeatColumn());
//    }
//
//}
