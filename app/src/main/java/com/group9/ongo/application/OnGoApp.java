package com.group9.ongo.application;

import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_EMAIL;
import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_NAME;

import android.app.Application;

import com.group9.ongo.business.services.Implementations.LoginServiceImpl;
import com.group9.ongo.business.services.Interfaces.BookingService;
import com.group9.ongo.business.services.Implementations.BookingServiceImpl;
import com.group9.ongo.business.services.Implementations.FlightDetailGen;
import com.group9.ongo.business.services.Interfaces.FlightService;
import com.group9.ongo.business.services.Implementations.FlightServiceImpl;
import com.group9.ongo.business.services.Interfaces.Generator;
import com.group9.ongo.business.services.Interfaces.LoginService;
import com.group9.ongo.business.services.Interfaces.PassengerService;
import com.group9.ongo.business.services.Implementations.PassengerServiceImpl;
import com.group9.ongo.business.services.Interfaces.SeatService;
import com.group9.ongo.business.services.Implementations.SeatServiceImpl;
import com.group9.ongo.persistence.AircraftRepository;
import com.group9.ongo.persistence.BookingRepository;
import com.group9.ongo.persistence.FlightRepository;
import com.group9.ongo.persistence.PassengerRepository;
import com.group9.ongo.persistence.SeatRepository;
import com.group9.ongo.persistence.UserRepository;
import com.group9.ongo.persistence.fake.FakeAircraftRepository;
import com.group9.ongo.persistence.fake.FakeBookingRepository;
import com.group9.ongo.persistence.fake.FakeFlightRepository;
import com.group9.ongo.persistence.fake.FakePassengerRepository;
import com.group9.ongo.persistence.fake.FakeSeatsRepository;
import com.group9.ongo.persistence.fake.FakeUserRepository;
import com.group9.ongo.persistence.real.AppDbHelper;
import com.group9.ongo.persistence.real.SqlAircraftRepository;
import com.group9.ongo.persistence.real.SqlBookingRepository;
import com.group9.ongo.persistence.real.SqlFlightRepository;
import com.group9.ongo.persistence.real.SqlPassengerRepository;
import com.group9.ongo.persistence.real.SqlSeatRepository;
import com.group9.ongo.persistence.real.SqlUserRepository;

import java.util.Random;

public class OnGoApp extends Application {

    private FlightService flightService;
    private BookingService bookingService;
    private SeatService seatService;
    private PassengerService passengerService;

    @Override
    public void onCreate() {
        super.onCreate();

        AppDbHelper dbHelper = new AppDbHelper(getApplicationContext());
        boolean USE_SQL = true;

        Generator fnGenerator = new FlightDetailGen(new Random());

        SeatRepository seatRepository = USE_SQL ? new SqlSeatRepository(dbHelper) : new FakeSeatsRepository(true);
        seatService = new SeatServiceImpl(seatRepository);

        AircraftRepository aircraftRepository = USE_SQL ? new SqlAircraftRepository(dbHelper) : new FakeAircraftRepository();
        FlightRepository flightRepository = USE_SQL ? new SqlFlightRepository(dbHelper) : new FakeFlightRepository(true);

        flightService = new FlightServiceImpl(flightRepository, fnGenerator, seatService, aircraftRepository);

        UserRepository userRepository = USE_SQL ? new SqlUserRepository(dbHelper) : new FakeUserRepository();

        //simulate a fake login
        LoginService loginService = new LoginServiceImpl(userRepository);
        int userId  = loginService.login(SAMPLE_USER_NAME, SAMPLE_USER_EMAIL);

        BookingRepository bookingRepo = USE_SQL ? new SqlBookingRepository(dbHelper): new FakeBookingRepository();
        PassengerRepository passengerRepo = USE_SQL ? new SqlPassengerRepository(dbHelper): new FakePassengerRepository();
        passengerService = new PassengerServiceImpl(passengerRepo);
        bookingService = new BookingServiceImpl(userId, bookingRepo, passengerService, flightService, seatService);
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

    public PassengerService getPassengerService() {
        return passengerService;
    }
}
