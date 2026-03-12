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

    private int currentUserId;

    public BookingServiceImpl( int currentUserId,BookingRepository bookingsRepo, PassengerRepository passengerRepo,
                              FlightService flightService, SeatService seatService) {
        this.bookingsRepo = bookingsRepo;
        this.passengerRepo = passengerRepo;
        this.flightService = flightService;
        this.seatService = seatService;
        this.currentUserId = currentUserId;
    }

    @Override
    public Booking createBooking(int flightId, PassengerInput passengerInfo, int seatRow, String seatColumn) throws BookingException, ValidationException {
        BookingValidator.validate(flightService.getFlightById(flightId), passengerInfo);

        //throws exception if seat is already booked or not found
        int seatId = seatService.bookSeat(flightId, seatRow, seatColumn);

        Booking booking = bookingsRepo.addBooking(new Booking(0, currentUserId, flightId, seatId));
        Passenger passenger = passengerRepo.addPassenger(passengerInfo, booking.getBookingId());

        if (passenger == null) {
            bookingsRepo.deleteBooking(booking.getBookingId());
            seatService.unbookSeat(flightId, seatId);
            throw new BookingException(BOOKING_PASSENGER_ERROR);
        }

        flightService.isFlightFull(flightId);

        return booking;
    }
    @Override
    public boolean cancelBooking(int bookingId) throws ValidationException {
        Booking booking = bookingsRepo.getBookingById(bookingId);

        if(booking == null) return false;

        passengerRepo.deletePassengersByBookingId(bookingId);

        Seat seat = seatService.getSeatById(booking.getFlightId(), booking.getSeatId());

        seatService.unbookSeat(seat.getFlightId(), seat.getSeatId());

        flightService.isFlightFull(seat.getFlightId());

        return bookingsRepo.deleteBooking(bookingId);
    }

    @Override
    public List<BookingDetails> getBookingDetailsForCurrentUser() {
        List<Booking> bookings = bookingsRepo.getBookingByUserId(currentUserId);
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

    @Override
    public BookingDetails getBookingDetailsById(int bookingId) {
        Booking booking = bookingsRepo.getBookingById(bookingId);

        if (booking == null) {
            return null;
        }

        try {
            Flight flight = flightService.getFlightById(booking.getFlightId());
            Passenger passenger = passengerRepo.getPassengerByBookingId(bookingId);

            if (passenger == null) {
                return null;
            }

            return new BookingDetails(booking, flight, passenger);

        } catch (ValidationException e) {
            // If flight validation fails (e.g. flight not found), skip this booking details
            return null;
        }
    }

}
