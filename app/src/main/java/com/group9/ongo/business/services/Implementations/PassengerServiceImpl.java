package com.group9.ongo.business.services.Implementations;

import com.group9.ongo.business.services.Interfaces.PassengerService;
import com.group9.ongo.persistence.PassengerRepository;

public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public boolean updatePassengerInfo(String id, String fName, String lName, String dob, String passport) {
        // Validation logic
        if (fName == null || fName.trim().isEmpty() || lName == null || lName.trim().isEmpty()) {
            return false;
        }

        // Call repository to update database
        return passengerRepository.update(id, fName, lName, dob, passport);
    }
}
