package com.group9.ongo.business.constants;

import com.group9.ongo.models.Flight;

public class FlightConstants {
    private FlightConstants(){}

    //AIRLINES
    public final static String AIR_CANADA = "Air Canada";
    public final static String WESTJET = "Westjet";
    public final static String AIR_TRANSAT = "Air Transat";
    public final static String PORTER_AIRLINES = "Porter Airlines";
    public final static String[] arrAirlines = {AIR_CANADA, WESTJET, AIR_TRANSAT, PORTER_AIRLINES};


    //LOCATIONS
    public final static String TORONTO = "Toronto";
    public final static String MONTREAL = "Montreal";
    public final static String WINNIPEG = "Winnipeg";
    public final static String BC = "British Columbia";
    public final static String VANCOUVER = "Vancouver";
    public final static String QUEBEC_CITY = "Quebec City";
    public final static String CALGARY = "Calgary";
    public final static String[] arrLocations = {TORONTO, WINNIPEG, BC, VANCOUVER, QUEBEC_CITY, CALGARY, MONTREAL};


    //CAPACITY
    public final static int LARGE_CAPACITY = 200;
    public final static int MEDIUM_CAPACITY = 150;
    public final static int LOW_CAPACITY = 100;
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
    
}
