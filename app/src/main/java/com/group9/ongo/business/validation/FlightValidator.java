package com.group9.ongo.business.validation;

import com.group9.ongo.models.Flight;

public class FlightValidator {
    public static void validate(Flight flight) {
        if (flight == null) {
            throw new ValidationException("Flight cannot be null");
        }
    }
}
