package com.group9.ongo.business.validation;

import static com.group9.ongo.business.constants.ErrorMessageConstants.DATE_OF_BIRTH;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FIRST_NAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.LAST_NAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_DOB_INVALID_DATE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_DOB_INVALID_DATE_RANGE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_DOB_INVALID_MONTH;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_DOB_INVALID_NUM;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_DOB_INVALID_YEAR;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_FUTURE_DOB;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_INVALID_BIRTHDATE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_BIRTHDATE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_FIRSTNAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_LASTNAME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NO_PNUMBER;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSENGER_NULL_INPUT;
import static com.group9.ongo.business.constants.ErrorMessageConstants.PASSPORT;

import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;

import java.time.LocalDate;
import java.time.DateTimeException;

public class PassengerInputValidator {

    public static void validatePassenger(Passenger passenger) throws ValidationException {
        if (passenger == null) {
            throw new ValidationException(PASSENGER_NOT_FOUND);
        }
    }
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
            throw new ValidationException(PASSENGER_NO_BIRTHDATE, DATE_OF_BIRTH);
        }


        String[] parts = dob.split("-");
        if (parts.length != 3) {
            throw new ValidationException(PASSENGER_INVALID_BIRTHDATE, DATE_OF_BIRTH);
        }

        // Enforce strict YYYY-MM-DD format
        if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new ValidationException(PASSENGER_INVALID_BIRTHDATE, DATE_OF_BIRTH);
        }

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            if (month < 1 || month > 12) {
                throw new ValidationException(PASSENGER_DOB_INVALID_MONTH, DATE_OF_BIRTH);
            }

            if (day < 1 || day > 31) {
                throw new ValidationException(PASSENGER_DOB_INVALID_DATE_RANGE, DATE_OF_BIRTH);
            }

            if (year < 1850) {
                throw new ValidationException(PASSENGER_DOB_INVALID_YEAR, DATE_OF_BIRTH);
            }

            LocalDate birthDate = LocalDate.of(year, month, day);
            LocalDate today = LocalDate.now();

            if (birthDate.isAfter(today)) {
                throw new ValidationException(PASSENGER_FUTURE_DOB, DATE_OF_BIRTH);
            }

        } catch (NumberFormatException e) {
            throw new ValidationException(PASSENGER_DOB_INVALID_NUM, DATE_OF_BIRTH);
        } catch (DateTimeException e) {
            throw new ValidationException(PASSENGER_DOB_INVALID_DATE + e.getMessage(), DATE_OF_BIRTH);

        }
    }
}
