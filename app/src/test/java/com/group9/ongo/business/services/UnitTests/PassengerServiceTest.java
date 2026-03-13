package com.group9.ongo.business.services.UnitTests;


import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NOT_FOUND;

import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_BIRTHDATE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_FIRSTNAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_LASTNAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_PNUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group9.ongo.business.services.Implementations.PassengerServiceImpl;
import com.group9.ongo.business.services.Interfaces.PassengerService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.persistence.PassengerRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    private PassengerService passengerService;

    @Before
    public void setUp() {
        passengerService = new PassengerServiceImpl(passengerRepository);
    }

    @Test
    public void addPassenger_whenInputIsValid_returnsPassenger() throws ValidationException {
        // Arrange
        int bookingId = 10;
        PassengerInput input = new PassengerInput("John", "Doe", "2000-01-01", "P123456");
        Passenger passenger = new Passenger(1, bookingId, "John", "Doe",
                LocalDate.of(2000, 1, 1), "P123456");

        when(passengerRepository.addPassenger(input, bookingId)).thenReturn(passenger);

        // Act
        Passenger result = passengerService.addPassenger(input, bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPassengerId());
        assertEquals(bookingId, result.getBookingId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), result.getDateOfBirth());
        assertEquals("P123456", result.getPassportNumber());
        verify(passengerRepository).addPassenger(input, bookingId);
    }

    @Test
    public void addPassenger_whenRepositoryReturnsNull_throwsValidationException() {
        // Arrange
        int bookingId = 10;
        PassengerInput input = new PassengerInput("John", "Doe", "2000-01-01", "P123456");

        when(passengerRepository.addPassenger(input, bookingId)).thenReturn(null);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.addPassenger(input, bookingId)
        );

        // Assert
        assertEquals(PASSENGER_NOT_FOUND, exception.getMessage());
        verify(passengerRepository).addPassenger(input, bookingId);
    }

    @Test
    public void getPassengerByBookingId_whenPassengerExists_returnsPassenger() throws ValidationException {
        // Arrange
        int bookingId = 20;
        Passenger passenger = new Passenger(2, bookingId, "Jane", "Smith",
                LocalDate.of(1998, 5, 12), "X987654");

        when(passengerRepository.getPassengerByBookingId(bookingId)).thenReturn(passenger);

        // Act
        Passenger result = passengerService.getPassengerByBookingId(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPassengerId());
        assertEquals(bookingId, result.getBookingId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(LocalDate.of(1998, 5, 12), result.getDateOfBirth());
        assertEquals("X987654", result.getPassportNumber());
        verify(passengerRepository).getPassengerByBookingId(bookingId);
    }

    @Test
    public void getPassengerByBookingId_whenPassengerMissing_throwsValidationException() {
        // Arrange
        int bookingId = 999;

        when(passengerRepository.getPassengerByBookingId(bookingId)).thenReturn(null);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.getPassengerByBookingId(bookingId)
        );

        // Assert
        assertEquals(PASSENGER_NOT_FOUND, exception.getMessage());
        verify(passengerRepository).getPassengerByBookingId(bookingId);
    }

    @Test
    public void updatePassengerInfo_whenInputIsValid_returnsTrue() throws ValidationException {
        // Arrange
        String passengerId = "1";
        String firstName = "John";
        String lastName = "Doe";
        String dob = "2000-01-01";
        String passport = "P123456";

        when(passengerRepository.update(passengerId, firstName, lastName, dob, passport))
                .thenReturn(true);

        // Act
        boolean result = passengerService.updatePassengerInfo(
                passengerId, firstName, lastName, dob, passport
        );

        // Assert
        assertEquals(true, result);
        verify(passengerRepository).update(passengerId, firstName, lastName, dob, passport);
    }

    @Test
    public void updatePassengerInfo_whenRepositoryReturnsFalse_returnsFalse() throws ValidationException {
        // Arrange
        String passengerId = "1";
        String firstName = "John";
        String lastName = "Doe";
        String dob = "2000-01-01";
        String passport = "P123456";

        when(passengerRepository.update(passengerId, firstName, lastName, dob, passport))
                .thenReturn(false);

        // Act
        boolean result = passengerService.updatePassengerInfo(
                passengerId, firstName, lastName, dob, passport
        );

        // Assert
        assertEquals(false, result);
        verify(passengerRepository).update(passengerId, firstName, lastName, dob, passport);
    }

    @Test
    public void updatePassengerInfo_whenFirstNameIsNull_throwsValidationException() {
        // Arrange
        String passengerId = "1";
        String firstName = null;
        String lastName = "Doe";
        String dob = "2000-01-01";
        String passport = "P123456";

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.updatePassengerInfo(
                        passengerId, firstName, lastName, dob, passport
                )
        );

        // Assert
        assertEquals(PASSENGER_NO_FIRSTNAME, exception.getMessage());
        verify(passengerRepository, never()).update(passengerId, firstName, lastName, dob, passport);
    }

    @Test
    public void updatePassengerInfo_whenLastNameIsBlank_throwsValidationException() {
        // Arrange
        String passengerId = "1";
        String firstName = "John";
        String lastName = "";
        String dob = "2000-01-01";
        String passport = "P123456";

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.updatePassengerInfo(
                        passengerId, firstName, lastName, dob, passport
                )
        );

        // Assert
        assertEquals(PASSENGER_NO_LASTNAME, exception.getMessage());
        verify(passengerRepository, never()).update(passengerId, firstName, lastName, dob, passport);
    }

    @Test
    public void updatePassengerInfo_whenDobIsNull_throwsValidationException() {
        // Arrange
        String passengerId = "1";
        String firstName = "John";
        String lastName = "Doe";
        String dob = null;
        String passport = "P123456";

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.updatePassengerInfo(
                        passengerId, firstName, lastName, dob, passport
                )
        );

        // Assert
        assertEquals(PASSENGER_NO_BIRTHDATE, exception.getMessage());
        verify(passengerRepository, never()).update(passengerId, firstName, lastName, dob, passport);
    }

    @Test
    public void updatePassengerInfo_whenPassportIsBlank_throwsValidationException() {
        // Arrange
        String passengerId = "1";
        String firstName = "John";
        String lastName = "Doe";
        String dob = "2000-01-01";
        String passport = "";

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> passengerService.updatePassengerInfo(
                        passengerId, firstName, lastName, dob, passport
                )
        );

        // Assert
        assertEquals(PASSENGER_NO_PNUMBER, exception.getMessage());
        verify(passengerRepository, never()).update(passengerId, firstName, lastName, dob, passport);
    }
}