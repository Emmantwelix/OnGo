package com.group9.ongo.business.services.Interfaces;

import com.group9.ongo.business.services.BookingException;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.PassengerInput;

import java.util.List;

public interface BookingService {
    Booking createBooking(int flightId, PassengerInput passengerInfo, int seatRow, String seatColumn) throws BookingException, ValidationException;

    boolean cancelBooking(int bookingId) throws ValidationException;

    List<BookingDetails> getBookingDetailsForCurrentUser();
}
