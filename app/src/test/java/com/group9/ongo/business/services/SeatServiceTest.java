package com.group9.ongo.business.services;

import static com.group9.ongo.business.constants.FlightConstants.A320_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.COLUMN_1;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_TIME;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_TIME2;
import static com.group9.ongo.business.constants.FlightConstants.LARGE_PRICE;
import static com.group9.ongo.business.constants.FlightConstants.ROW_ONE;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.group9.ongo.business.services.Implementations.FlightDetailGen;
import com.group9.ongo.business.services.Implementations.FlightServiceImpl;
import com.group9.ongo.business.services.Implementations.SeatServiceImplementation;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.persistence.SeatRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;
import com.group9.ongo.persistence.fake.FakeSeatsRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class SeatServiceTest {
    private FlightService flightService;
    private SeatService seatService;
    private Generator fdgen;
    private SeatRepository seatRepository;

    @Before
    public void setup() {
        seatRepository = new FakeSeatsRepository();
        fdgen = new FlightDetailGen(new Random());
        seatService = new SeatServiceImplementation(seatRepository);
        flightService = new FlightServiceImpl(new FakeFlightRepository(), fdgen, seatService);
    }

    @Test
    public void testCreateSeat_throwsValidationException_whenSeatAlreadyExists() throws ValidationException {
        int flightId = flightService.createFlight(AIR_CANADA, WINNIPEG, TORONTO, DEFAULT_TIME, DEFAULT_TIME2, A320_DETAILS, LARGE_PRICE);
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> seatService.createSeat(flightId, ROW_ONE, COLUMN_1)
        );

        assertEquals("Seat already exists for this flight", exception.getMessage());
    }
}
