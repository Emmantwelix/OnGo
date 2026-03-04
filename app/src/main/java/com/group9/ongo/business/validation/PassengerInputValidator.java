package com.group9.ongo.business.validation;

import static com.group9.ongo.business.constants.ErrorMessageConstants.DATE_OF_BIRTH;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FIRST_NAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.LAST_NAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_INVALID_BIRTHDATE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_BIRTHDATE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_FIRSTNAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_LASTNAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_PNUMBER;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NULL_INPUT;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSPORT;

import com.group9.ongo.models.PassengerInput;

import java.time.LocalDate;
import java.time.DateTimeException;

public class PassengerInputValidator {

    public static void validate(PassengerInput input) throws ValidationException {
        if (input == null) {
            throw new ValidationException(PASSENGER_NULL_INPUT);
        }
        if (input.getFirstName() == null || input.getFirstName().isBlank()) {
            throw new ValidationException(PASSENGER_NO_FIRSTNAME, FIRST_NAME);
        }
        if (input.getLastName() == null || input.getLastName().isBlank()) {
            throw new ValidationException(PASSENGER_NO_LASTNAME, LAST_NAME);
        }
        
        validateBirthDate(input.getDateOfBirth());

        if (input.getPassportNumber() == null || input.getPassportNumber().isBlank()) {
                throw new ValidationException(PASSENGER_NO_PNUMBER, PASSPORT);
        }
    }

    private static void validateBirthDate(String dob) throws ValidationException {
        if (dob == null || dob.isBlank()) {
            throw new ValidationException("Date of birth is required", "birthDate");
        }

        String[] parts = dob.split("-");
        if (parts.length != 3) {
            throw new ValidationException("Date of birth must be in format YYYY-MM-DD", "birthDate");
        }

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            if (month < 1 || month > 12) {
                throw new ValidationException("Month must be between 01 and 12", "birthDate");
            }

            if (day < 1 || day > 31) {
                throw new ValidationException("Day must be between 01 and 31", "birthDate");
            }

            if (year < 1850) {
                throw new ValidationException("Birth year must be 1850 or later", "birthDate");
            }

            LocalDate birthDate = LocalDate.of(year, month, day);
            LocalDate today = LocalDate.now();

            if (birthDate.isAfter(today)) {
                throw new ValidationException("Date of birth cannot be in the future", "birthDate");
            }

        } catch (NumberFormatException e) {
            throw new ValidationException("Date of birth must contain valid numbers", "birthDate");
        } catch (DateTimeException e) {
            throw new ValidationException("Invalid date: " + e.getMessage(), "birthDate");
        }
    }
}
