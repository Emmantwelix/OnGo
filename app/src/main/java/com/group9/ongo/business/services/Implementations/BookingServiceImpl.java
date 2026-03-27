package com.group9.ongo.business.services.Implementations;

import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_CREATION_FAILED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_PASSENGER_ERROR;
import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_UPDATE_FAILED;

import com.group9.ongo.business.services.BookingException;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.PassengerService;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.BookingValidator;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Booking;
import com.group9.ongo.models.BookingDetails;
import com.group9.ongo.models.BookingStatus;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.BookingRepository;

import java.util.ArrayList;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingsRepo;
    private PassengerService passengerService;

    private FlightService flightService;
    private SeatService seatService;

    private int currentUserId;

    public BookingServiceImpl( int currentUserId,BookingRepository bookingsRepo, PassengerService passengerService,
                               FlightService flightService, SeatService seatService) {
        this.bookingsRepo = bookingsRepo;
        this.passengerService = passengerService;
        this.flightService = flightService;
        this.seatService = seatService;
        this.currentUserId = currentUserId;
    }

    @Override
    public Booking createBooking(int flightId, PassengerInput passengerInfo, int seatRow, String seatColumn)
            throws BookingException, ValidationException {

        BookingValidator.validateBookingFields(flightService.getFlightById(flightId), passengerInfo);

        // throws exception if seat already booked
        int seatId = seatService.bookSeat(flightId, seatRow, seatColumn);

        Booking booking = bookingsRepo.addBooking(new Booking(0, currentUserId, flightId, seatId));

        if (booking == null) {
            // rollback seat reservation
            seatService.unbookSeat(flightId, seatId);
            throw new BookingException(BOOKING_CREATION_FAILED);
        }

        try {
            passengerService.addPassenger(passengerInfo, booking.getBookingId());
        } catch (ValidationException e) {

            // rollback booking + seat
            bookingsRepo.deleteBooking(booking.getBookingId());
            seatService.unbookSeat(flightId, seatId);

            throw new BookingException(BOOKING_PASSENGER_ERROR);
        }

        flightService.updateFlightAvailability(flightId);

        return booking;
    }

    @Override
    public void cancelBooking(int bookingId) throws ValidationException {
        Booking booking = getBookingById(bookingId);

        updateBookingStatusToCancelled(bookingId);

        Seat seat = seatService.getSeatById(booking.getFlightId(), booking.getSeatId());
        seatService.unbookSeat(seat.getFlightId(), seat.getSeatId());
        flightService.updateFlightAvailability(seat.getFlightId());
    }

    @Override
    public List<BookingDetails> getBookingDetailsForCurrentUser() {
        return getBookingDetailsByStatus(BookingStatus.UPCOMING);
    }

    @Override
    public List<BookingDetails> getCancelledBookingDetailsForCurrentUser() {
        return getBookingDetailsByStatus(BookingStatus.CANCELLED);
    }

    private List<BookingDetails> getBookingDetailsByStatus(BookingStatus status)
    {
        List<Booking> bookings = bookingsRepo.getBookingByUserId(currentUserId);
        List<BookingDetails> detailsList = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getStatus() == status) {
                try {
                    BookingDetails details = getDetailsForBooking(booking);
                    detailsList.add(details);
                } catch (ValidationException e) {
                    // Skip this booking if related details cannot be loaded
                }
            }
        }

        return detailsList;
    }
    @Override
    public BookingDetails getBookingDetailsById(int bookingId) throws ValidationException {
        Booking booking = getBookingById(bookingId);

        return getDetailsForBooking(booking);
    }

    private BookingDetails getDetailsForBooking(Booking booking) throws ValidationException {
            Flight flight = flightService.getFlightById(booking.getFlightId());
            Passenger passenger = passengerService.getPassengerByBookingId(booking.getBookingId());
            String formattedSeat = seatService.getFormattedSeatById(flight.getFlightId(), booking.getSeatId());
            return new BookingDetails(booking, flight, passenger, formattedSeat);
    }

    private Booking getBookingById(int bookingId) throws ValidationException
    {
        Booking booking = bookingsRepo.getBookingById(bookingId);
        BookingValidator.validateBooking(booking);
        return booking;
    }

    private void updateBookingStatusToCancelled(int bookingId) throws ValidationException
    {
        boolean updated = bookingsRepo.updateBookingStatus(bookingId, BookingStatus.CANCELLED);

        if(!updated){
            throw new ValidationException(BOOKING_UPDATE_FAILED);
        }

    }

}
