package com.group9.ongo.application;

import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_EMAIL;
import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_NAME;

import android.app.Application;

import com.group9.ongo.business.services.Implementations.AircraftServiceImpl;
import com.group9.ongo.business.services.Implementations.LoginServiceImpl;
import com.group9.ongo.business.services.Interfaces.AircraftService;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Implementations.BookingServiceImpl;
import com.group9.ongo.business.services.Implementations.FlightDetailGen;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Implementations.FlightServiceImpl;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.LoginService;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.services.Implementations.SeatServiceImplementation;
import com.group9.ongo.persistence.BookingRepository;
import com.group9.ongo.persistence.FlightRepository;
import com.group9.ongo.persistence.PassengerRepository;
import com.group9.ongo.persistence.fake.FakeAircraftRepository;
import com.group9.ongo.persistence.fake.FakeBookingRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;
import com.group9.ongo.persistence.fake.FakePassengerRepository;
import com.group9.ongo.persistence.fake.FakeSeatsRepository;
import com.group9.ongo.persistence.fake.FakeUserRepository;

import java.util.Random;

public class OnGoApp extends Application {

    private FlightService flightService;
    private BookingService bookingService;
    private SeatService seatService;

    @Override
    public void onCreate() {
        super.onCreate();

        FlightRepository flightRepo = new FakeFlightRepository(true);
        Generator fnGenerator = new FlightDetailGen(new Random());
        seatService = new SeatServiceImplementation(new FakeSeatsRepository(true));

        AircraftService aircraftService = new AircraftServiceImpl(new FakeAircraftRepository());
        flightService = new FlightServiceImpl(flightRepo, fnGenerator, seatService, aircraftService);

        //simulate a fake login
        LoginService loginService = new LoginServiceImpl(new FakeUserRepository());
        int userId  = loginService.login(SAMPLE_USER_NAME, SAMPLE_USER_EMAIL);

        BookingRepository bookingRepo = new FakeBookingRepository();
        PassengerRepository passengerRepo = new FakePassengerRepository();
        bookingService = new BookingServiceImpl(userId, bookingRepo, passengerRepo, flightService, seatService);
    }

    public FlightService getFlightService() {
        return flightService;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public SeatService getSeatService(){
        return seatService;
    }
}