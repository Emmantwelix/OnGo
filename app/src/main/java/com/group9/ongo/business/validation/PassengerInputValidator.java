package com.group9.ongo.business.validation;

import com.group9.ongo.models.PassengerInput;

public class PassengerInputValidator {

    public static void validate(PassengerInput input) {
        if (input == null) {
            throw new ValidationException("Passenger input cannot be null");
        }
        if (input.firstName == null || input.firstName.isBlank()) {
            throw new ValidationException("First name is required");
        }
        if (input.lastName == null || input.lastName.isBlank()) {
            throw new ValidationException("Last name is required");
        }
        if (input.dateOfBirth == null) {
            throw new ValidationException("Date of birth is required");
        }
        if (input.passportNumber == null || input.passportNumber.isBlank()) {
            throw new ValidationException("Passport number is required");
        }
    }
}
