package com.group9.ongo.business.services.Implementations;

import com.group9.ongo.business.services.Interfaces.PassengerService;
import com.group9.ongo.business.validation.PassengerInputValidator;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.persistence.PassengerRepository;

public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public boolean updatePassengerInfo(String id, String fName, String lName, String dob, String passport) throws ValidationException {
        PassengerInput input = new PassengerInput(fName, lName, dob, passport);
        PassengerInputValidator.validate(input);

        return passengerRepository.update(id, fName, lName, dob, passport);
    }
}
