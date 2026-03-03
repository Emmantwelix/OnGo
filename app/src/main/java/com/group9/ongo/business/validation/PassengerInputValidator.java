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
import java.time.format.DateTimeParseException;

public class PassengerInputValidator {

    public static void validate(PassengerInput input) {
        if (input == null) {
            throw new ValidationException(PASSENGER_NULL_INPUT);
        }
        if (input.getFirstName() == null || input.getFirstName().isBlank()) {
            throw new ValidationException(PASSENGER_NO_FIRSTNAME, FIRST_NAME);
        }
        if (input.getLastName() == null || input.getLastName().isBlank()) {
            throw new ValidationException(PASSENGER_NO_LASTNAME, LAST_NAME);
        }
        if (input.getDateOfBirth() == null || input.getDateOfBirth().isBlank()) {
            throw new ValidationException(PASSENGER_NO_BIRTHDATE, DATE_OF_BIRTH);
        }
        
        try {
            LocalDate.parse(input.getDateOfBirth());
        } catch (DateTimeParseException e) {
            throw new ValidationException(PASSENGER_INVALID_BIRTHDATE, DATE_OF_BIRTH);
        }

        if (input.getPassportNumber() == null || input.getPassportNumber().isBlank()) {
                throw new ValidationException(PASSENGER_NO_PNUMBER, PASSPORT);
        }
    }
}
