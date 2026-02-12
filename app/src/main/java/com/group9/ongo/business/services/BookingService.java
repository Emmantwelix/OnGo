package com.group9.ongo.business.services;

import com.group9.ongo.business.validation.BookingValidator;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.persistence.BookingRepository;
import com.group9.ongo.persistence.PassengerRepository;

import java.util.ArrayList;
import java.util.List;

public class BookingService {

    private BookingRepository bookingsRepo;
    private PassengerRepository passengerRepo;

    private FlightService flightService;

    public BookingService(BookingRepository bookingsRepo, PassengerRepository passengerRepo,
                          FlightService flightService) {
        this.bookingsRepo = bookingsRepo;
        this.passengerRepo = passengerRepo;
        this.flightService = flightService;
    }

    public List<Booking> getBookingByUserId(int userId) {
        return bookingsRepo.getBookingByUserId(userId);
    }

    public Booking createBooking(int userId, int flightId, PassengerInput passengerInfo) {
        BookingValidator.validate(flightService.getFlightById(flightId), flightId, passengerInfo);
        Booking booking = bookingsRepo.addBooking(new Booking(0, userId, flightId));
        Passenger passenger = passengerRepo.addPassenger(passengerInfo, booking.getBookingId());

        if (passenger == null) {
            bookingsRepo.deleteBooking(booking.getBookingId());
            throw new RuntimeException("Failed to create passenger. Booking has been rolled back.");
        }

        return booking;
    }

    public boolean cancelBooking(int bookingId) {
        passengerRepo.deletePassengersByBookingId(bookingId);
        return bookingsRepo.deleteBooking(bookingId);
    }

    public List<BookingDetails> getBookingDetailsByUserId(int userId) {
        List<Booking> bookings = bookingsRepo.getBookingByUserId(userId);
        List<BookingDetails> detailsList = new ArrayList<>();

        for (Booking booking : bookings) {
            Flight flight = flightService.getFlightById(booking.getFlightId());

            //only one passenger per flight for now
            Passenger passenger =
                    passengerRepo.getPassengerByBookingId(booking.getBookingId());

            if (flight != null && passenger != null) {
                detailsList.add(
                        new BookingDetails(booking, flight, passenger)
                );
            }
        }
        return detailsList;
    }


}
