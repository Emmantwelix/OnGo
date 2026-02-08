package com.group9.ongo.persistence;

import com.group9.ongo.models.Booking;

import java.util.List;

public interface BookingRepository {

    public List<Booking> getBookingByUserId(int userId);

    public Booking addBooking(Booking booking);

    public boolean updateBooking(Booking booking);

    public Booking getBookingById(int bookingId);

    public boolean deleteBooking(int bookingId);
}
