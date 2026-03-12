package com.group9.ongo.persistence.real;

import static com.group9.ongo.business.constants.FlightConstants.A320_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.A380_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.AIR_CANADA;
import static com.group9.ongo.business.constants.FlightConstants.AIR_TRANSAT;
import static com.group9.ongo.business.constants.FlightConstants.B737_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.B787_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_DATE;
import static com.group9.ongo.business.constants.FlightConstants.DEFAULT_FLIGHT_NUM;
import static com.group9.ongo.business.constants.FlightConstants.MONTREAL;
import static com.group9.ongo.business.constants.FlightConstants.PORTER_AIRLINES;
import static com.group9.ongo.business.constants.FlightConstants.TORONTO;
import static com.group9.ongo.business.constants.FlightConstants.VANCOUVER;
import static com.group9.ongo.business.constants.FlightConstants.WESTJET;
import static com.group9.ongo.business.constants.FlightConstants.WINNIPEG;
import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_EMAIL;
import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_NAME;
import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_PHONE_NUM;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group9.ongo.business.services.Implementations.SeatMapService;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.models.Seat;
import com.group9.ongo.models.SeatMapConfig;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class AppDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "ongo.db";
    public static final int DB_VERSION = 1;

    //bookings
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String COL_BOOKING_ID = "id";
    public static final String COL_BOOKING_FLIGHT_ID = "flight_id";
    public static final String COL_BOOKING_USER_ID = "user_id";
    public static final String COL_BOOKING_SEAT_ID = "seat_id";
    public static final String COL_BOOKING_STATUS = "status";


    //flights
    public static final String TABLE_FLIGHTS = "flights";
    public static final String COL_FLIGHT_ID = "id";
    public static final String COL_FLIGHT_AIRLINE = "airline";
    public static final String COL_FLIGHT_AIRCRAFT_ID = "aircraft_id";
    public static final String COL_FLIGHT_ORIGIN =  "origin";
    public static final String COL_FLIGHT_DEPART_TIME = "depart_time";
    public static final String COL_FLIGHT_LAND_TIME = "land_time";
    public static final String COL_FLIGHT_DESTINATION = "destination";
    public static final String COL_FLIGHT_PRICE = "price";
    public static final String COL_FLIGHT_DATE = "flight_date";
    public static final String COL_FLIGHT_NUMBER = "flight_number";
    public static final String COL_FLIGHT_AVAILABILITY = "availability";

    //passengers
    public static final String TABLE_PASSENGERS = "passengers";
    public static final String COL_PASSENGER_ID = "id";
    public static final String COL_PASSENGER_BOOKING_ID  = "booking_id";
    public static final String COL_PASSENGER_FIRST_NAME = "first_name";
    public static final String COL_PASSENGER_LAST_NAME = "last_name";
    public static final String COL_PASSENGER_DATE_OF_BIRTH = "date_of_birth";
    public static final String COL_PASSENGER_PASSPORT_NUMBER = "passport_number";

    //users
    public static final String TABLE_USERS = "users";
    public static final String  COL_USER_ID = "id";
    public static final String COL_USER_USERNAME = "username";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PHONE = "phone";

    //seats
    public static final String TABLE_SEATS = "seats";
    public static final String COL_SEAT_ID = "id";
    public static final String COL_SEAT_FLIGHT_ID = "flight_id";
    public static final String COL_SEAT_ROW = "seat_row";
    public static final String COL_SEAT_COLUMN = "seat_column";
    public static final String COL_SEAT_IS_BOOKED = "is_booked";


    //aircraft
    public static final String TABLE_AIRCRAFT = "aircraft";
    public static final String COL_AIRCRAFT_ID = "id";
    public static final String COL_AIRCRAFT_MODEL_NAME = "model_name";
    public static final String COL_AIRCRAFT_CAPACITY = "capacity";
    public static final String COL_AIRCRAFT_HAS_WIFI = "has_wifi";



    public AppDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //aircraft table
        db.execSQL(
                "CREATE TABLE " + TABLE_AIRCRAFT + " (" +
                        COL_AIRCRAFT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_AIRCRAFT_MODEL_NAME + " TEXT NOT NULL, " +
                        COL_AIRCRAFT_CAPACITY + " INTEGER NOT NULL CHECK(" + COL_AIRCRAFT_CAPACITY + " > 0), " +
                        COL_AIRCRAFT_HAS_WIFI + " INTEGER NOT NULL DEFAULT 0 CHECK(" + COL_AIRCRAFT_HAS_WIFI + " IN (0,1)) " +
                        ");"
        );

        int[] aircraft_ids = seedDbWithAircraft(db);

        // flights table
        db.execSQL(
                "CREATE TABLE " + TABLE_FLIGHTS + " (" +
                        COL_FLIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_FLIGHT_AIRCRAFT_ID + " INTEGER NOT NULL, " +
                        COL_FLIGHT_AIRLINE + " TEXT NOT NULL, " +
                        COL_FLIGHT_DATE + " TEXT NOT NULL, " +
                        COL_FLIGHT_DEPART_TIME + " TEXT NOT NULL, " +
                        COL_FLIGHT_LAND_TIME + " TEXT NOT NULL, " +
                        COL_FLIGHT_ORIGIN + " TEXT NOT NULL, " +
                        COL_FLIGHT_DESTINATION + " TEXT NOT NULL, " +
                        COL_FLIGHT_PRICE + " REAL NOT NULL CHECK(" + COL_FLIGHT_PRICE + " > 0), " +
                        COL_FLIGHT_NUMBER + " TEXT NOT NULL, " +
                        COL_FLIGHT_AVAILABILITY + " INTEGER NOT NULL DEFAULT 1 CHECK(" + COL_FLIGHT_AVAILABILITY + " IN (0,1)), " +
                        "FOREIGN KEY(" + COL_FLIGHT_AIRCRAFT_ID + ") REFERENCES " + TABLE_AIRCRAFT + "(" + COL_AIRCRAFT_ID + ")" +
                        ");"
        );

        // seats table
        db.execSQL(
                "CREATE TABLE " + TABLE_SEATS + " (" +
                        COL_SEAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_SEAT_FLIGHT_ID + " INTEGER NOT NULL, " +
                        COL_SEAT_ROW + " INTEGER NOT NULL CHECK(" + COL_SEAT_ROW + " > 0), " +
                        COL_SEAT_COLUMN + " TEXT NOT NULL, " +
                        COL_SEAT_IS_BOOKED + " INTEGER NOT NULL DEFAULT 0 CHECK(" + COL_SEAT_IS_BOOKED + " IN (0,1)), " +
                        "FOREIGN KEY(" + COL_SEAT_FLIGHT_ID + ") REFERENCES " + TABLE_FLIGHTS + "(" + COL_FLIGHT_ID + ")" +
                        ");"
        );

        seedFlights(db, aircraft_ids);

        //users table
        db.execSQL(
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_USER_USERNAME + " TEXT NOT NULL, " +
                        COL_USER_EMAIL + " TEXT NOT NULL, " +
                        COL_USER_PHONE + " TEXT NOT NULL " +
                        ");"
        );

        seedDbWithSampleUser(db);

        //bookings table
        db.execSQL(
                "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                        COL_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_BOOKING_FLIGHT_ID + " INTEGER NOT NULL, " +
                        COL_BOOKING_USER_ID + " INTEGER NOT NULL, " +
                        COL_BOOKING_SEAT_ID + " INTEGER NOT NULL, " +
                        COL_BOOKING_STATUS + " TEXT NOT NULL, " +

                        "FOREIGN KEY (" + COL_BOOKING_FLIGHT_ID + ") REFERENCES " +
                        TABLE_FLIGHTS + "(" + COL_FLIGHT_ID + ") ON DELETE CASCADE, " +

                        "FOREIGN KEY (" + COL_BOOKING_USER_ID + ") REFERENCES " +
                        TABLE_USERS + "(" + COL_USER_ID + ") ON DELETE CASCADE, " +

                        "FOREIGN KEY (" + COL_BOOKING_SEAT_ID + ") REFERENCES " +
                                TABLE_SEATS + "(" + COL_SEAT_ID + ") ON DELETE CASCADE" +
                                ");"
        );

        //passengers table
        db.execSQL(
                "CREATE TABLE " + TABLE_PASSENGERS + " (" +
                        COL_PASSENGER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_PASSENGER_BOOKING_ID + " INTEGER NOT NULL, " +
                        COL_PASSENGER_FIRST_NAME + " TEXT NOT NULL, " +
                        COL_PASSENGER_LAST_NAME + " TEXT NOT NULL, " +
                        COL_PASSENGER_DATE_OF_BIRTH + " DATE NOT NULL, " +
                        COL_PASSENGER_PASSPORT_NUMBER + " TEXT NOT NULL, " +
                        "FOREIGN KEY (" + COL_PASSENGER_BOOKING_ID + ") REFERENCES " +
                        TABLE_BOOKINGS + "(" + COL_BOOKING_ID + ")" +
                        ");"
        );

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSENGERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLIGHTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AIRCRAFT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    private void seedDbWithSampleUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_USERNAME, SAMPLE_USER_NAME);
        values.put(COL_USER_EMAIL, SAMPLE_USER_EMAIL);
        values.put(COL_USER_PHONE, SAMPLE_USER_PHONE_NUM);

        db.insert(TABLE_USERS, null, values);
    }
    private int[] seedDbWithAircraft(SQLiteDatabase db) {

        int[] aircraftIds = new int[4];

        aircraftIds[0] = insertAircraft(db, A320_DETAILS);
        aircraftIds[1] = insertAircraft(db, B737_DETAILS);
        aircraftIds[2] = insertAircraft(db, B787_DETAILS);
        aircraftIds[3] = insertAircraft(db, A380_DETAILS);

        return aircraftIds;
    }

    private int insertAircraft(SQLiteDatabase db, Aircraft aircraft) {

        ContentValues values = new ContentValues();
        values.put(COL_AIRCRAFT_MODEL_NAME, aircraft.getModelName());
        values.put(COL_AIRCRAFT_CAPACITY, aircraft.getCapacity());
        values.put(COL_AIRCRAFT_HAS_WIFI, aircraft.hasWifi() ? 1 : 0);

        return (int) db.insert(TABLE_AIRCRAFT, null, values);
    }

    private void seedFlights(SQLiteDatabase db, int[] aircraftIds) {
        seedOneFlight(
                db,
                AIR_CANADA,
                TORONTO,
                WINNIPEG,
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                aircraftIds[0],
                A320_DETAILS.getCapacity(),
                603.49,
                DEFAULT_FLIGHT_NUM,
                DEFAULT_DATE
        );

        seedOneFlight(
                db,
                PORTER_AIRLINES,
                TORONTO,
                MONTREAL,
                LocalTime.of(12, 0),
                LocalTime.of(14, 0),
                aircraftIds[1],
                B737_DETAILS.getCapacity(),
                979.52,
                DEFAULT_FLIGHT_NUM,
                DEFAULT_DATE
        );

        seedOneFlight(
                db,
                AIR_TRANSAT,
                WINNIPEG,
                VANCOUVER,
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                aircraftIds[2],
                B787_DETAILS.getCapacity(),
                200.01,
                DEFAULT_FLIGHT_NUM,
                DEFAULT_DATE
        );

        seedOneFlight(
                db,
                WESTJET,
                MONTREAL,
                WINNIPEG,
                LocalTime.of(16, 0),
                LocalTime.of(18, 0),
                aircraftIds[3],
                A380_DETAILS.getCapacity(),
                417.38,
                DEFAULT_FLIGHT_NUM,
                DEFAULT_DATE
        );
    }

    private int seedOneFlight(SQLiteDatabase db,
                              String airline,
                              String origin,
                              String destination,
                              LocalTime departTime,
                              LocalTime landTime,
                              int aircraftId,
                              int capacity,
                              double price,
                              String flightNumber,
                              LocalDate date) {

        ContentValues values = new ContentValues();
        values.put(COL_FLIGHT_AIRLINE, airline);
        values.put(COL_FLIGHT_ORIGIN, origin);
        values.put(COL_FLIGHT_DESTINATION, destination);
        values.put(COL_FLIGHT_DEPART_TIME, departTime.toString());
        values.put(COL_FLIGHT_LAND_TIME, landTime.toString());
        values.put(COL_FLIGHT_AIRCRAFT_ID, aircraftId);
        values.put(COL_FLIGHT_PRICE, price);
        values.put(COL_FLIGHT_NUMBER, flightNumber);
        values.put(COL_FLIGHT_DATE, date.toString());
        values.put(COL_FLIGHT_AVAILABILITY, 1);

        int flightId = (int) db.insert(TABLE_FLIGHTS, null, values);

        seedSeats(db, flightId, capacity);

        return flightId;
    }

    private void seedSeats(SQLiteDatabase db, int flightId, int capacity) {
        // Use the centralized SeatMapConfig to ensure database seats match UI seats perfectly.
        SeatMapConfig config = SeatMapService.createFromCapacity(capacity);
        List<Seat> generatedSeats = SeatMapService.generateSeats(config);

        for (Seat seat : generatedSeats) {
            if (seat.getType() == Seat.Type.SEAT) {
                insertSeat(db, flightId, seat.getRow(), seat.getLabel());
            }
        }
    }

    private void insertSeat(SQLiteDatabase db, int flightId, int row, String column) {
        ContentValues seatValues = new ContentValues();
        seatValues.put(COL_SEAT_FLIGHT_ID, flightId);
        seatValues.put(COL_SEAT_ROW, row);
        seatValues.put(COL_SEAT_COLUMN, column);
        seatValues.put(COL_SEAT_IS_BOOKED, 0);

        db.insert(TABLE_SEATS, null, seatValues);
    }

}
