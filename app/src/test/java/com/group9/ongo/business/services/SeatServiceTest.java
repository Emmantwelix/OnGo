package com.group9.ongo.business.services;

import com.group9.ongo.business.services.Implementations.FlightDetailGen;
import com.group9.ongo.business.services.Implementations.FlightServiceImpl;
import com.group9.ongo.business.services.Implementations.SeatServiceImplementation;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.persistence.SeatRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;
import com.group9.ongo.persistence.fake.FakeSeatsRepository;

import org.junit.Before;

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



}
