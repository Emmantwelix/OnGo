package com.group9.ongo.persistence.fake;


import com.group9.ongo.models.Booking;
import com.group9.ongo.persistence.BookingRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeBookingRepository implements BookingRepository {

    private List<Booking> bookings = new ArrayList<>();
    private int nextId = 1;

    @Override
    public List<Booking> getBookingByUserId(int userId) {
        List<Booking> userBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getUserId() == userId) {
                userBookings.add(booking);
            }
        }

        return userBookings;
    }


    @Override
    public Booking getBookingById(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    @Override
    public Booking addBooking(Booking booking) {
        int id = booking.getBookingId();

        if (id == 0) {
            id = nextId++;
        } else if (getBookingById(id) != null) {
            // booking already exists
            return null;
        }

        Booking newBooking = new Booking(id, booking.getUserId(), booking.getFlightId(), booking.getSeatId());
        bookings.add(newBooking);
        return newBooking;

    }


    @Override
    public boolean deleteBooking(int bookingId) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId() == bookingId) {
                bookings.remove(i);
                return true;
            }
        }
        return false;
    }

}
