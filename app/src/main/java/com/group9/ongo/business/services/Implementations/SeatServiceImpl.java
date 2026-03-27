package com.group9.ongo.business.services.Implementations;

import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_BOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_EXISTS;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_ALREADY_UNBOOKED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.SEAT_NOT_FOUND;
import static com.group9.ongo.business.constants.SeatConstants.UNAVAILABLE_SEAT;

import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Seat;
import com.group9.ongo.models.SeatMapConfig;
import com.group9.ongo.persistence.SeatRepository;

import java.util.List;

public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
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
        SeatMapConfig config = SeatMapService.createFromCapacity(capacity);
        List<Seat> generatedSeats = SeatMapService.generateSeats(config);

        for (Seat seat : generatedSeats) {
            if (seat.getType() == Seat.Type.SEAT) {
                this.createSeat(flightId, seat.getRow(), seat.getLabel());
            }
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

        seatRepository.unBookSeat(seat_id);
    }

    @Override
    public String getFormattedSeatById(int flight_id, int seatId)
    {
        try{
            Seat seat = getSeatById(flight_id, seatId);
            return seat.getSeatRow() + seat.getLabel();
        } catch (ValidationException e)
        {
            return UNAVAILABLE_SEAT;
        }
    }

    @Override
    public SeatMapConfig getSeatMapConfiguration(int capacity)
    {
        return SeatMapService.createFromCapacity(capacity);
    }

    @Override
    public List<Seat> getSeatsForDisplay(int flightId, SeatMapConfig config)
    {
        // 1. Generate the standard map grid for a configuration
        List<Seat> gridSeats = SeatMapService.generateSeats(config);

        // 2. Fetch actual booked status from the seatService
        List<Seat> realSeats = getAllSeatsByFlightId(flightId);

        // 3. Mark matching seats as OCCUPIED in our grid
        SeatMapService.applyBookedSeats(gridSeats,realSeats);

        return gridSeats;
    }
}
