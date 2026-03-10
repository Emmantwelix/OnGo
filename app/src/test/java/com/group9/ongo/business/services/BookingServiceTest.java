package com.group9.ongo.business.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.group9.ongo.business.services.Implementations.AircraftServiceImpl;
import com.group9.ongo.business.services.Implementations.BookingServiceImpl;
import com.group9.ongo.business.services.Implementations.FlightDetailGen;
import com.group9.ongo.business.services.Implementations.FlightServiceImpl;
import com.group9.ongo.business.services.Implementations.SeatServiceImplementation;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.fake.FakeAircraftRepository;
import com.group9.ongo.persistence.fake.FakeBookingRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;
import com.group9.ongo.persistence.fake.FakePassengerRepository;
import com.group9.ongo.persistence.fake.FakeSeatsRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingServiceTest {

    private BookingService bookingService;
    private BookingService bookingService2;
    private FakeBookingRepository bookingRepo;
    private FakePassengerRepository passengerRepo;
    private Generator generator;

    private FlightService flightService;
    private SeatService seatService;

    @Before
    public void setUp() {
        generator = new FlightDetailGen(new Random());
        bookingRepo = new FakeBookingRepository();
        passengerRepo = new FakePassengerRepository();
        seatService = new SeatServiceImplementation(new FakeSeatsRepository(true));
        flightService = new FlightServiceImpl(new FakeFlightRepository(true), generator, seatService, new AircraftServiceImpl(new FakeAircraftRepository()));
        bookingService = new BookingServiceImpl(1,bookingRepo, passengerRepo, flightService, seatService);
        bookingService2 = new BookingServiceImpl(2,bookingRepo, passengerRepo, flightService, seatService);
    }


    @Test
    public void getBookingByUserId_returnsOnlyThatUsersBookings() throws BookingException, ValidationException {
        bookingService.createBooking( 1, samplePassengerInput("A"), 1, "A");
        bookingService.createBooking( 2, samplePassengerInput("B"), 1, "A");
        bookingService2.createBooking( 3, samplePassengerInput("C"), 1, "A");

        List<BookingDetails> user1Bookings = bookingService.getBookingDetailsForCurrentUser();

        assertEquals(2, user1Bookings.size());
    }

    @Test
    public void createBooking_createsBookingAndPassengerLinkedToBookingId() throws BookingException, ValidationException {
        PassengerInput input = samplePassengerInput("Z");

        Booking booking = bookingService.createBooking( 1, input, 1, "A");

        assertNotNull(booking);
        assertTrue(booking.getBookingId() > 0);
        assertEquals(1, booking.getUserId());
        assertEquals(1, booking.getFlightId());

        //ensure passenger information is correct
        Passenger p = passengerRepo.getPassengerByBookingId(booking.getBookingId());
        assertNotNull(p);
        assertEquals(booking.getBookingId(), p.getBookingId());
        assertEquals(input.getFirstName(), p.getFirstName());
        assertEquals(input.getLastName(), p.getLastName());
        assertEquals(input.getDateOfBirth(), p.getDateOfBirth().toString());
        assertEquals(input.getPassportNumber(), p.getPassportNumber());
    }

    @Test
    public void createBooking_throwsValidationException_whenFlightDoesNotExist()  {
        PassengerInput passenger = samplePassengerInput("A");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking( 9999, passenger, 1, "A")
        );

        assertEquals(
                "Flight not found",
                exception.getMessage()
        );
    }

    @Test
    public void createBooking_whenPassengerInputIsNull_throwsExpectedMessage() {
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, null, 1, "A")
        );
        assertEquals("Passenger input cannot be null", ex.getMessage());
    }


    @Test
    public void createBooking_whenFirstNameIsNull_throwsExpectedMessage() {
        PassengerInput input = new PassengerInput(null, "Last", "2000-01-01", "P123");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking( 1, input, 1,"A")
        );
        assertEquals("First name is required", ex.getMessage());
    }


    @Test
    public void createBooking_whenFirstNameIsBlank_throwsExpectedMessage() {
        PassengerInput input = new PassengerInput("   ", "Last", "2000-01-01", "P123");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking( 1, input, 1, "A")
        );

        assertEquals("First name is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenLastNameIsNull_throwsExpectedMessage() {
        PassengerInput input = new PassengerInput("First", null, "2000-01-01", "P123");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking( 1, input, 1, "A")
        );

        assertEquals("Last name is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenLastNameIsBlank_throwsExpectedMessage() {
        PassengerInput input = new PassengerInput("First", "", "2000-01-01", "P123");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, input, 1, "A")
        );

        assertEquals("Last name is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenDateOfBirthIsNull_throwsExpectedMessage() {
        PassengerInput input = new PassengerInput("First", "Last", null, "P123");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking( 1, input, 1, "A")
        );

        assertEquals("Date of birth is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenPassportNumberIsNull_throwsExpectedMessage() {
        PassengerInput input = new PassengerInput("First", "Last", "2000-01-01", null);

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking( 1, input, 1, "A")
        );

        assertEquals("Passport number is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenPassportNumberIsBlank_throwsExpectedMessage() {
        PassengerInput input = new PassengerInput("First", "Last", "2000-01-01", " ");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking( 1, input, 1, "A")
        );

        assertEquals("Passport number is required", ex.getMessage());
    }


    @Test
    public void createBooking_whenPassengerInvalid_doesNotCreateBookingOrPassenger() {
        PassengerInput input = new PassengerInput(null, "Last", "2000-01-01", "P123");

        assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, input, 1, "A")
        );

        assertTrue(bookingService.getBookingDetailsForCurrentUser().isEmpty());
    }

    @Test
    public void createBooking_whenSeatIsBooked_throwsException() throws ValidationException, BookingException {
        PassengerInput input = samplePassengerInput("Z");
        Booking booking = bookingService.createBooking( 1, input, 1, "A");
        assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking( 1, input, 1, "A")
        );
    }

    @Test
    public void createBooking_booksSeat() throws BookingException, ValidationException {
        PassengerInput input = samplePassengerInput("Z");
        Booking booking = bookingService.createBooking(1, input, 1, "A");
        Seat seat = seatService.getSeatById(booking.getFlightId(), booking.getSeatId());
        assertTrue(seat.getIsBooked());
    }


    @Test
    public void cancelBooking_deletesBookingAndItsPassenger_andReturnsTrue() throws BookingException, ValidationException {
        Booking booking = bookingService.createBooking( 1, samplePassengerInput("X"), 1, "A");
        int bookingId = booking.getBookingId();

        boolean success = bookingService.cancelBooking(bookingId);

        assertTrue(success);
        assertNull(bookingRepo.getBookingById(bookingId));
        assertNull(passengerRepo.getPassengerByBookingId(bookingId));
    }

    @Test
    public void cancelBooking_whenBookingDoesNotExist_returnsFalse_andDoesNotCrash() throws ValidationException {
        boolean success = bookingService.cancelBooking(9999);
        assertFalse(success);
    }

    @Test
    public void cancelBooking_deletesOnlyPassengersForThatBookingId() throws BookingException, ValidationException {
        Booking b1 = bookingService.createBooking(1, samplePassengerInput("A"), 1, "A");
        Booking b2 = bookingService2.createBooking(2, samplePassengerInput("B"), 1, "A");

        boolean success = bookingService.cancelBooking(b1.getBookingId());

        assertTrue(success);
        //ensure passenger and booking are deleted
        assertNull(passengerRepo.getPassengerByBookingId(b1.getBookingId()));
        assertNull(bookingRepo.getBookingById(b1.getBookingId()));

        //ensure other passengers and bookings are not deleted
        assertNotNull(passengerRepo.getPassengerByBookingId(b2.getBookingId()));
        assertNotNull(bookingRepo.getBookingById(b2.getBookingId()));
    }

    @Test
    public void cancelBooking_unbooksSeat() throws BookingException, ValidationException {
        Booking booking = bookingService.createBooking(1, samplePassengerInput("A"), 1, "A");
        Seat bookedSeat = seatService.getSeatById(booking.getFlightId(), booking.getSeatId());
        bookingService.cancelBooking(booking.getBookingId());

        assertFalse(bookedSeat.getIsBooked());
    }

    @Test
    public void getBookingDetailsByUserId_whenUserHasNoBookings_returnsEmptyList() {
        List<BookingDetails> details = bookingService2.getBookingDetailsForCurrentUser();
        assertNotNull(details);
        assertTrue(details.isEmpty());
    }


    @Test
    public void getBookingDetailsByUserId_returnsBookingPassengerAndFlight() throws BookingException, ValidationException {
        PassengerInput samplePassenger = samplePassengerInput("A");

        Booking b1 = bookingService.createBooking(1, samplePassenger, 1, "A");
        Booking b2 = bookingService.createBooking( 2, samplePassenger, 1, "A");
        bookingService2.createBooking(3, samplePassengerInput("C"), 1, "A");

        List<BookingDetails> details = bookingService.getBookingDetailsForCurrentUser();

        // Correct number of results
        assertEquals(2, details.size());

        // Correct bookings returned
        Set<Integer> bookingIds =
                details.stream()
                        .map(d -> d.getBooking().getBookingId())
                        .collect(Collectors.toSet());

        assertEquals(
                Set.of(b1.getBookingId(), b2.getBookingId()),
                bookingIds
        );

        // Validate each BookingDetails object
        for (BookingDetails detail : details) {
            assertNotNull(detail.getBooking());
            assertNotNull(detail.getFlight());
            assertNotNull(detail.getPassenger());

            // Ownership
            assertEquals(1, detail.getBooking().getUserId());

            // Ensure Each passenger and bookings are correctly related
            assertEquals(
                    detail.getBooking().getBookingId(),
                    detail.getPassenger().getBookingId()
            );

            // Passenger data
            assertEquals("FirstA", detail.getPassenger().getFirstName());
        }

        // Correct flights returned
        Set<Integer> flightIds =
                details.stream()
                        .map(d -> d.getFlight().getFlightId())
                        .collect(Collectors.toSet());

        assertEquals(Set.of(1, 2), flightIds);
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
