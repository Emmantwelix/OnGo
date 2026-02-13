package com.group9.ongo.business.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.persistence.fake.FakeBookingRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;
import com.group9.ongo.persistence.fake.FakePassengerRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingServiceTest {

    private BookingService bookingService;
    private FakeBookingRepository bookingRepo;
    private FakePassengerRepository passengerRepo;

    private FlightService flightService;

    @Before
    public void setUp() {
        bookingRepo = new FakeBookingRepository();
        passengerRepo = new FakePassengerRepository();
        flightService = new FlightServiceImpl(new FakeFlightRepository(true));
        bookingService = new BookingService(bookingRepo, passengerRepo, flightService);
    }

    @Test
    public void getBookingByUserId_returnsEmptyList_whenUserHasNoBookings() {
        List<Booking> bookings = bookingService.getBookingByUserId(999);
        assertNotNull(bookings);
        assertTrue(bookings.isEmpty());
    }

    @Test
    public void getBookingByUserId_returnsOnlyThatUsersBookings() {
        bookingService.createBooking(1, 1, samplePassengerInput("A"));
        bookingService.createBooking(1, 2, samplePassengerInput("B"));
        bookingService.createBooking(2, 3, samplePassengerInput("C"));

        List<Booking> user1Bookings = bookingService.getBookingByUserId(1);

        assertEquals(2, user1Bookings.size());
        for (Booking b : user1Bookings) {
            assertEquals(1, b.getUserId());
        }
    }

    @Test
    public void createBooking_createsBookingAndPassengerLinkedToBookingId() {
        PassengerInput input = samplePassengerInput("Z");

        Booking booking = bookingService.createBooking(5, 1, input);

        assertNotNull(booking);
        assertTrue(booking.getBookingId() > 0);
        assertEquals(5, booking.getUserId());
        assertEquals(1, booking.getFlightId());

        //ensure passenger information is correct
        Passenger p = passengerRepo.getPassengerByBookingId(booking.getBookingId());
        assertNotNull(p);
        assertEquals(booking.getBookingId(), p.getBookingId());
        assertEquals(input.firstName, p.getFirstName());
        assertEquals(input.lastName, p.getLastName());
        assertEquals(input.dateOfBirth, p.getDateOfBirth());
        assertEquals(input.passportNumber, p.getPassportNumber());
    }

    @Test
    public void createBooking_throwsValidationException_whenFlightDoesNotExist() {
        PassengerInput passenger = samplePassengerInput("A");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, 9999, passenger)
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
                () -> bookingService.createBooking(1, 1, null)
        );
        assertEquals("Passenger input cannot be null", ex.getMessage());
    }


    @Test
    public void createBooking_whenFirstNameIsNull_throwsExpectedMessage() {
        PassengerInput input = samplePassengerInput("A");
        input.firstName = null;

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, 1, input)
        );
        assertEquals("First name is required", ex.getMessage());
    }


    @Test
    public void createBooking_whenFirstNameIsBlank_throwsExpectedMessage() {
        PassengerInput input = samplePassengerInput("A");
        input.firstName = "   ";

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, 1, input)
        );

        assertEquals("First name is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenLastNameIsNull_throwsExpectedMessage() {
        PassengerInput input = samplePassengerInput("A");
        input.lastName = null;

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, 1, input)
        );

        assertEquals("Last name is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenLastNameIsBlank_throwsExpectedMessage() {
        PassengerInput input = samplePassengerInput("A");
        input.lastName = "";

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, 1, input)
        );

        assertEquals("Last name is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenDateOfBirthIsNull_throwsExpectedMessage() {
        PassengerInput input = samplePassengerInput("A");
        input.dateOfBirth = null;

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, 1, input)
        );

        assertEquals("Date of birth is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenPassportNumberIsNull_throwsExpectedMessage() {
        PassengerInput input = samplePassengerInput("A");
        input.passportNumber = null;

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, 1, input)
        );

        assertEquals("Passport number is required", ex.getMessage());
    }

    @Test
    public void createBooking_whenPassportNumberIsBlank_throwsExpectedMessage() {
        PassengerInput input = samplePassengerInput("A");
        input.passportNumber = " ";

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, 1, input)
        );

        assertEquals("Passport number is required", ex.getMessage());
    }


    @Test
    public void createBooking_whenPassengerInvalid_doesNotCreateBookingOrPassenger() {
        PassengerInput input = samplePassengerInput("A");
        input.firstName = null;

        assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(1, 1, input)
        );

        assertTrue(bookingService.getBookingByUserId(1).isEmpty());
    }


    @Test
    public void cancelBooking_deletesBookingAndItsPassenger_andReturnsTrue() {
        Booking booking = bookingService.createBooking(9, 1, samplePassengerInput("X"));
        int bookingId = booking.getBookingId();

        boolean success = bookingService.cancelBooking(bookingId);

        assertTrue(success);
        assertNull(bookingRepo.getBookingById(bookingId));
        assertNull(passengerRepo.getPassengerByBookingId(bookingId));
    }

    @Test
    public void cancelBooking_whenBookingDoesNotExist_returnsFalse_andDoesNotCrash() {
        boolean success = bookingService.cancelBooking(9999);
        assertFalse(success);
    }

    @Test
    public void cancelBooking_deletesOnlyPassengersForThatBookingId() {
        Booking b1 = bookingService.createBooking(1, 1, samplePassengerInput("A"));
        Booking b2 = bookingService.createBooking(1, 2, samplePassengerInput("B"));

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
    public void getBookingDetailsByUserId_whenUserHasNoBookings_returnsEmptyList() {
        List<BookingDetails> details = bookingService.getBookingDetailsByUserId(999);
        assertNotNull(details);
        assertTrue(details.isEmpty());
    }


    @Test
    public void getBookingDetailsByUserId_returnsBookingPassengerAndFlight() {
        PassengerInput samplePassenger = samplePassengerInput("A");

        Booking b1 = bookingService.createBooking(1, 1, samplePassenger);
        Booking b2 = bookingService.createBooking(1, 2, samplePassenger);
        bookingService.createBooking(2, 3, samplePassengerInput("C"));

        List<BookingDetails> details = bookingService.getBookingDetailsByUserId(1);

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
        PassengerInput input = new PassengerInput();
        input.firstName = "First" + tag;
        input.lastName = "Last" + tag;
        input.dateOfBirth = LocalDate.of(2000, 1, 1);
        input.passportNumber = "P" + tag + "12345";
        return input;
    }
}
