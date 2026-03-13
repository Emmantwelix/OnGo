package com.group9.ongo.business.services.IntegrationTests;

import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_BIRTHDATE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_FIRSTNAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_LASTNAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_PNUMBER;
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
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.group9.ongo.business.services.Implementations.PassengerServiceImpl;
import com.group9.ongo.business.services.Interfaces.PassengerService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
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

import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class PassengerServiceIntegrationTest {

    private PassengerRepository passengerRepository;
    private PassengerService passengerService;

    private BookingRepository bookingRepository;

    private int testBookingId;

    private FlightRepository flightRepository;
    private AircraftRepository aircraftRepository;


    @Before
    public void setUp() throws ValidationException {
        Context context = ApplicationProvider.getApplicationContext();

        context.deleteDatabase(AppDbHelper.DB_NAME);
        AppDbHelper dbHelper = new AppDbHelper(context, false);

        passengerRepository = new SqlPassengerRepository(dbHelper);
        passengerService = new PassengerServiceImpl(passengerRepository);
        bookingRepository = new SqlBookingRepository(dbHelper);
        UserRepository userRepository = new SqlUserRepository(dbHelper);
        flightRepository = new SqlFlightRepository(dbHelper);
        aircraftRepository = new SqlAircraftRepository(dbHelper);
        int userId = userRepository.addUser("test", "test@gmail.com","82380294");
        int flightId = createTestFlight();
        SeatRepository seatRepository = new SqlSeatRepository(dbHelper);
        int seatId = seatRepository.createSeat(flightId,1, "A");

        Booking booking = bookingRepository.addBooking(new Booking(0,userId,flightId,seatId));
        testBookingId = booking.getBookingId();
    }

    @Test
    public void addPassenger_whenInputIsValid_returnsPassenger() throws ValidationException {
        PassengerInput input = new PassengerInput("John", "Doe", "2000-01-01", "P123456");

        Passenger result = passengerService.addPassenger(input, testBookingId);

        assertNotNull(result);
        assertTrue(result.getPassengerId() > 0);
        assertEquals(testBookingId, result.getBookingId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), result.getDateOfBirth());
        assertEquals("P123456", result.getPassportNumber());

        Passenger persisted = passengerService.getPassengerByBookingId(testBookingId);
        assertNotNull(persisted);
        assertEquals(result.getPassengerId(), persisted.getPassengerId());
        assertEquals(testBookingId, persisted.getBookingId());
        assertEquals("John", persisted.getFirstName());
        assertEquals("Doe", persisted.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), persisted.getDateOfBirth());
        assertEquals("P123456", persisted.getPassportNumber());
    }

    @Test
    public void addPassenger_whenRepositoryCannotCreatePassenger_throwsValidationException() {
        PassengerInput input = new PassengerInput("John", "Doe", "2000-01-01", "P123456");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.addPassenger(input, -999)
        );

        assertEquals(PASSENGER_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void getPassengerByBookingId_whenPassengerExists_returnsPassenger() throws ValidationException{
        PassengerInput input = new PassengerInput("Jane", "Smith", "1998-05-12", "X987654");
        Passenger created = passengerService.addPassenger(input, testBookingId);

        Passenger result = passengerService.getPassengerByBookingId(testBookingId);

        assertNotNull(result);
        assertEquals(created.getPassengerId(), result.getPassengerId());
        assertEquals(testBookingId, result.getBookingId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(LocalDate.of(1998, 5, 12), result.getDateOfBirth());
        assertEquals("X987654", result.getPassportNumber());
    }

    @Test
    public void getPassengerByBookingId_whenPassengerMissing_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.getPassengerByBookingId(999999)
        );

        assertEquals(PASSENGER_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void updatePassengerInfo_whenInputIsValid_returnsTrue() throws ValidationException {
        PassengerInput input = new PassengerInput("John", "Doe", "2000-01-01", "P123456");
        Passenger created = passengerService.addPassenger(input, testBookingId);

        boolean result = passengerService.updatePassengerInfo(
                String.valueOf(created.getPassengerId()),
                "Johnny",
                "Dorian",
                "2001-02-03",
                "NEW999"
        );

        assertTrue(result);

        Passenger updated = passengerService.getPassengerByBookingId(testBookingId);
        assertEquals(created.getPassengerId(), updated.getPassengerId());
        assertEquals("Johnny", updated.getFirstName());
        assertEquals("Dorian", updated.getLastName());
        assertEquals(LocalDate.of(2001, 2, 3), updated.getDateOfBirth());
        assertEquals("NEW999", updated.getPassportNumber());
    }

    @Test
    public void updatePassengerInfo_whenPassengerDoesNotExist_returnsFalse() throws Exception {
        boolean result = passengerService.updatePassengerInfo(
                "999999",
                "John",
                "Doe",
                "2000-01-01",
                "P123456"
        );

        assertFalse(result);
    }

    @Test
    public void updatePassengerInfo_whenFirstNameIsNull_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.updatePassengerInfo(
                        "1",
                        null,
                        "Doe",
                        "2000-01-01",
                        "P123456"
                )
        );

        assertEquals(PASSENGER_NO_FIRSTNAME, exception.getMessage());
    }

    @Test
    public void updatePassengerInfo_whenLastNameIsBlank_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.updatePassengerInfo(
                        "1",
                        "John",
                        "",
                        "2000-01-01",
                        "P123456"
                )
        );

        assertEquals(PASSENGER_NO_LASTNAME, exception.getMessage());
    }

    @Test
    public void updatePassengerInfo_whenDobIsNull_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.updatePassengerInfo(
                        "1",
                        "John",
                        "Doe",
                        null,
                        "P123456"
                )
        );

        assertEquals(PASSENGER_NO_BIRTHDATE, exception.getMessage());
    }

    @Test
    public void updatePassengerInfo_whenPassportIsBlank_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.updatePassengerInfo(
                        "1",
                        "John",
                        "Doe",
                        "2000-01-01",
                        ""
                )
        );

        assertEquals(PASSENGER_NO_PNUMBER, exception.getMessage());
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