package com.group9.ongo.application;

import android.app.Application;

import com.group9.ongo.business.services.BookingService;
import com.group9.ongo.business.services.BookingServiceImpl;
import com.group9.ongo.business.services.FlightDetailGen;
import com.group9.ongo.business.services.FlightService;
import com.group9.ongo.business.services.FlightServiceImpl;
import com.group9.ongo.business.services.Generator;
import com.group9.ongo.persistence.BookingRepository;
import com.group9.ongo.persistence.FlightRepository;
import com.group9.ongo.persistence.PassengerRepository;
import com.group9.ongo.persistence.fake.FakeBookingRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;
import com.group9.ongo.persistence.fake.FakePassengerRepository;

import java.util.Random;

public class OnGoApp extends Application {

    private FlightService flightService;
    private BookingService bookingService;

    @Override
    public void onCreate() {
        super.onCreate();

        FlightRepository flightRepo = new FakeFlightRepository(true);
        Generator fnGenerator = new FlightDetailGen(new Random());
        flightService = new FlightServiceImpl(flightRepo, fnGenerator);

        BookingRepository bookingRepo = new FakeBookingRepository();
        PassengerRepository passengerRepo = new FakePassengerRepository();
        bookingService = new BookingServiceImpl(bookingRepo, passengerRepo, flightService);
    }

    public FlightService getFlightService() {
        return flightService;
    }

    public BookingService getBookingService() {
        return bookingService;
    }
}