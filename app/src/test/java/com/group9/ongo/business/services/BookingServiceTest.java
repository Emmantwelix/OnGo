package com.group9.ongo.business.services;

import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_CREATION_FAILED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_PASSENGER_ERROR;
import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_UPDATE_FAILED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_NOT_FOUND;
import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group9.ongo.business.services.Implementations.BookingServiceImpl;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.PassengerService;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.BookingStatus;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.BookingRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    private static final int CURRENT_USER_ID = 1;

    @Mock
    private BookingRepository bookingsRepo;

    @Mock
    private PassengerService passengerService;

    @Mock
    private FlightService flightService;

    @Mock
    private SeatService seatService;

    private BookingService bookingService;

    @Before
    public void setUp() {
        bookingService = new BookingServiceImpl(
                CURRENT_USER_ID,
                bookingsRepo,
                passengerService,
                flightService,
                seatService
        );
    }

    @Test
    public void createBooking_whenValidInput_createsBookingAndPassenger() throws ValidationException, BookingException {
        // Arrange
        int flightId = 10;
        int seatRow = 5;
        String seatColumn = "B";
        int seatId = 77;

        PassengerInput passengerInput = samplePassengerInput();

        Flight flight = sampleFlight(flightId);
        Booking savedBooking = new Booking(100, CURRENT_USER_ID, flightId, seatId);

        when(flightService.getFlightById(flightId)).thenReturn(flight);
        when(seatService.bookSeat(flightId, seatRow, seatColumn)).thenReturn(seatId);
        when(bookingsRepo.addBooking(any(Booking.class))).thenReturn(savedBooking);

        // Act
        Booking result = bookingService.createBooking(flightId, passengerInput, seatRow, seatColumn);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getBookingId());
        assertEquals(CURRENT_USER_ID, result.getUserId());
        assertEquals(flightId, result.getFlightId());
        assertEquals(seatId, result.getSeatId());

        //capture the booking passed into the addBooking method
        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingsRepo).addBooking(captor.capture());

        Booking passedBooking = captor.getValue();
        assertEquals(CURRENT_USER_ID, passedBooking.getUserId());
        assertEquals(flightId, passedBooking.getFlightId());
        assertEquals(seatId, passedBooking.getSeatId());

        //verify these methods were called with this argument
        verify(passengerService).addPassenger(passengerInput, savedBooking.getBookingId());
        verify(flightService).updateFlightAvailability(flightId);
    }

    @Test
    public void createBooking_whenFlightDoesNotExist_throwsValidationException() throws ValidationException, BookingException {
        // Arrange
        int invalidFlightId = 999;
        PassengerInput passengerInput = samplePassengerInput();

        when(flightService.getFlightById(invalidFlightId)).thenReturn(null);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(invalidFlightId, passengerInput, 1, "A")
        );

        // Assert
        assertEquals(FLIGHT_NOT_FOUND, exception.getMessage());

        //verify these methods are never called
        verify(seatService, never()).bookSeat(any(Integer.class), any(Integer.class), any(String.class));
        verify(bookingsRepo, never()).addBooking(any(Booking.class));
        verify(passengerService, never()).addPassenger(any(PassengerInput.class), any(Integer.class));
    }

    @Test
    public void createBooking_whenBookingInsertFails_rollsBackSeatAndThrowsBookingException() throws ValidationException, BookingException {
        // Arrange
        int flightId = 10;
        int seatRow = 2;
        String seatColumn = "C";
        int seatId = 88;

        PassengerInput passengerInput = samplePassengerInput();

        when(flightService.getFlightById(flightId)).thenReturn(sampleFlight(flightId));
        when(seatService.bookSeat(flightId, seatRow, seatColumn)).thenReturn(seatId);
        when(bookingsRepo.addBooking(any(Booking.class))).thenReturn(null);

        // Act
        BookingException exception = assertThrows(
                BookingException.class,
                () -> bookingService.createBooking(flightId, passengerInput, seatRow, seatColumn)
        );

        // Assert
        assertEquals(BOOKING_CREATION_FAILED, exception.getMessage());
        verify(seatService).unbookSeat(flightId, seatId);
        verify(passengerService, never()).addPassenger(any(PassengerInput.class), any(Integer.class));
    }

    @Test
    public void createBooking_whenPassengerCreationFails_rollsBackBookingAndSeat() throws Exception {
        // Arrange
        int flightId = 10;
        int seatRow = 4;
        String seatColumn = "D";
        int seatId = 55;

        PassengerInput passengerInput = samplePassengerInput();
        Booking savedBooking = new Booking(200, CURRENT_USER_ID, flightId, seatId);

        when(flightService.getFlightById(flightId)).thenReturn(sampleFlight(flightId));
        when(seatService.bookSeat(flightId, seatRow, seatColumn)).thenReturn(seatId);
        when(bookingsRepo.addBooking(any(Booking.class))).thenReturn(savedBooking);

        doThrow(new ValidationException("Passenger invalid"))
                .when(passengerService)
                .addPassenger(passengerInput, savedBooking.getBookingId());

        // Act
        BookingException exception = assertThrows(
                BookingException.class,
                () -> bookingService.createBooking(flightId, passengerInput, seatRow, seatColumn)
        );

        // Assert
        assertEquals(BOOKING_PASSENGER_ERROR, exception.getMessage());
        verify(bookingsRepo).deleteBooking(savedBooking.getBookingId());
        verify(seatService).unbookSeat(flightId, seatId);
        verify(flightService, never()).updateFlightAvailability(flightId);
    }

    @Test
    public void cancelBooking_whenBookingExists_updatesStatusAndUnbooksSeat() throws Exception {
        // Arrange
        int bookingId = 300;
        int flightId = 12;
        int seatId = 9;

        Booking booking = new Booking(bookingId, CURRENT_USER_ID, flightId, seatId);
        Seat seat = new Seat(seatId, flightId, 3, "A", true);

        when(bookingsRepo.getBookingById(bookingId)).thenReturn(booking);
        when(bookingsRepo.updateBookingStatus(bookingId, BookingStatus.CANCELLED)).thenReturn(true);
        when(seatService.getSeatById(flightId, seatId)).thenReturn(seat);

        // Act
        bookingService.cancelBooking(bookingId);

        // Assert
        verify(bookingsRepo).updateBookingStatus(bookingId, BookingStatus.CANCELLED);
        verify(seatService).unbookSeat(flightId, seatId);
        verify(flightService).updateFlightAvailability(flightId);
    }

    @Test
    public void cancelBooking_whenUpdateFails_throwsValidationException() throws ValidationException {
        // Arrange
        int bookingId = 301;
        Booking booking = new Booking(bookingId, CURRENT_USER_ID, 15, 22);

        when(bookingsRepo.getBookingById(bookingId)).thenReturn(booking);
        when(bookingsRepo.updateBookingStatus(bookingId, BookingStatus.CANCELLED)).thenReturn(false);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.cancelBooking(bookingId)
        );

        // Assert
        assertEquals(BOOKING_UPDATE_FAILED, exception.getMessage());
        verify(seatService, never()).getSeatById(any(Integer.class), any(Integer.class));
        verify(seatService, never()).unbookSeat(any(Integer.class), any(Integer.class));
    }

    @Test
    public void getBookingDetailsForCurrentUser_returnsOnlyUpcomingBookings() throws Exception {
        // Arrange
        Booking upcomingBooking1 = new Booking(1, CURRENT_USER_ID, 100, 11);
        upcomingBooking1.setStatus(BookingStatus.UPCOMING);

        Booking cancelledBooking = new Booking(2, CURRENT_USER_ID, 101, 12);
        cancelledBooking.setStatus(BookingStatus.CANCELLED);

        Booking upcomingBooking2 = new Booking(3, CURRENT_USER_ID, 102, 13);
        upcomingBooking2.setStatus(BookingStatus.UPCOMING);

        when(bookingsRepo.getBookingByUserId(CURRENT_USER_ID))
                .thenReturn(Arrays.asList(upcomingBooking1, cancelledBooking, upcomingBooking2));

        when(flightService.getFlightById(100)).thenReturn(sampleFlight(100));
        when(flightService.getFlightById(102)).thenReturn(sampleFlight(102));

        when(passengerService.getPassengerByBookingId(1)).thenReturn(samplePassenger(1));
        when(passengerService.getPassengerByBookingId(3)).thenReturn(samplePassenger(3));

        // Act
        List<BookingDetails> result = bookingService.getBookingDetailsForCurrentUser();

        // Assert
        assertEquals(2, result.size());

        //assert all of them have status upcoming
        assertTrue(
                result.stream()
                        .allMatch(d -> d.getBooking().getStatus() == BookingStatus.UPCOMING)
        );
    }

    @Test
    public void getCancelledBookingDetailsForCurrentUser_returnsOnlyCancelledBookings() throws Exception {
        // Arrange
        Booking upcomingBooking = new Booking(1, CURRENT_USER_ID, 100, 11);
        upcomingBooking.setStatus(BookingStatus.UPCOMING);

        Booking cancelledBooking = new Booking(2, CURRENT_USER_ID, 101, 12);
        cancelledBooking.setStatus(BookingStatus.CANCELLED);

        when(bookingsRepo.getBookingByUserId(CURRENT_USER_ID))
                .thenReturn(Arrays.asList(upcomingBooking, cancelledBooking));

        when(flightService.getFlightById(101)).thenReturn(sampleFlight(101));
        when(passengerService.getPassengerByBookingId(2)).thenReturn(samplePassenger(2));

        // Act
        List<BookingDetails> result = bookingService.getCancelledBookingDetailsForCurrentUser();

        // Assert
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getBooking().getBookingId());
    }

    @Test
    public void getBookingDetailsById_whenBookingExists_returnsDetails() throws ValidationException {
        // Arrange
        int bookingId = 400;
        int flightId = 25;
        int seatId = 90;
        String formattedSeat = "12A";

        Booking booking = new Booking(bookingId, CURRENT_USER_ID, flightId, seatId);
        Flight flight = sampleFlight(flightId);
        Passenger passenger = samplePassenger(bookingId);

        when(bookingsRepo.getBookingById(bookingId)).thenReturn(booking);
        when(flightService.getFlightById(flightId)).thenReturn(flight);
        when(passengerService.getPassengerByBookingId(bookingId)).thenReturn(passenger);
        when(seatService.getFormattedSeatById(flightId, seatId)).thenReturn(formattedSeat);

        // Act
        BookingDetails result = bookingService.getBookingDetailsById(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(booking, result.getBooking());
        assertEquals(flight, result.getFlight());
        assertEquals(passenger, result.getPassenger());
        assertEquals(formattedSeat, result.getFormattedSeat());
    }

    @Test
    public void getBookingDetailsById_whenBookingMissing_throwsValidationException() throws ValidationException{
        // Arrange
        int bookingId = 9999;
        when(bookingsRepo.getBookingById(bookingId)).thenReturn(null);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.cancelBooking(bookingId)
        );


        //assert
        assertEquals(BOOKING_NOT_FOUND, exception.getMessage());
        verify(flightService, never()).getFlightById(any (Integer.class));
        verify(passengerService, never()).getPassengerByBookingId(any (Integer.class));
    }

    private PassengerInput samplePassengerInput() {
        return new PassengerInput(
                "John",
                "Doe",
                "2000-01-01",
                "P1234567"
        );
    }

    private Flight sampleFlight(int flightId) {
        return new Flight(
                flightId,
                AIR_CANADA,
                TORONTO,
                WINNIPEG,
                LocalTime.of(10, 0),
                LocalTime.of(12, 30),
                1,
                299.99,
                "AC101",
                LocalDate.of(2026, 3, 20)
        );
    }

    private Passenger samplePassenger(int bookingId) {
        return new Passenger(
                1,
                bookingId,
                "John",
                "Doe",
                LocalDate.of(2000, 1, 1),
                "P1234567"
        );
    }
}