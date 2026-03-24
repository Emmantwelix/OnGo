package com.group9.ongo.business.services.UnitTests;

import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_BOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_EXISTS;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_UNBOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_NOT_FOUND;
import static com.group9.ongo.business.constants.SeatConstants.UNAVAILABLE_SEAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group9.ongo.business.services.Implementations.SeatServiceImpl;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.SeatRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    private SeatService seatService;

    @Before
    public void setUp() {
        seatService = new SeatServiceImpl(seatRepository);
    }

    @Test
    public void getAllSeatsByFlightId_returnsSeatList() {
        // Arrange
        int flightId = 1;
        List<Seat> seats = Arrays.asList(
                seat(1, flightId, 1, "A", false),
                seat(2, flightId, 1, "B", false),
                seat(3, flightId, 1, "C", true)
        );

        when(seatRepository.getSeatsByFlightId(flightId)).thenReturn(seats);

        // Act
        List<Seat> result = seatService.getAllSeatsByFlightId(flightId);

        // Assert
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).getLabel());
        assertEquals("B", result.get(1).getLabel());
        assertEquals("C", result.get(2).getLabel());
        verify(seatRepository).getSeatsByFlightId(flightId);
    }

    @Test
    public void createSeat_whenSeatDoesNotExist_returnsSeatId() throws ValidationException {
        // Arrange
        int flightId = 1;
        int row = 1;
        String column = "A";

        when(seatRepository.createSeat(flightId, row, column)).thenReturn(10);

        // Act
        int result = seatService.createSeat(flightId, row, column);

        // Assert
        assertEquals(10, result);
        verify(seatRepository).createSeat(flightId, row, column);
    }

    @Test
    public void createSeat_whenSeatAlreadyExists_throwsValidationException() {
        // Arrange
        int flightId = 1;
        int row = 1;
        String column = "A";

        when(seatRepository.createSeat(flightId, row, column)).thenReturn(-1);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.createSeat(flightId, row, column)
        );

        // Assert
        assertEquals(SEAT_ALREADY_EXISTS, exception.getMessage());
        verify(seatRepository).createSeat(flightId, row, column);
    }

    @Test
    public void createSeats_createsSeatsForCapacity() throws ValidationException {
        // Arrange
        int flightId = 1;
        int capacity = 8;

        when(seatRepository.createSeat(eq(flightId), anyInt(), anyString())).thenReturn(1);

        // Act
        seatService.createSeats(flightId, capacity);

        // Assert
        verify(seatRepository, times(8))
                .createSeat(eq(flightId), anyInt(), anyString());
    }

    @Test
    public void findSeat_whenSeatExists_returnsSeat() throws ValidationException {
        // Arrange
        int flightId = 1;
        int row = 1;
        String column = "A";
        Seat seat = seat(5, flightId, row, column, false);

        when(seatRepository.findSeat(flightId, row, column)).thenReturn(seat);

        // Act
        Seat result = seatService.findSeat(flightId, row, column);

        // Assert
        assertEquals(5, result.getSeatId());
        assertEquals(flightId, result.getFlightId());
        assertEquals(row, result.getSeatRow());
        assertEquals(column, result.getLabel());
        verify(seatRepository).findSeat(flightId, row, column);
    }

    @Test
    public void findSeat_whenSeatMissing_throwsValidationException() {
        // Arrange
        int flightId = 1;
        int row = 100;
        String column = "Z";

        when(seatRepository.findSeat(flightId, row, column)).thenReturn(null);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.findSeat(flightId, row, column)
        );

        // Assert
        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
        verify(seatRepository).findSeat(flightId, row, column);
    }

    @Test
    public void getSeatById_whenSeatExists_returnsSeat() throws ValidationException {
        // Arrange
        int flightId = 1;
        int seatId = 5;
        Seat seat = seat(seatId, flightId, 1, "A", false);

        when(seatRepository.getSeatById(flightId, seatId)).thenReturn(seat);

        // Act
        Seat result = seatService.getSeatById(flightId, seatId);

        // Assert
        assertEquals(seatId, result.getSeatId());
        assertEquals(flightId, result.getFlightId());
        assertEquals(1, result.getSeatRow());
        assertEquals("A", result.getLabel());
        verify(seatRepository).getSeatById(flightId, seatId);
    }

    @Test
    public void getSeatById_whenSeatMissing_throwsValidationException() {
        // Arrange
        int flightId = 1;
        int seatId = 999;

        when(seatRepository.getSeatById(flightId, seatId)).thenReturn(null);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.getSeatById(flightId, seatId)
        );

        // Assert
        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
        verify(seatRepository).getSeatById(flightId, seatId);
    }

    @Test
    public void bookSeat_whenSeatExistsAndAvailable_booksSeatAndReturnsSeatId() throws ValidationException {
        // Arrange
        int flightId = 1;
        int row = 1;
        String column = "A";
        int seatId = 5;
        Seat seat = seat(seatId, flightId, row, column, false);

        when(seatRepository.findSeat(flightId, row, column)).thenReturn(seat);

        // Act
        int result = seatService.bookSeat(flightId, row, column);

        // Assert
        assertEquals(seatId, result);
        verify(seatRepository).findSeat(flightId, row, column);
        verify(seatRepository).bookSeat(seatId);
    }

    @Test
    public void bookSeat_whenSeatMissing_throwsValidationException() {
        // Arrange
        int flightId = 1;
        int row = 100;
        String column = "Z";

        when(seatRepository.findSeat(flightId, row, column)).thenReturn(null);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.bookSeat(flightId, row, column)
        );

        // Assert
        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
        verify(seatRepository).findSeat(flightId, row, column);
        verify(seatRepository, never()).bookSeat(anyInt());
    }

    @Test
    public void bookSeat_whenSeatAlreadyBooked_throwsValidationException() {
        // Arrange
        int flightId = 1;
        int row = 1;
        String column = "A";
        int seatId = 5;
        Seat seat = seat(seatId, flightId, row, column, true);

        when(seatRepository.findSeat(flightId, row, column)).thenReturn(seat);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.bookSeat(flightId, row, column)
        );

        // Assert
        assertEquals(SEAT_ALREADY_BOOKED, exception.getMessage());
        verify(seatRepository).findSeat(flightId, row, column);
        verify(seatRepository, never()).bookSeat(anyInt());
    }

    @Test
    public void unbookSeat_whenSeatExistsAndBooked_unbooksSeat() throws ValidationException {
        // Arrange
        int flightId = 1;
        int seatId = 5;
        Seat seat = seat(seatId, flightId, 1, "A", true);

        when(seatRepository.getSeatById(flightId, seatId)).thenReturn(seat);

        // Act
        seatService.unbookSeat(flightId, seatId);

        // Assert
        verify(seatRepository).getSeatById(flightId, seatId);
        verify(seatRepository).unBookSeat(seatId);
    }

    @Test
    public void unbookSeat_whenSeatMissing_throwsValidationException() {
        // Arrange
        int flightId = 1;
        int seatId = 999;

        when(seatRepository.getSeatById(flightId, seatId)).thenReturn(null);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.unbookSeat(flightId, seatId)
        );

        // Assert
        assertEquals(SEAT_NOT_FOUND, exception.getMessage());
        verify(seatRepository).getSeatById(flightId, seatId);
        verify(seatRepository, never()).unBookSeat(anyInt());
    }

    @Test
    public void unbookSeat_whenSeatAlreadyUnbooked_throwsValidationException() {
        // Arrange
        int flightId = 1;
        int seatId = 5;
        Seat seat = seat(seatId, flightId, 1, "A", false);

        when(seatRepository.getSeatById(flightId, seatId)).thenReturn(seat);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.unbookSeat(flightId, seatId)
        );

        // Assert
        assertEquals(SEAT_ALREADY_UNBOOKED, exception.getMessage());
        verify(seatRepository).getSeatById(flightId, seatId);
        verify(seatRepository, never()).unBookSeat(anyInt());
    }

    @Test
    public void getFormattedSeatById_whenSeatExists_returnsFormattedSeat() {
        // Arrange
        int flightId = 1;
        int seatId = 5;
        Seat seat = seat(seatId, flightId, 1, "A", false);

        when(seatRepository.getSeatById(flightId, seatId)).thenReturn(seat);

        // Act
        String result = seatService.getFormattedSeatById(flightId, seatId);

        // Assert
        assertEquals("1A", result);
        verify(seatRepository).getSeatById(flightId, seatId);
    }

    @Test
    public void getFormattedSeatById_whenSeatMissing_returnsUnavailableSeat() {
        // Arrange
        int flightId = 1;
        int seatId = 999;

        when(seatRepository.getSeatById(flightId, seatId)).thenReturn(null);

        // Act
        String result = seatService.getFormattedSeatById(flightId, seatId);

        // Assert
        assertEquals(UNAVAILABLE_SEAT, result);
        verify(seatRepository).getSeatById(flightId, seatId);
    }

    private Seat seat(int seatId, int flightId, int row, String column, boolean isBooked) {
        return new Seat(seatId, flightId, row, column, isBooked);
    }
}