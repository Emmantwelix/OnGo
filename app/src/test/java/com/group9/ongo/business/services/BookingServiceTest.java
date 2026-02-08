package com.group9.ongo.business.services;

import static org.junit.Assert.*;

import com.group9.ongo.models.Booking;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.persistence.fake.FakeBookingRepository;
import com.group9.ongo.persistence.fake.FakePassengerRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

/**
 * Unit tests for BookingService (excluding getBookingDetailsByUserId).
 *
 * Assumes:
 * - FakeBookingRepository implements: addBooking, getBookingByUserId, getBookingById, updateBooking, deleteBooking
 * - FakePassengerRepository implements: addPassenger(PassengerInput, int), getPassengerByBookingId(int),
 *   deletePassengersByBookingId(int)
 */
public class BookingServiceTest {

    private BookingService bookingService;
    private FakeBookingRepository bookingRepo;
    private FakePassengerRepository passengerRepo;


    @Before
    public void setUp() {
        bookingRepo = new FakeBookingRepository();
        passengerRepo = new FakePassengerRepository();
        bookingService = new BookingService(bookingRepo, passengerRepo, null);
    }

    @Test
    public void getBookingByUserId_returnsOnlyThatUsersBookings() {
        // Arrange: create bookings for user 1 and user 2
        bookingService.createBooking(1, 100, samplePassengerInput("A"));
        bookingService.createBooking(1, 101, samplePassengerInput("B"));
        bookingService.createBooking(2, 200, samplePassengerInput("C"));

        // Act
        List<Booking> user1Bookings = bookingService.getBookingByUserId(1);

        // Assert
        assertNotNull(user1Bookings);
        assertEquals(2, user1Bookings.size());
        for (Booking b : user1Bookings) {
            assertEquals(1, b.getUserId());
        }
    }

    @Test
    public void createBooking_createsBookingAndPassengerLinkedToBookingId() {
        // Arrange
        int userId = 5;
        int flightId = 77;
        PassengerInput input = samplePassengerInput("Z");

        // Act
        Booking booking = bookingService.createBooking(userId, flightId, input);

        // Assert booking
        assertNotNull(booking);
        assertTrue("bookingId should be assigned", booking.getBookingId() > 0);
        assertEquals(userId, booking.getUserId());
        assertEquals(flightId, booking.getFlightId());

        // Assert passenger created + linked
        Passenger p = passengerRepo.getPassengerByBookingId(booking.getBookingId());
        assertNotNull("Passenger should be created for booking", p);
        assertEquals(booking.getBookingId(), p.getBookingId());
        assertEquals(input.firstName, p.getFirstName());
        assertEquals(input.lastName, p.getLastName());
        assertEquals(input.dateOfBirth, p.getDateOfBirth());
        assertEquals(input.passportNumber, p.getPassportNumber());
    }

    @Test
    public void cancelBooking_deletesBookingAndItsPassenger_andReturnsTrue() {
        // Arrange: create a booking with passenger
        Booking booking = bookingService.createBooking(9, 555, samplePassengerInput("X"));
        int bookingId = booking.getBookingId();
        assertNotNull(passengerRepo.getPassengerByBookingId(bookingId));

        // Act
        boolean success = bookingService.cancelBooking(bookingId);

        // Assert
        assertTrue(success);
        assertNull("Booking should be deleted", bookingRepo.getBookingById(bookingId));
        assertNull("Passenger should be deleted", passengerRepo.getPassengerByBookingId(bookingId));
    }

    @Test
    public void cancelBooking_whenBookingDoesNotExist_returnsFalse_andDoesNotCrash() {
        // Arrange
        int missingBookingId = 9999;

        // Act
        boolean success = bookingService.cancelBooking(missingBookingId);

        // Assert
        assertFalse(success);
    }

    // ---------- helpers ----------

    private PassengerInput samplePassengerInput(String tag) {
        PassengerInput input = new PassengerInput();
        input.firstName = "First" + tag;
        input.lastName = "Last" + tag;
        input.dateOfBirth = LocalDate.of(2000, 1, 1);
        input.passportNumber = "P" + tag + "12345";
        return input;
    }
}
