package com.group9.ongo.business.validation;

import com.group9.ongo.models.Flight;
import com.group9.ongo.models.PassengerInput;

public class BookingValidator {
    public static void validate(Flight flight, int flightId, PassengerInput input) {
        PassengerInputValidator.validate(input);
        FlightValidator.validate(flight);

    }
}
