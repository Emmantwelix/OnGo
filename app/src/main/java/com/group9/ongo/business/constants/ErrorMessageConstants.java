package com.group9.ongo.business.constants;

public class ErrorMessageConstants {
    private ErrorMessageConstants() {
    }

    //USER ERROR MESSAGES
    public static final String USER_DELETE_ERROR = "User could not be deleted, since user does not exist";
    public static final String USER_NAME_TO_LONG = "Name is to long";
    public static final String USER_NAME_TO_SHORT = "Name is to short";
    public static final String USER_INVALID_EMAIL = "Invalid email format";
    public static final String USER_INVALID_PHONE = "Invalid phone number, should be 10 digits";
    public static final String USER_NOT_FOUND = "User not found";



    //FLIGHT ERROR MESSAGES
    public static final String FLIGHT_DELETE_ERROR = "Flight could not be deleted, since flight does not exist";

    //VALIDATION
    public static final String FLIGHT_INVALID_ORIGIN = "Invalid origin";
    public static final String FLIGHT_INVALID_DESTINATION = "Invalid destination";
    public static final String FLIGHT_MAX_CAPACITY = "Capacity must be less than 501";
    public static final String FLIGHT_MIN_CAPACITY = "Capacity must be greater than 0";
    public static final String FLIGHT_INVALID_DTIME = "Invalid departure time";
    public static final String FLIGHT_INVALID_LTIME = "Invalid landing time";
    public static final String FLIGHT_SAME_ORIGIN_DESTINATION = "Origin and destination cannot be the same";
    public static final String FLIGHT_MIN_PRICE = "Price must be greater than 0";
    public static final String FLIGHT_MAX_PRICE = "Price must be less than 5001";
    public static final String FLIGHT_INVALID_AIRLINE = "Invalid airline";
    public static final String FLIGHT_NOT_FOUND = "Flight not found";

    //BOOKING ERROR MESSAGES
    public static final String BOOKING_PASSENGER_ERROR = "Failed to create passenger. Booking has been rolled back.";


}
