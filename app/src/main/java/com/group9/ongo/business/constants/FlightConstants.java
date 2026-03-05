package com.group9.ongo.business.constants;

import com.group9.ongo.models.Flight;

import java.time.LocalDate;

public class FlightConstants {
    private FlightConstants(){}

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
    public final static String[] ARR_LOCATIONS = {TORONTO, WINNIPEG, BC, VANCOUVER, QUEBEC_CITY, CALGARY, MONTREAL};

    //PLANES
    public final static String AIRBUS_A320 = "Airbus A320";
    public final static String BOEING_737 = "Boeing 737";
    public final static String AIRBUS_A380 = "Airbus A380";
    public final static String AIRBUS_A350 = "Airbus A350";
    public final static String BOEING_787 = "Boeing 787 Dreamliner";
    public final static String COMAC_C919 = "Comac C919";
    public final static String CONVAIR_880 = "Convair 880";
    public final static String DOUGLAS_DC_8 = "Douglas DC-8";
    public final static String[] ARR_PLANES = {AIRBUS_A320, BOEING_737, AIRBUS_A380, AIRBUS_A350, BOEING_787, COMAC_C919, CONVAIR_880, DOUGLAS_DC_8};

    //CAPACITY
    public final static int LARGE_CAPACITY = 200;
    public final static int MEDIUM_CAPACITY = 150;
    public final static int SMALL_CAPACITY = 100;
    public final static int MAX_CAPACITY = 500;
    public final static int MIN_CAPACITY = 1;

    //OCCUPATION
    public final static int EMPTY = 0;
    
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
    
    public final static int MAX_TIME_LENGTH = 4;

    //DATE
    public final static int MAX_DATE_LENGTH = 8;
    public final static LocalDate DEFUALT_DATE = LocalDate.of(2026, 12, 25);



    //FLIGHT NUMBER
    public final static int FLIGHT_NUM_LENGTH = 6;
    public final static String DEFUALT_FLIGHT_NUM = "AA0000";


    //MISCELLANEOUS
    public final static String[] ALPHABET = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public final static String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static int DATE_RANGE = 21;
}
