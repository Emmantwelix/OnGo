package com.group9.ongo.business.validation;

import static com.group9.ongo.business.constants.ErrorMessageConstants.AIRCRAFT_NOT_FOUND;
import com.group9.ongo.models.Aircraft;

public class AircraftValidator {
    public static void validate(Aircraft aircraft) throws ValidationException {
        if (aircraft == null) {
            throw new ValidationException(AIRCRAFT_NOT_FOUND);
        }
    }
}
