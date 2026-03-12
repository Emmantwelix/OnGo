package com.group9.ongo.persistence.real;

import static com.group9.ongo.persistence.real.AppDbHelper.COL_PASSENGER_BOOKING_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_PASSENGER_DATE_OF_BIRTH;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_PASSENGER_FIRST_NAME;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_PASSENGER_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_PASSENGER_LAST_NAME;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_PASSENGER_PASSPORT_NUMBER;
import static com.group9.ongo.persistence.real.AppDbHelper.TABLE_PASSENGERS;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.group9.ongo.models.Passenger;
import com.group9.ongo.models.PassengerInput;
import com.group9.ongo.persistence.PassengerRepository;

import java.time.LocalDate;

public class SqlPassengerRepository implements PassengerRepository {

    private final AppDbHelper dbHelper;
    public SqlPassengerRepository(AppDbHelper adHelper) {
        this.dbHelper = adHelper;
    }
    @Override
    public Passenger addPassenger(PassengerInput info, int bookingId) {
        if (info == null) return null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PASSENGER_BOOKING_ID, bookingId);
        values.put(COL_PASSENGER_FIRST_NAME, info.getFirstName());
        values.put(COL_PASSENGER_LAST_NAME, info.getLastName());

        values.put(COL_PASSENGER_DATE_OF_BIRTH, info.getDateOfBirth());

        values.put(COL_PASSENGER_PASSPORT_NUMBER, info.getPassportNumber());

        long id = db.insert(TABLE_PASSENGERS, null, values);

        if (id == -1) {
            return null; // insert failed
        }

        return new Passenger(
                (int) id,
                bookingId,
                info.getFirstName(),
                info.getLastName(),
                LocalDate.parse(info.getDateOfBirth()),
                info.getPassportNumber()
        );
    }

    @Override
    public Passenger getPassengerByBookingId(int bookingId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PASSENGERS,
                null,
                COL_PASSENGER_BOOKING_ID + "=?",
                new String[]{String.valueOf(bookingId)},
                null,
                null,
                null
        );

        Passenger passenger = null;

        if (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PASSENGER_ID));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSENGER_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSENGER_LAST_NAME));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSENGER_DATE_OF_BIRTH));
            String passport = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSENGER_PASSPORT_NUMBER));

            passenger = new Passenger(
                    id,
                    bookingId,
                    firstName,
                    lastName,
                    LocalDate.parse(dob),
                    passport
            );
        }

        cursor.close();

        return passenger;
    }


    @Override
    public boolean deletePassengersByBookingId(int bookingId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_PASSENGERS,
                COL_PASSENGER_BOOKING_ID + "=?",
                new String[]{String.valueOf(bookingId)}
        );

        return rowsDeleted > 0;
    }

    @Override
    public boolean update(String id, String fName, String lName, String dob, String passport) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PASSENGER_FIRST_NAME, fName);
        values.put(COL_PASSENGER_LAST_NAME, lName);
        values.put(COL_PASSENGER_DATE_OF_BIRTH, dob);
        values.put(COL_PASSENGER_PASSPORT_NUMBER, passport);

        int rowsUpdated = db.update(
                TABLE_PASSENGERS,
                values,
                COL_PASSENGER_ID + "=?",
                new String[]{id}
        );

        return rowsUpdated > 0;
    }

}
