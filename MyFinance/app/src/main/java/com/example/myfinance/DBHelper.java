package com.example.myfinance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME    = "myfinance.db";
    private static final int    DB_VERSION = 2;

    // users
    public static final String TABLE_USER      = "users";
    public static final String COL_USER_ID     = "id";
    public static final String COL_USERNAME    = "username";
    public static final String COL_PASSWORD    = "password";

    // categories
    public static final String TABLE_CATEGORY  = "categories";
    public static final String COL_CAT_ID      = "id";
    public static final String COL_CAT_NAME    = "name";

    // expenses
    public static final String TABLE_EXPENSE   = "expenses";
    public static final String COL_EXP_ID      = "id";
    public static final String COL_EXP_AMOUNT  = "amount";
    public static final String COL_EXP_MONTH   = "month";
    public static final String COL_EXP_DATE    = "date";         // ← নতুন
    public static final String COL_EXP_CAT_ID  = "category_id";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + " ("
                + COL_USER_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " TEXT UNIQUE, "
                + COL_PASSWORD + " TEXT"
                + ")");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " ("
                + COL_CAT_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_CAT_NAME + " TEXT UNIQUE"
                + ")");

        db.execSQL("CREATE TABLE " + TABLE_EXPENSE + " ("
                + COL_EXP_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EXP_AMOUNT + " REAL, "
                + COL_EXP_MONTH  + " TEXT, "
                + COL_EXP_DATE   + " TEXT, "
                + COL_EXP_CAT_ID + " INTEGER, "
                + "FOREIGN KEY(" + COL_EXP_CAT_ID + ") REFERENCES "
                + TABLE_CATEGORY + "(" + COL_CAT_ID + ")"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // ভের্শন ১ → ২ এ মাইগ্রেশন: date কলাম অ্যাড
            db.execSQL("ALTER TABLE " + TABLE_EXPENSE
                    + " ADD COLUMN " + COL_EXP_DATE + " TEXT DEFAULT ''");
        }
    }
}
