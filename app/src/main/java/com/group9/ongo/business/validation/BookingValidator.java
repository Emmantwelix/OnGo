package com.group9.ongo.business.validation;

import static com.group9.ongo.business.constants.ErrorMessageConstants.BOOKING_NOT_FOUND;

import com.group9.ongo.models.Booking;
import com.group9.ongo.models.Flight;
import com.group9.ongo.models.PassengerInput;

public class BookingValidator {

    public static void validateBooking(Booking booking) throws ValidationException
    {
        if(booking == null) {
            throw new ValidationException(BOOKING_NOT_FOUND);
        }
    }

    public static void validateBookingFields(Flight flight, PassengerInput input) throws ValidationException {
        PassengerInputValidator.validate(input);
        FlightValidator.validate(flight);
    }
}
