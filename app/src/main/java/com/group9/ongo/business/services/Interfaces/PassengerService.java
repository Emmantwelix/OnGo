package com.group9.ongo.business.services.Interfaces;

import com.group9.ongo.business.validation.PassengerInputValidator;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;

public interface PassengerService {
    boolean updatePassengerInfo(String passengerId, String firstName, String lastName, String dob, String passport) throws ValidationException;
    Passenger addPassenger(PassengerInput passengerInfo, int bookingId) throws ValidationException;
    Passenger getPassengerByBookingId(int bookingId) throws ValidationException;
}
