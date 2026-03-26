package com.group9.ongo.persistence.real;

import static com.group9.ongo.persistence.real.AppDbHelper.COL_USER_EMAIL;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_USER_ID;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_USER_PASSWORD;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_USER_PHONE;
import static com.group9.ongo.persistence.real.AppDbHelper.COL_USER_USERNAME;
import static com.group9.ongo.persistence.real.AppDbHelper.TABLE_USERS;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.group9.ongo.models.User;
import com.group9.ongo.persistence.UserRepository;

public class SqlUserRepository implements UserRepository {


    private AppDbHelper dbHelper;

    public SqlUserRepository(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    @Override
    public User getUserById(int userId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        User user = null;

        if (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_USERNAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PHONE));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD));

            user = new User(id, username, email, phone, password);
        }

        cursor.close();

        return user;
    }

    @Override
    public int addUser(String name, String email, String phone, String password) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USER_USERNAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PHONE, phone);
        values.put(COL_USER_PASSWORD, password);

        long id = db.insert(TABLE_USERS, null, values);

        if (id == -1) {
            return -1; // insert failed
        }

        return (int) id;
    }

    @Override
    public boolean deleteUser(int userId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_USERS,
                COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)}
        );

        return rowsDeleted > 0;
    }

    @Override
    public int findUserIDByEmailAndPassword(String name, String password) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{name, password},
                null,
                null,
                null
        );

        int userId = -1;

        if (cursor.moveToNext()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
        }

        cursor.close();

        return userId;
    }

}
