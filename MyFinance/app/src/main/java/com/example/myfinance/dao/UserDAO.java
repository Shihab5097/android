package com.example.myfinance.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfinance.DBHelper;
import com.example.myfinance.model.User;

public class UserDAO {
    private DBHelper helper;

    public UserDAO(Context ctx) {
        helper = new DBHelper(ctx);
    }

    public boolean register(User u) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_USERNAME, u.getUsername());
        cv.put(DBHelper.COL_PASSWORD, u.getPassword());
        long id = db.insert(DBHelper.TABLE_USER, null, cv);
        return id != -1;
    }

    public User login(String username, String password) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
                DBHelper.TABLE_USER,
                null,
                DBHelper.COL_USERNAME + "=? AND " + DBHelper.COL_PASSWORD + "=?",
                new String[]{ username, password },
                null, null, null
        );

        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    User u = new User();
                    int idx = c.getColumnIndexOrThrow(DBHelper.COL_USER_ID);
                    u.setId(c.getInt(idx));
                    u.setUsername(username);
                    u.setPassword(password);
                    return u;
                }
            } finally {
                c.close();
            }
        }

        return null;
    }
}
