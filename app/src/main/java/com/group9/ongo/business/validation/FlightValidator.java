package com.group9.ongo.business.validation;

import com.group9.ongo.models.Flight;

public class FlightValidator {
    private static final String[] arrLocations = {"Toronto", "Montreal", "Vancouver", "Winnipeg"};
    private static final int MAX_TIME_LENGTH = 4;
    private static final int MAX_CAPACITY = 500;
    private static final int MIN_CAPACITY = 1;
    private static final double MAX_PRICE = 5000;
    private static final double MIN_PRICE = 0; //0 is invalid
    

    public static void validate(Flight flight) {
        if (flight == null) {
            throw new ValidationException("Flight not found");
        }
    }


    public static void validateNewFlight(String airline, String origin, String destination, String departTime, String landTime, int capacity, double price) {
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
        else if (!validDestination)
        {
            throw new ValidationException("Invalid destination");
        }
        else if ( capacity > MAX_CAPACITY) {
            throw new ValidationException("Capacity must be less than 501");
        }
        else if ( capacity < MIN_CAPACITY ){
            throw new ValidationException("Capacity must be greater than 0");
        }
        else if ( !isValidTime(departTime) )
        {
            throw new ValidationException("Invalid departure time");
        }
        else if ( !isValidTime(landTime) )
        {
            throw new ValidationException("Invalid landing time");
        }
        else if ( origin.equals(destination) )
        {
            throw new ValidationException("Origin and destination cannot be the same");
        }
        else if (price <= MIN_PRICE) {
            throw new ValidationException("Price must be greater than 0");
        }
        else if (price > MAX_PRICE) {
            throw new ValidationException("Price must be less than 5001");
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
