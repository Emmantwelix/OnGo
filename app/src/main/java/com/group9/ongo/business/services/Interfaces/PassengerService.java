package com.group9.ongo.business.services.Interfaces;

public interface PassengerService {
    boolean updatePassengerInfo(String passengerId, String firstName, String lastName, String dob, String passport);
}
