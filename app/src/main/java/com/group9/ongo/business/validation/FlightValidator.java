package com.group9.ongo.business.validation;

import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_DTIME_NULL;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_AIRLINE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_DESTINATION;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_ORIGIN;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_TIME_SEQUENCE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_LTIME_NULL;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_INVALID_PLANE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MAX_CAPACITY;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MAX_PRICE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MIN_CAPACITY;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_MIN_PRICE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.FLIGHT_SAME_ORIGIN_DESTINATION;
import static com.group9.ongo.business.constants.FlightConstants.ARR_AIRLINES;
import static com.group9.ongo.business.constants.FlightConstants.ARR_LOCATIONS;
import static com.group9.ongo.business.constants.FlightConstants.MAX_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.MAX_PRICE;
import static com.group9.ongo.business.constants.FlightConstants.MIN_CAPACITY;
import static com.group9.ongo.business.constants.FlightConstants.MIN_PRICE;

import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Flight;

import java.time.LocalTime;

public class FlightValidator {

    public static void validate(Flight flight) throws ValidationException {
        if (flight == null) {
            throw new ValidationException(FLIGHT_NOT_FOUND);
        }
    }


    public static void validateNewFlight(String airline, String origin, String destination, LocalTime departTime, LocalTime landTime, Aircraft aircraft, double price) throws ValidationException {
        boolean validDestination = false;
        boolean validOrigin = false;
        boolean validAirline = false;


        for (String location : ARR_LOCATIONS)
        {
            if (location.equals(destination)) {
                validDestination = true;
            }
            if (location.equals(origin)) {
                validOrigin = true;
            }
        }

        for (String airlineName : ARR_AIRLINES)
        {
            if (airlineName.equals(airline)) {
                validAirline = true;
            }
        }

        if (aircraft == null || aircraft.getModelName() == null || aircraft.getModelName().isEmpty())
        {
            throw new ValidationException(FLIGHT_INVALID_PLANE);
        }
        else if (!validAirline)
        {
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
        else if ( aircraft.getCapacity() > MAX_CAPACITY) {
            throw new ValidationException(FLIGHT_MAX_CAPACITY);
        }
        else if ( aircraft.getCapacity() < MIN_CAPACITY ){
            throw new ValidationException(FLIGHT_MIN_CAPACITY);
        }
        else if(departTime == null)
        {
            throw new ValidationException(FLIGHT_DTIME_NULL);
        }
        else if(landTime == null)
        {
            throw new ValidationException(FLIGHT_LTIME_NULL);
        }
        else if(landTime.equals(departTime))
        {
            throw new ValidationException(FLIGHT_INVALID_TIME_SEQUENCE);
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

}
