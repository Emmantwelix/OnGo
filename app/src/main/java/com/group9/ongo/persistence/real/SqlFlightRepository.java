package com.group9.ongo.persistence.real;

import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_AIRCRAFT_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_AIRLINE;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_AVAILABILITY;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_DATE;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_DEPART_TIME;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_DESTINATION;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_LAND_TIME;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_NUMBER;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_ORIGIN;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_FLIGHT_PRICE;
import static com.group9.ongo.persistence.real.AppDbHelper.TABLE_FLIGHTS;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.group9.ongo.models.Flight;
import com.group9.ongo.persistence.FlightRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SqlFlightRepository implements FlightRepository {
    private final AppDbHelper dbHelper;
    public SqlFlightRepository(AppDbHelper adHelper) {
        this.dbHelper = adHelper;
    }

    @Override
    public List<Flight> getAll() {
        List<Flight> flights = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_FLIGHTS,
                null, // all columns
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {

                flights.add(mapCursorToFlight(cursor));
            }

            cursor.close();
        }

        return flights;
    }

    @Override
    public Flight getFlightById(int flightId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_FLIGHTS,
                null,
                COL_FLIGHT_ID + " = ?",
                new String[]{String.valueOf(flightId)},
                null,
                null,
                null
        );

        Flight flight = null;

        if (cursor != null && cursor.moveToFirst()) {

            flight = mapCursorToFlight(cursor);

            cursor.close();

        }

        return flight;
    }

    @Override
    public List<Flight> getAllAvailableFlights() {

        List<Flight> flights = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_FLIGHTS,
                null,
                COL_FLIGHT_AVAILABILITY + " = ?",
                new String[]{"1"},
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                flights.add(mapCursorToFlight(cursor));
            }
            cursor.close();
        }

        return flights;
    }
    @Override
    public int createFlight(String airline,
                            String origin,
                            String destination,
                            LocalTime departTime,
                            LocalTime landTime,
                            int aircraftId,
                            double price,
                            String flightNumber,
                            LocalDate date) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_FLIGHT_AIRCRAFT_ID, aircraftId);
        values.put(COL_FLIGHT_AIRLINE, airline);
        values.put(COL_FLIGHT_DATE, date.toString());
        values.put(COL_FLIGHT_DEPART_TIME, departTime.toString());
        values.put(COL_FLIGHT_LAND_TIME, landTime.toString());
        values.put(COL_FLIGHT_ORIGIN, origin);
        values.put(COL_FLIGHT_DESTINATION, destination);
        values.put(COL_FLIGHT_PRICE, price);
        values.put(COL_FLIGHT_NUMBER, flightNumber);
        values.put(COL_FLIGHT_AVAILABILITY, 1);

        long insertedId = db.insert(TABLE_FLIGHTS, null, values);

        if (insertedId == -1) {
            return -1;
        }

        return (int) insertedId;
    }
    public boolean deleteFlight(int flightId)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_FLIGHTS,
                COL_FLIGHT_ID + " = ?",
                new String[]{ String.valueOf(flightId) }
        );

        return rowsDeleted > 0;
    }

    @Override
    public void scheduleFlight(int flightId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_FLIGHT_AVAILABILITY, 1);

        db.update(
                TABLE_FLIGHTS,
                values,
                COL_FLIGHT_ID + " = ?",
                new String[]{String.valueOf(flightId)}
        );
    }

    @Override
    public void deScheduleFlight(int flightId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_FLIGHT_AVAILABILITY, 0);

        db.update(
                TABLE_FLIGHTS,
                values,
                COL_FLIGHT_ID + " = ?",
                new String[]{String.valueOf(flightId)}
        );
    }

    private Flight mapCursorToFlight(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FLIGHT_ID));
        int aircraftId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FLIGHT_AIRCRAFT_ID));
        String airline = cursor.getString(cursor.getColumnIndexOrThrow(COL_FLIGHT_AIRLINE));
        String origin = cursor.getString(cursor.getColumnIndexOrThrow(COL_FLIGHT_ORIGIN));
        String destination = cursor.getString(cursor.getColumnIndexOrThrow(COL_FLIGHT_DESTINATION));

        LocalDate date = LocalDate.parse(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_FLIGHT_DATE))
        );

        LocalTime depart = LocalTime.parse(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_FLIGHT_DEPART_TIME))
        );

        LocalTime land = LocalTime.parse(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_FLIGHT_LAND_TIME))
        );

        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_FLIGHT_PRICE));
        String flightNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_FLIGHT_NUMBER));
        boolean isAvailable = cursor.getInt(
                cursor.getColumnIndexOrThrow(COL_FLIGHT_AVAILABILITY)
        ) == 1;

        Flight flight = new Flight(
                id,
                airline,
                origin,
                destination,
                depart,
                land,
                aircraftId,
                price,
                flightNumber,
                date
        );

        flight.setAvailability(isAvailable);

        return flight;
    }
}
