package com.group9.ongo.business.validation;

import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_AIRLINE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_DESTINATION;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_DTIME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_LTIME;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_ORIGIN;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MAX_CAPACITY;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MAX_PRICE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MIN_CAPACITY;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MIN_PRICE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_SAME_ORIGIN_DESTINATION;
import static com.group9.ongo.business.constants.FlightConstants.MAX_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.MAX_PRICE;
import static com.group9.ongo.business.constants.FlightConstants.MAX_TIME_LENGTH;
import static com.group9.ongo.business.constants.FlightConstants.MIN_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.MIN_PRICE;
import static com.group9.ongo.business.constants.FlightConstants.arrAirlines;
import static com.group9.ongo.business.constants.FlightConstants.arrLocations;

import com.group9.ongo.models.Flight;

public class FlightValidator {

    public static void validate(Flight flight) throws ValidationException {
        if (flight == null) {
            throw new ValidationException(FLIGHT_NOT_FOUND);
        }
    }


    public static void validateNewFlight(String airline, String origin, String destination, String departTime, String landTime, int capacity, double price) throws ValidationException {
        boolean validDestination = false;
        boolean validOrigin = false;
        boolean validAirline = false;

        for (String location : arrLocations)
        {
            if (location.equals(destination)) {
                validDestination = true;
            }
            if (location.equals(origin)) {
                validOrigin = true;
            }
        }

        for (String airlineName : arrAirlines)
        {
            if (airlineName.equals(airline)) {
                validAirline = true;
            }
        }

        if (!validAirline) {
            throw new ValidationException(FLIGHT_INVALID_AIRLINE);
        }
        else if (!validOrigin)
        {
            throw new ValidationException(FLIGHT_INVALID_ORIGIN);
        }
        else if (!validDestination)
        {
            throw new ValidationException(FLIGHT_INVALID_DESTINATION);
        }
        else if ( capacity > MAX_CAPACITY) {
            throw new ValidationException(FLIGHT_MAX_CAPACITY);
        }
        else if ( capacity < MIN_CAPACITY ){
            throw new ValidationException(FLIGHT_MIN_CAPACITY);
        }
        else if ( !isValidTime(departTime) )
        {
            throw new ValidationException(FLIGHT_INVALID_DTIME);
        }
        else if ( !isValidTime(landTime) )
        {
            throw new ValidationException(FLIGHT_INVALID_LTIME);
        }
        else if ( origin.equals(destination) )
        {
            throw new ValidationException(FLIGHT_SAME_ORIGIN_DESTINATION);
        }
        else if (price <= MIN_PRICE) {
            throw new ValidationException(FLIGHT_MIN_PRICE);
        }
        else if (price > MAX_PRICE) {
            throw new ValidationException(FLIGHT_MAX_PRICE);
        }
    }

    private static boolean isValidTime(String time) {
        if (time == null)
        {
            return false;
        }
        else if (time.length() != MAX_TIME_LENGTH)
        {
            return false;
        }

        // Must match HHmm exactly
        if (!time.matches("\\d{4}")) {
            return false;
        }

        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(2, 4));

        return hours >= 0 && hours <= 23 &&
                minutes >= 0 && minutes <= 59;
    }

}
