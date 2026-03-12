package com.group9.ongo.business.constants;

public class ErrorMessageConstants {
    private ErrorMessageConstants() {
    }

    //USER ERROR MESSAGES
    public static final String USER_DELETE_ERROR = "User could not be deleted, since user does not exist";
    public static final String USER_NAME_TO_LONG = "Name is too long";
    public static final String USER_NAME_TO_SHORT = "Name is too short";
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

    public static final String FLIGHT_DTIME_NULL = "departure time cannot be null";

    public static final String FLIGHT_LTIME_NULL = "landing time cannot be null";

    public static final String FLIGHT_INVALID_TIME_SEQUENCE = "Landing time cannot be equal to depart time";
    public static final String FLIGHT_SAME_ORIGIN_DESTINATION = "Origin and destination cannot be the same";
    public static final String FLIGHT_MIN_PRICE = "Price must be greater than 0";
    public static final String FLIGHT_MAX_PRICE = "Price must be less than 5001";
    public static final String FLIGHT_INVALID_AIRLINE = "Invalid airline";
    public static final String FLIGHT_NOT_FOUND = "Flight not found";
    public static final String FLIGHT_INVALID_PLANE = "Invalid plane type";
    public static final String NO_FLIGHTS_AVAILABLE = "No flights available with search criteria";


    public static final String AIRCRAFT_NOT_FOUND = "Aircraft not found";


    //BOOKING ERROR MESSAGES
    public static final String BOOKING_PASSENGER_ERROR = "Failed to create passenger. Booking has been rolled back.";

    //PASSENGER ERROR MESSAGES
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String DATE_OF_BIRTH = "birthDate";
    public static final String PASSPORT = "passport";
    public static final String PASSENGER_NULL_INPUT = "Passenger input cannot be null";
    public static final String PASSENGER_NO_FIRSTNAME = "First name is required";
    public static final String PASSENGER_NO_LASTNAME = "Last name is required";
    public static final String PASSENGER_NO_BIRTHDATE = "Date of birth is required";
    public static final String PASSENGER_INVALID_BIRTHDATE = "Date of birth must be in format YYYY-MM-DD";
    public static final String PASSENGER_NO_PNUMBER = "Passport number is required";
    public static final String PASSENGER_DOB_INVALID_MONTH = "Month must be between 01 and 12";
    public static final String PASSENGER_DOB_INVALID_DATE_RANGE = "Day must be between 01 and 31";
    public static final String PASSENGER_DOB_INVALID_YEAR = "Birth year must be 1850 or later";
    public static final String PASSENGER_FUTURE_DOB = "Date of birth cannot be in the future";
    public static final String PASSENGER_DOB_INVALID_NUM = "Date of birth must contain valid numbers";
    public static final String PASSENGER_DOB_INVALID_DATE = "Invalid date: ";


    //SEAT ERROR MESSAGES
    public static final String SEAT_NOT_FOUND = "Seat not found";
    public static final String SEAT_ALREADY_EXISTS = "Seat already exists for this flight";
    public static final String SEAT_ALREADY_BOOKED = "Seat is already booked";
    public static final String SEAT_ALREADY_UNBOOKED = "Seat is not booked";
    public static final String NO_AVAILABLE_SEAT = "No available seats";


}
