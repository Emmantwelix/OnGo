package com.group9.ongo.business.services;

import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.PassengerInput;

import java.util.List;

public interface BookingService {
    public List<Booking> getBookingByUserId(int userId);
    public Booking createBooking(int userId, int flightId, PassengerInput passengerInfo) throws BookingException, ValidationException;

    public boolean cancelBooking(int bookingId);

    public List<BookingDetails> getBookingDetailsByUserId(int userId);
}
