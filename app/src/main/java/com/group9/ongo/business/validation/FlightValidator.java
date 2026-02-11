package com.group9.ongo.business.validation;

import com.group9.ongo.models.Flight;

public class FlightValidator {
    private static final String[] arrLocations = {"Toronto", "Montreal", "Vancouver", "Winnipeg"};
    private static final int MAX_TIME_LENGTH = 4;
    private static final int MAX_CAPACITY = 500;
    private static final int MIN_CAPACITY = 1;
    

    public static void validate(Flight flight) {
        if (flight == null) {
            throw new ValidationException("Flight cannot be null");
        }
    }
    public static void validateNewFlight(String airline, String origin, String destination, String departTime, String landTime, int capacity) {
        boolean validDestination = false;
        boolean validOrigin = false;

        for (String location : arrLocations)
        {
            if (location.equals(destination)) {
                validDestination = true;
            }
            if (location.equals(origin)) {
                validOrigin = true;
            }
        }
        if (!validOrigin)
        {
            throw new ValidationException("Invalid origin");
        }
        if (!validDestination)
        {
            throw new ValidationException("Invalid destination");
        }

        if (capacity <= 0)
        {
            throw new ValidationException("Capacity must be greater than 0");
        }
        if ( capacity > MAX_CAPACITY) {
            throw new ValidationException("Capacity must be less than 501");
        }
        if ( capacity < MIN_CAPACITY ){
            throw new ValidationException("Capacity must be greater than 0");
        }
        if (departTime.length() != MAX_TIME_LENGTH )
        {
            throw new ValidationException("Invalid departure time");
        }
        if (landTime.length() != MAX_TIME_LENGTH )
        {
            throw new ValidationException("Invalid landing time");
        }
        if ( origin.equals(destination) )
        {
            throw new ValidationException("Origin and destination cannot be the same");
        }
    }
}
