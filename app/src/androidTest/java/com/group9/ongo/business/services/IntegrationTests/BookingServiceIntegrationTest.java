package com.group9.ongo.business.services.IntegrationTests;

import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_NOT_FOUND;
import static com.group9.ongo.business.constants.FlightConstants.A380_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_TIME;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_TIME2;
import static com.group9.ongo.business.constants.FlightConstants.LARGE_PRICE;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;


import com.group9.ongo.business.services.BookingException;
import com.group9.ongo.business.services.Implementations.BookingServiceImpl;
import com.group9.ongo.business.services.Implementations.FlightDetailGen;
import com.group9.ongo.business.services.Implementations.FlightServiceImpl;
import com.group9.ongo.business.services.Implementations.PassengerServiceImpl;
import com.group9.ongo.business.services.Implementations.SeatServiceImpl;
import com.group9.ongo.business.services.Implementations.UserServiceImpl;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.PassengerService;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.services.Interfaces.UserService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.BookingStatus;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.AircraftRepository;
import com.group9.ongo.persistence.BookingRepository;
import com.group9.ongo.persistence.FlightRepository;
import com.group9.ongo.persistence.PassengerRepository;
import com.group9.ongo.persistence.SeatRepository;
import com.group9.ongo.persistence.UserRepository;
import com.group9.ongo.persistence.real.AppDbHelper;
import com.group9.ongo.persistence.real.SqlAircraftRepository;
import com.group9.ongo.persistence.real.SqlBookingRepository;
import com.group9.ongo.persistence.real.SqlFlightRepository;
import com.group9.ongo.persistence.real.SqlPassengerRepository;
import com.group9.ongo.persistence.real.SqlSeatRepository;
import com.group9.ongo.persistence.real.SqlUserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import java.util.List;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class BookingServiceIntegrationTest {

    private BookingService bookingService;
    private BookingService bookingService2;

    private PassengerService passengerService;
    private FlightService flightService;
    private SeatService seatService;
    private UserService userService;

    private AircraftRepository aircraftRepository;


    private int currentUserId;
    private int secondUserId;

    @Before
    public void setUp() throws ValidationException {
        Context context = ApplicationProvider.getApplicationContext();

        // fresh db without seed data
        context.deleteDatabase(AppDbHelper.DB_NAME);
        AppDbHelper dbHelper = new AppDbHelper(context, false);

        BookingRepository bookingRepository = new SqlBookingRepository(dbHelper);
        PassengerRepository passengerRepository = new SqlPassengerRepository(dbHelper);
        FlightRepository flightRepository = new SqlFlightRepository(dbHelper);
        SeatRepository seatRepository = new SqlSeatRepository(dbHelper);
        aircraftRepository = new SqlAircraftRepository(dbHelper);
        UserRepository userRepository = new SqlUserRepository(dbHelper);

        seatService = new SeatServiceImpl(seatRepository);

        Generator generator = new FlightDetailGen(new Random(1));
        flightService = new FlightServiceImpl(
                flightRepository,
                generator,
                seatService,
                aircraftRepository
        );

        passengerService = new PassengerServiceImpl(passengerRepository);
        userService = new UserServiceImpl(userRepository);


        currentUserId = userService.createUser(
                "User",
                "user@test.com",
                "2045551234"
        );

        secondUserId = userService.createUser(
                "UserTwo",
                "usertwo@test.com",
                "4315551234"
        );

        bookingService = new BookingServiceImpl(
                currentUserId,
                bookingRepository,
                passengerService,
                flightService,
                seatService
        );

        bookingService2 = new BookingServiceImpl(
                secondUserId,
                bookingRepository,
                passengerService,
                flightService,
                seatService
        );
    }

    @Test
    public void createBooking_whenValidInput_createsBookingAndPassenger()
            throws ValidationException, BookingException {
        // Arrange
        int flightId = createTestFlight();
        PassengerInput input = samplePassengerInput("A");

        // Act
        Booking booking = bookingService.createBooking(flightId, input, 1, "A");

        // Assert
        assertNotNull(booking);
        assertTrue(booking.getBookingId() > 0);
        assertEquals(currentUserId, booking.getUserId());
        assertEquals(flightId, booking.getFlightId());

        BookingDetails details = bookingService.getBookingDetailsById(booking.getBookingId());
        assertNotNull(details);
        assertEquals(booking.getBookingId(), details.getBooking().getBookingId());
        assertEquals(input.getFirstName(), details.getPassenger().getFirstName());
        assertEquals(input.getLastName(), details.getPassenger().getLastName());
        assertEquals(input.getPassportNumber(), details.getPassenger().getPassportNumber());
        assertEquals("1A", details.getFormattedSeat());

        Seat seat = seatService.getSeatById(flightId, booking.getSeatId());
        assertTrue(seat.getIsBooked());
        assertEquals(seat.getSeatRow() + seat.getLabel(), details.getFormattedSeat());
    }

    @Test
    public void createBooking_whenFlightDoesNotExist_throwsValidationException() {
        // Arrange
        PassengerInput input = samplePassengerInput("B");

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(999999, input, 1, "A")
        );

        // Assert
        assertEquals(FLIGHT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void createBooking_whenSeatAlreadyBooked_throwsValidationException()
            throws ValidationException, BookingException {
        // Arrange
        int flightId = createTestFlight();

        bookingService.createBooking(flightId, samplePassengerInput("C1"), 1, "A");

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService2.createBooking(flightId, samplePassengerInput("C2"), 1, "A")
        );

        // Assert
        assertNotNull(exception);
    }

    @Test
    public void getBookingDetailsForCurrentUser_returnsOnlyUpcomingBookings()
            throws ValidationException, BookingException {
        // Arrange
        int flightId1 = createTestFlight();
        int flightId2 = createTestFlight();
        int flightId3 = createTestFlight();

        bookingService.createBooking(flightId1, samplePassengerInput("D1"), 1, "A");
        bookingService.createBooking(flightId2, samplePassengerInput("D2"), 1, "B");
        bookingService2.createBooking(flightId3, samplePassengerInput("D3"), 1, "A");

        // Act
        List<BookingDetails> result = bookingService.getBookingDetailsForCurrentUser();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(d -> d.getBooking().getStatus() == BookingStatus.UPCOMING));
        assertTrue(result.stream().allMatch(d -> d.getBooking().getUserId() == currentUserId));
    }

    @Test
    public void cancelBooking_whenBookingExists_updatesStatusAndUnbooksSeat()
            throws ValidationException, BookingException {
        // Arrange
        int flightId = createTestFlight();
        Booking booking = bookingService.createBooking(flightId, samplePassengerInput("E"), 1, "A");

        Seat beforeCancel = seatService.getSeatById(flightId, booking.getSeatId());
        assertTrue(beforeCancel.getIsBooked());

        // Act
        bookingService.cancelBooking(booking.getBookingId());

        // Assert
        BookingDetails cancelled = bookingService.getBookingDetailsById(booking.getBookingId());
        assertEquals(BookingStatus.CANCELLED, cancelled.getBooking().getStatus());

        Seat afterCancel = seatService.getSeatById(flightId, booking.getSeatId());
        assertFalse(afterCancel.getIsBooked());
    }

    @Test
    public void getCancelledBookingDetailsForCurrentUser_returnsOnlyCancelledBookings()
            throws ValidationException, BookingException {
        // Arrange
        int flightId1 = createTestFlight();
        int flightId2 = createTestFlight();

        Booking active = bookingService.createBooking(flightId1, samplePassengerInput("F1"), 1, "A");
        Booking cancelled = bookingService.createBooking(flightId2, samplePassengerInput("F2"), 1, "B");

        bookingService.cancelBooking(cancelled.getBookingId());

        // Act
        List<BookingDetails> result = bookingService.getCancelledBookingDetailsForCurrentUser();

        // Assert
        assertEquals(1, result.size());
        assertEquals(cancelled.getBookingId(), result.get(0).getBooking().getBookingId());
        assertEquals(BookingStatus.CANCELLED, result.get(0).getBooking().getStatus());

        BookingDetails activeDetails = bookingService.getBookingDetailsById(active.getBookingId());
        assertEquals(BookingStatus.UPCOMING, activeDetails.getBooking().getStatus());
    }

    @Test
    public void getBookingDetailsById_whenBookingExists_returnsDetails()
            throws ValidationException, BookingException {
        // Arrange
        int flightId = createTestFlight();
        PassengerInput input = samplePassengerInput("G");

        Booking booking = bookingService.createBooking(flightId, input, 1, "A");

        // Act
        BookingDetails result = bookingService.getBookingDetailsById(booking.getBookingId());

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBooking());
        assertNotNull(result.getFlight());
        assertNotNull(result.getPassenger());

        assertEquals(booking.getBookingId(), result.getBooking().getBookingId());
        assertEquals(flightId, result.getFlight().getFlightId());
        assertEquals(input.getFirstName(), result.getPassenger().getFirstName());
        assertEquals(input.getLastName(), result.getPassenger().getLastName());
        assertEquals(input.getPassportNumber(), result.getPassenger().getPassportNumber());
    }

    @Test
    public void getBookingDetailsById_whenBookingMissing_throwsValidationException() {
        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.getBookingDetailsById(999999)
        );

        // Assert
        assertEquals(BOOKING_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void cancelBooking_whenBookingMissing_throwsValidationException() {
        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.cancelBooking(999999)
        );

        // Assert
        assertEquals(BOOKING_NOT_FOUND, exception.getMessage());
    }



    private int createTestFlight() throws ValidationException {

        Aircraft aircraft = aircraftRepository.addAircraft(A380_DETAILS);

        return flightService.createFlight(
                AIR_CANADA,
                WINNIPEG,
                TORONTO,
                DEFAULT_TIME,
                DEFAULT_TIME2,
                aircraft.getAircraftId(),
                LARGE_PRICE
        );
    }

    private PassengerInput samplePassengerInput(String tag) {
        return new PassengerInput(
                "First" + tag,
                "Last" + tag,
                "2000-01-01",
                "P" + tag + "12345"
        );
    }
}