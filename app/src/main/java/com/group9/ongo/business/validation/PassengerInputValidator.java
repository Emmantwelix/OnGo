package com.group9.ongo.business.validation;

import com.group9.ongo.models.PassengerInput;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PassengerInputValidator {

    public static void validate(PassengerInput input) {
        if (input == null) {
            throw new ValidationException("Passenger input cannot be null");
        }
        if (input.getFirstName() == null || input.getFirstName().isBlank()) {
            throw new ValidationException("First name is required", "firstName");
        }
        if (input.getLastName() == null || input.getLastName().isBlank()) {
            throw new ValidationException("Last name is required", "lastName");
        }
        if (input.getDateOfBirth() == null || input.getDateOfBirth().isBlank()) {
            throw new ValidationException("Date of birth is required", "birthDate");
        }
        
        try {
            LocalDate.parse(input.getDateOfBirth());
        } catch (DateTimeParseException e) {
            throw new ValidationException("Date of birth must be in format YYYY-MM-DD", "birthDate");
        }

        if (input.getPassportNumber() == null || input.getPassportNumber().isBlank()) {
            throw new ValidationException("Passport number is required", "passport");
        }
    }
}
