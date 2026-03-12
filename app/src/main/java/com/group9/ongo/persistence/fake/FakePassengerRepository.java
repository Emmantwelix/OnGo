package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.persistence.PassengerRepository;

import java.time.LocalDate;
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
                info.getFirstName(),
                info.getLastName(),
                LocalDate.parse(info.getDateOfBirth()),
                info.getPassportNumber()
        );

        passengers.add(newPassenger);
        return newPassenger;
    }

    @Override
    public Passenger getPassengerByBookingId(int bookingId) {

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

    @Override
    public boolean update(String id, String fName, String lName, String dob, String passport) {
        int passengerId = Integer.parseInt(id);
        for (int i = 0; i < passengers.size(); i++) {
            Passenger p = passengers.get(i);
            if (p.getPassengerId() == passengerId) {
                passengers.set(i, new Passenger(
                        passengerId,
                        p.getBookingId(),
                        fName,
                        lName,
                        LocalDate.parse(dob),
                        passport
                ));
                return true;
            }
        }
        return false;
    }


}
