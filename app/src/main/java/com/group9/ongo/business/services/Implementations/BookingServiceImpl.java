package com.group9.ongo.business.services.Implementations;

import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_PASSENGER_ERROR;

import com.group9.ongo.business.services.BookingException;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.BookingValidator;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.BookingRepository;
import com.group9.ongo.persistence.PassengerRepository;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingsRepo;
    private PassengerRepository passengerRepo;

    private FlightService flightService;
    private SeatService seatService;


    public BookingServiceImpl(BookingRepository bookingsRepo, PassengerRepository passengerRepo,
                              FlightService flightService, SeatService seatService) {
        this.bookingsRepo = bookingsRepo;
        this.passengerRepo = passengerRepo;
        this.flightService = flightService;
        this.seatService = seatService;
    }

    public List<Booking> getBookingByUserId(int userId) {
        return bookingsRepo.getBookingByUserId(userId);
    }

    public Booking createBooking(int userId, int flightId, PassengerInput passengerInfo, int seatRow, String seatColumn) throws BookingException, ValidationException {
        BookingValidator.validate(flightService.getFlightById(flightId), passengerInfo);

        Seat seat = seatService.findSeat(flightId, seatRow, seatColumn);
        Booking booking = bookingsRepo.addBooking(new Booking(0, userId, flightId, seat.getSeatId()));
        Passenger passenger = passengerRepo.addPassenger(passengerInfo, booking.getBookingId());
        seatService.bookSeat(flightId, seat.getSeatId());

        if (passenger == null) {
            bookingsRepo.deleteBooking(booking.getBookingId());
            seatService.unbookSeat(flightId, seat.getSeatId());
            throw new RuntimeException(BOOKING_PASSENGER_ERROR);
        }

        return booking;
    }

    public boolean cancelBooking(int bookingId) throws ValidationException {
        Booking booking = bookingsRepo.getBookingById(bookingId);

        if(booking == null) return false;

        passengerRepo.deletePassengersByBookingId(bookingId);

        Seat seat = seatService.getSeatById(booking.getFlightId(), booking.getSeatId());

        seatService.unbookSeat(seat.getFlightId(), seat.getSeatId());

        return bookingsRepo.deleteBooking(bookingId);
    }

    public List<BookingDetails> getBookingDetailsByUserId(int userId) {
        List<Booking> bookings = bookingsRepo.getBookingByUserId(userId);
        List<BookingDetails> detailsList = new ArrayList<>();

        for (Booking booking : bookings) {
            try {
                Flight flight = flightService.getFlightById(booking.getFlightId());

                //only one passenger per flight for now
                Passenger passenger =
                        passengerRepo.getPassengerByBookingId(booking.getBookingId());

                if (flight != null && passenger != null) {
                    detailsList.add(
                            new BookingDetails(booking, flight, passenger)
                    );
                }
            } catch (ValidationException e) {
                // If flight validation fails (e.g. flight not found), skip this booking details
            }
        }
        return detailsList;
    }
}
