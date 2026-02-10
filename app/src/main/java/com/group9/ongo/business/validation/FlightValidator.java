package com.group9.ongo.business.validation;

import com.group9.ongo.models.Flight;

public class FlightValidator {
    private static final String[] arrLocations = {"Toronto", "Montreal", "Vancouver", "Winnipeg"};
    private static final int MAX_TIME = 2359;
    

    public static void validate(Flight flight) {
        if (flight == null) {
            throw new ValidationException("Flight cannot be null");
        }
    }
    public static void validateNewFlight(String airline, String destination, int departTime, int landTime, int capacity) {
        boolean valid = false;
        for (String location : arrLocations)
        {
            if (location.equals(destination)) {
                valid = true;
                break;
            }
        }
        if (!valid)
        {
            throw new ValidationException("Invalid destination");
        }
        if (capacity <= 0)
        {
            throw new ValidationException("Capacity must be greater than 0");
        }
        if (departTime - MAX_TIME > 0)
        {
            throw new ValidationException("Invalid departure time");
        }
        if (landTime - MAX_TIME > 0)
        {
            throw new ValidationException("Invalid landing time");
        }
    }
}
