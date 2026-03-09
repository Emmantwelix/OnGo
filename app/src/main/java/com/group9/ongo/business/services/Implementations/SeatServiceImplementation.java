package com.group9.ongo.business.services.Implementations;

import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_BOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_EXISTS;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_UNBOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_NOT_FOUND;

import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.SeatRepository;

import java.util.List;

public class SeatServiceImplementation  implements SeatService {
    private final SeatRepository seatRepository;

    public SeatServiceImplementation(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public List<Seat> getAllSeatsByFlightId(int flight_id) {
        return seatRepository.getSeatsByFlightId(flight_id);
    }

    @Override
    public int createSeat(int flight_id, int row, String column) throws ValidationException {
        int seatId = seatRepository.createSeat(flight_id, row, column);
        if (seatId == -1) {
            throw new ValidationException(SEAT_ALREADY_EXISTS);
        }
        return seatId;
    }

    @Override
    public Seat findSeat(int flight_id, int seatRow, String seatColumn) throws ValidationException
    {
        Seat seat = seatRepository.findSeat(flight_id, seatRow, seatColumn);
        if (seat == null) {
            throw new ValidationException(SEAT_NOT_FOUND);
        }
        return seat;
    }
    @Override
    public Seat getSeatById(int flight_id, int seat_id) throws ValidationException {
        Seat seat = seatRepository.getSeatById(flight_id, seat_id);
        if (seat == null) {
            throw new ValidationException(SEAT_NOT_FOUND);
        }
        return seat;
    }

    @Override
    public void bookSeat(int flight_id, int seat_id) throws ValidationException {
        Seat seat = seatRepository.getSeatById(flight_id, seat_id);
        if (seat == null) {
            throw new ValidationException(SEAT_NOT_FOUND);
        }
        if (seat.getIsBooked()) {
            throw new ValidationException(SEAT_ALREADY_BOOKED);
        }

        seat.bookSeat();
    }

    @Override
    public void unbookSeat(int flight_id, int seat_id) throws ValidationException {
        Seat seat = seatRepository.getSeatById(flight_id, seat_id);
        if (seat == null) {
            throw new ValidationException(SEAT_NOT_FOUND);
        }
        if (!seat.getIsBooked()) {
            throw new ValidationException(SEAT_ALREADY_UNBOOKED);
        }
        seat.unbookSeat();
    }

    @Override
    public boolean isSeatBooked(int flight_id, int seat_id) throws ValidationException {
        Seat seat = seatRepository.getSeatById(flight_id, seat_id);
        if (seat == null) {
            throw new ValidationException(SEAT_NOT_FOUND);
        }
        return seat.getIsBooked();
    }
}
