package com.group9.ongo.business.services.Interfaces;

import com.group9.ongo.business.validation.ValidationException;

public interface PassengerService {
    boolean updatePassengerInfo(String passengerId, String firstName, String lastName, String dob, String passport) throws ValidationException;
}
