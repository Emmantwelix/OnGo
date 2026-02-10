package com.group9.ongo.persistence;

import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;

public interface PassengerRepository {
    public Passenger addPassenger(PassengerInput info, int bookingId);

    public Passenger getPassengerByBookingId(int bookingId);

    public boolean deletePassengersByBookingId(int bookingId);
}
