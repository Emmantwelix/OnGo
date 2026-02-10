package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.persistence.PassengerRepository;

import java.util.ArrayList;
import java.util.List;

public class FakePassengerRepository implements PassengerRepository {

    private List<Passenger> passengers = new ArrayList<>();
    private int nextId = 1;

    @Override
    public Passenger addPassenger(PassengerInput info, int bookingId) {
        if (info == null) return null;

        int id = nextId++;

        Passenger newPassenger = new Passenger(
                id,
                bookingId,
                info.firstName,
                info.lastName,
                info.dateOfBirth,
                info.passportNumber
        );

        passengers.add(newPassenger);
        return newPassenger;
    }

    @Override
    public Passenger getPassengerByBookingId (int bookingId) {

        for (Passenger passenger : passengers) {
            if (passenger.getBookingId() == bookingId) {
                return passenger;
            }
        }

        return null;
    }

    @Override
    public boolean deletePassengersByBookingId(int bookingId) {
        return passengers.removeIf(p -> p.getBookingId() == bookingId);
    }



}
