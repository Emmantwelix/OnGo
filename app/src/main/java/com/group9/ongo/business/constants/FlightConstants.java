package com.group9.ongo.business.constants;

import com.group9.ongo.models.Aircraft;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FlightConstants {
    private FlightConstants(){}

    public final static Aircraft A320_DETAILS = new Aircraft(0,"Airbus A320", 150, true);
    public final static Aircraft B737_DETAILS = new Aircraft(0,"Boeing 737", 160, false);
    public final static Aircraft A380_DETAILS = new Aircraft(0,"Airbus A380", 500, true);
    public final static Aircraft B787_DETAILS = new Aircraft(0,"Boeing 787 Dreamliner", 250, true);

    public final static Aircraft[] ARR_AIRCRAFT_MODELS = {
            A320_DETAILS, B737_DETAILS, A380_DETAILS, B787_DETAILS
    };

    //AIRLINES
    public final static String AIR_CANADA = "Air Canada";
    public final static String WESTJET = "Westjet";
    public final static String AIR_TRANSAT = "Air Transat";
    public final static String PORTER_AIRLINES = "Porter Airlines";
    public final static String[] ARR_AIRLINES = {AIR_CANADA, WESTJET, AIR_TRANSAT, PORTER_AIRLINES};


    //LOCATIONS
    public final static String TORONTO = "Toronto";
    public final static String MONTREAL = "Montreal";
    public final static String WINNIPEG = "Winnipeg";
    public final static String BC = "British Columbia";
    public final static String VANCOUVER = "Vancouver";
    public final static String QUEBEC_CITY = "Quebec City";
    public final static String CALGARY = "Calgary";
    public final static String TSU = "Tsu";
    public final static String[] ARR_LOCATIONS = {TSU,TORONTO, WINNIPEG, BC, VANCOUVER, QUEBEC_CITY, CALGARY, MONTREAL};

    //Airport Codes
    public static final String TORONTO_CODE = "YYZ";
    public static final String WINNIPEG_CODE = "YWG";
    public static final String MONTREAL_CODE = "YUL";
    public static final String VANCOUVER_CODE = "YVR";
    public static final String CALGARY_CODE = "YYC";
    public static final String QUEBEC_CITY_CODE = "YQB";
    public static final String DEFAULT_CODE = "YYY";

    public final static int MAX_CAPACITY = 500;
    public final static int MIN_CAPACITY = 1;

    //PRICE
    public final static double LARGER_PRICE = 903.94;
    public final static double LARGE_PRICE = 799.49;
    public final static double MEDIUM_PRICE = 633.52;
    public final static double LOW_PRICE = 446.01;
    public final static double LOWER_PRICE = 183.35;
    public final static double MAX_PRICE = 5000;
    public final static double MIN_PRICE = 0; //0 is invalid
    
    //TIME
    public final static String TIME_FORMAT_PATTERN = "hh:mm a";
    public final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN);
    
    public final static int MAX_TIME_LENGTH = 4;

    //DATE
    public final static int MAX_DATE_LENGTH = 8;
    public final static LocalDate DEFAULT_DATE = LocalDate.of(2026, 12, 25);
    public final static LocalDate DEFAULT_DATE2 = LocalDate.of(2027, 2, 5);
    public final static LocalTime DEFAULT_TIME = LocalTime.of(12, 30);
    public final static LocalTime DEFAULT_TIME2 = LocalTime.of(2, 30);

    //FLIGHT NUMBER
    public final static int FLIGHT_NUM_LENGTH = 6;
    public final static String DEFAULT_FLIGHT_NUM = "AA0000";

    //MISCELLANEOUS
    public final static String[] ALPHABET = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public final static String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public final static int DATE_RANGE = 21;

    //SEATS
    public final static int MAX_ROWS = 3;
    public final static int MAX_COLUMNS = 6;
    public final static int MAX_SEATS = 18;
    public final static int ROW_ONE = 1;
    public final static String COLUMN_1 = "A";
    public final static String COLUMN_2 = "B";
    public final static String COLUMN_3 = "C";
    public final static String COLUMN_4 = "D";
    public final static String COLUMN_5 = "E";
    public final static String COLUMN_6 = "F";

}
