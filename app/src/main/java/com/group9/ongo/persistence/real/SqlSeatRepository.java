package com.group9.ongo.persistence.real;

import static com.group9.ongo.persistence.real.AppDbHelper.COL_SEAT_COLUMN;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_SEAT_FLIGHT_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_SEAT_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_SEAT_IS_BOOKED;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_SEAT_ROW;
import static com.group9.ongo.persistence.real.AppDbHelper.TABLE_SEATS;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.group9.ongo.models.Seat;
import com.group9.ongo.persistence.SeatRepository;

import java.util.ArrayList;
import java.util.List;

public class SqlSeatRepository implements SeatRepository {

    private final AppDbHelper dbHelper;

    public SqlSeatRepository(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<Seat> getSeatsByFlightId(int flight_id) {
        List<Seat> seats = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_SEATS,
                null,
                COL_SEAT_FLIGHT_ID + " = ?",
                new String[]{String.valueOf(flight_id)},
                null,
                null,
                COL_SEAT_ROW + " ASC, " + COL_SEAT_COLUMN + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                seats.add(mapCursorToSeat(cursor));
            }
            cursor.close();
        }

        return seats;
    }

    @Override
    public Seat getSeatById(int flight_id, int seat_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_SEATS,
                null,
                COL_SEAT_FLIGHT_ID + " = ? AND " + COL_SEAT_ID + " = ?",
                new String[]{String.valueOf(flight_id), String.valueOf(seat_id)},
                null,
                null,
                null
        );

        Seat seat = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                seat = mapCursorToSeat(cursor);
            }
            cursor.close();
        }

        return seat;
    }

    @Override
    public int createSeat(int flight_id, int row, String column) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_SEAT_FLIGHT_ID, flight_id);
        values.put(COL_SEAT_ROW, row);
        values.put(COL_SEAT_COLUMN, column);
        values.put(COL_SEAT_IS_BOOKED, 0);

        long seatId = db.insert(TABLE_SEATS, null, values);

        return (int) seatId;
    }

    @Override
    public Seat findSeat(int flightId, int seatRow, String seatColumn) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_SEATS,
                null,
                COL_SEAT_FLIGHT_ID + " = ? AND " +
                        COL_SEAT_ROW + " = ? AND " +
                        COL_SEAT_COLUMN + " = ?",
                new String[]{
                        String.valueOf(flightId),
                        String.valueOf(seatRow),
                        seatColumn
                },
                null,
                null,
                null
        );

        Seat seat = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                seat = mapCursorToSeat(cursor);
            }
            cursor.close();
        }

        return seat;
    }

    @Override
    public void bookSeat(int seatId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_SEAT_IS_BOOKED, 1);

        db.update(
                TABLE_SEATS,
                values,
                COL_SEAT_ID + " = ?",
                new String[]{String.valueOf(seatId)}
        );
    }

    @Override
    public void unBookSeat(int seatId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_SEAT_IS_BOOKED, 0);

        db.update(
                TABLE_SEATS,
                values,
                COL_SEAT_ID + " = ?",
                new String[]{String.valueOf(seatId)}
        );
    }

    private Seat mapCursorToSeat(Cursor cursor) {
        int seatId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEAT_ID));
        int flightId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEAT_FLIGHT_ID));
        int seatRow = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEAT_ROW));
        String seatColumn = cursor.getString(cursor.getColumnIndexOrThrow(COL_SEAT_COLUMN));
        boolean isBooked = cursor.getInt(
                cursor.getColumnIndexOrThrow(COL_SEAT_IS_BOOKED)
        ) == 1;

        return new Seat(seatId, flightId, seatRow, seatColumn, isBooked);
    }
}