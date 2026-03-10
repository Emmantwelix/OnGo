package com.group9.ongo.business.services.Implementations;

import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_BOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_EXISTS;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_UNBOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_NOT_FOUND;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_1;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_2;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_3;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_4;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_5;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_6;
import static com.group9.ongo.business.constants.FlightConstants.MAX_COLUMNS;

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
    public void createSeats(int flightId, int capacity) throws ValidationException {
        String letter = " ";
        int amountRows = capacity / MAX_COLUMNS;
        int extraRow = capacity % MAX_COLUMNS;

        for (int i = 0; i < amountRows; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if (j == 0) {
                    letter = COLUMN_1;
                } else if (j == 1) {
                    letter = COLUMN_2;
                } else if (j == 2) {
                    letter = COLUMN_3;
                } else if (j == 3) {
                    letter = COLUMN_4;
                } else if (j == 4) {
                    letter = COLUMN_5;
                } else {
                    letter = COLUMN_6;
                }
                this.createSeat(flightId, i + 1, letter);
            }
        }

        //extra row
        for (int j = 0; j < extraRow; j++) {
            if (j == 0) {
                letter = COLUMN_1;
            } else if (j == 1) {
                letter = COLUMN_2;
            } else if (j == 2) {
                letter = COLUMN_3;
            } else if (j == 3) {
                letter = COLUMN_4;
            } else {
                letter = COLUMN_5;

            }
            this.createSeat(flightId, amountRows+1, letter);
        }
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
    public int bookSeat(int flight_id, int seatRow, String seatColumn) throws ValidationException {
        Seat seat = findSeat(flight_id, seatRow, seatColumn);
        int seatId = seat.getSeatId();

        if (seat.getIsBooked()) {
            throw new ValidationException(SEAT_ALREADY_BOOKED);
        }

        seatRepository.bookSeat(seatId);

        return seatId;
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
    }


}
