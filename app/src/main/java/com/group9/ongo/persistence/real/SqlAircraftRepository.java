package com.group9.ongo.persistence.real;

import static com.group9.ongo.persistence.real.AppDbHelper.COL_AIRCRAFT_CAPACITY;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_AIRCRAFT_HAS_WIFI;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_AIRCRAFT_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_AIRCRAFT_MODEL_NAME;
import static com.group9.ongo.persistence.real.AppDbHelper.TABLE_AIRCRAFT;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.group9.ongo.models.Aircraft;
import com.group9.ongo.persistence.AircraftRepository;

public class SqlAircraftRepository implements AircraftRepository {

    private AppDbHelper dbHelper;

    public SqlAircraftRepository(AppDbHelper adHelper) {
        this.dbHelper = adHelper;
    }

    @Override
    public Aircraft getAircraftById(int aircraftId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_AIRCRAFT,
                null,
                COL_AIRCRAFT_ID + " = ?",
                new String[]{String.valueOf(aircraftId)},
                null,
                null,
                null
        );

        Aircraft aircraft = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_AIRCRAFT_ID));
                String model = cursor.getString(cursor.getColumnIndexOrThrow(COL_AIRCRAFT_MODEL_NAME));
                int capacity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_AIRCRAFT_CAPACITY));
                boolean hasWifi = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COL_AIRCRAFT_HAS_WIFI)
                ) == 1;

                aircraft = new Aircraft(id, model, capacity, hasWifi);
            }

            cursor.close();
        }

        return aircraft;
    }

    @Override
    public Aircraft addAircraft(Aircraft aircraft) {

        if (aircraft == null) return null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_AIRCRAFT_MODEL_NAME, aircraft.getModelName());
        values.put(COL_AIRCRAFT_CAPACITY, aircraft.getCapacity());
        values.put(COL_AIRCRAFT_HAS_WIFI, aircraft.hasWifi() ? 1 : 0);

        long id = db.insert(TABLE_AIRCRAFT, null, values);

        if (id == -1) return null;

        return new Aircraft(
                (int) id,
                aircraft.getModelName(),
                aircraft.getCapacity(),
                aircraft.hasWifi()
        );
    }
}
