package com.group9.ongo.persistence.real;

import static com.group9.ongo.persistence.real.AppDbHelper.COL_BOOKING_FLIGHT_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_BOOKING_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_BOOKING_SEAT_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_BOOKING_USER_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.TABLE_BOOKINGS;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.group9.ongo.models.Booking;
import com.group9.ongo.persistence.BookingRepository;

import java.util.ArrayList;
import java.util.List;

public class SqlBookingRepository implements BookingRepository {

    private final AppDbHelper dbHelper;
    public SqlBookingRepository(AppDbHelper adHelper) {
        this.dbHelper = adHelper;
    }

    @Override
    public List<Booking> getBookingByUserId(int userId) {

        List<Booking> userBookings = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_BOOKINGS,
                null,
                COL_BOOKING_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_ID));
            int flightId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_FLIGHT_ID));
            int uId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_USER_ID));
            int seatId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_SEAT_ID));

            Booking booking = new Booking(id, uId, flightId, seatId);

            userBookings.add(booking);
        }

        cursor.close();

        return userBookings;
    }

    @Override
    public Booking addBooking(Booking booking) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_BOOKING_USER_ID, booking.getUserId());
        values.put(COL_BOOKING_FLIGHT_ID, booking.getFlightId());
        values.put(COL_BOOKING_SEAT_ID, booking.getSeatId());

        long id = db.insert(TABLE_BOOKINGS, null, values);

        if (id == -1) {
            return null; // insert failed
        }

        return new Booking((int) id, booking.getUserId(), booking.getFlightId(), booking.getSeatId());
    }

    @Override
    public Booking getBookingById(int bookingId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_BOOKINGS,
                null,
                COL_BOOKING_ID + "=?",
                new String[]{String.valueOf(bookingId)},
                null,
                null,
                null
        );

        Booking booking = null;

        if (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_ID));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_USER_ID));
            int flightId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_FLIGHT_ID));
            int seatId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOKING_SEAT_ID));

            booking = new Booking(id, userId, flightId, seatId);
        }

        cursor.close();

        return booking;
    }


    @Override
    public boolean deleteBooking(int bookingId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_BOOKINGS,
                COL_BOOKING_ID + "=?",
                new String[]{String.valueOf(bookingId)}
        );

        return rowsDeleted > 0;
    }
}
