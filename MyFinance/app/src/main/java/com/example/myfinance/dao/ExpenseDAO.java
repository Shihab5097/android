package com.example.myfinance.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfinance.model.Expense;
import com.example.myfinance.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private DBHelper helper;

    public ExpenseDAO(Context ctx) {
        helper = new DBHelper(ctx);
    }

    // Add a new expense
    public long addExpense(Expense e) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_EXP_DATE,    e.getDate());
        cv.put(DBHelper.COL_EXP_MONTH,   e.getMonth());
        cv.put(DBHelper.COL_EXP_CAT_ID,  e.getCategoryId());
        cv.put(DBHelper.COL_EXP_AMOUNT,  e.getAmount());
        long id = db.insert(DBHelper.TABLE_EXPENSE, null, cv);
        db.close();
        return id;
    }

    // Get all expenses
    public List<Expense> getAll() {
        List<Expense> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
                DBHelper.TABLE_EXPENSE, null,
                null, null, null, null,
                DBHelper.COL_EXP_DATE + " ASC"
        );
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    Expense e = new Expense();
                    e.setId(c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_EXP_ID)));
                    e.setDate(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_EXP_DATE)));
                    e.setMonth(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_EXP_MONTH)));
                    e.setCategoryId(c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_EXP_CAT_ID)));
                    e.setAmount(c.getDouble(c.getColumnIndexOrThrow(DBHelper.COL_EXP_AMOUNT)));
                    list.add(e);
                }
            } finally {
                c.close();
                db.close();
            }
        }
        return list;
    }

    // Get by month (for AddExpenseActivity)
    public List<Expense> getByMonth(String month) {
        List<Expense> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
                DBHelper.TABLE_EXPENSE, null,
                DBHelper.COL_EXP_MONTH + " = ?",
                new String[]{ month },
                null, null,
                DBHelper.COL_EXP_DATE + " ASC"
        );
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    Expense e = new Expense();
                    e.setId(c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_EXP_ID)));
                    e.setDate(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_EXP_DATE)));
                    e.setMonth(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_EXP_MONTH)));
                    e.setCategoryId(c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_EXP_CAT_ID)));
                    e.setAmount(c.getDouble(c.getColumnIndexOrThrow(DBHelper.COL_EXP_AMOUNT)));
                    list.add(e);
                }
            } finally {
                c.close();
                db.close();
            }
        }
        return list;
    }

    // Get by month and year (for DashboardActivity)
    public List<Expense> getByMonthAndYear(String month, int year) {
        List<Expense> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sel = DBHelper.COL_EXP_MONTH + " = ? AND " +
                DBHelper.COL_EXP_DATE + " LIKE ?";
        String yearPattern = year + "-%";  // e.g. "2025-%"
        Cursor c = db.query(
                DBHelper.TABLE_EXPENSE, null,
                sel,
                new String[]{ month, yearPattern },
                null, null,
                DBHelper.COL_EXP_DATE + " ASC"
        );
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    Expense e = new Expense();
                    e.setId(c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_EXP_ID)));
                    e.setDate(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_EXP_DATE)));
                    e.setMonth(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_EXP_MONTH)));
                    e.setCategoryId(c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_EXP_CAT_ID)));
                    e.setAmount(c.getDouble(c.getColumnIndexOrThrow(DBHelper.COL_EXP_AMOUNT)));
                    list.add(e);
                }
            } finally {
                c.close();
                db.close();
            }
        }
        return list;
    }

    // Get by category AND month (CategoryBreakdownActivity)
    public List<Expense> getByCategoryAndMonth(int catId, String month) {
        List<Expense> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sel = DBHelper.COL_EXP_MONTH + " = ? AND " + DBHelper.COL_EXP_CAT_ID + " = ?";
        Cursor c = db.query(
                DBHelper.TABLE_EXPENSE, null,
                sel,
                new String[]{ month, String.valueOf(catId) },
                null, null,
                DBHelper.COL_EXP_DATE + " ASC"
        );
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    Expense e = new Expense();
                    e.setId(c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_EXP_ID)));
                    e.setDate(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_EXP_DATE)));
                    e.setMonth(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_EXP_MONTH)));
                    e.setCategoryId(c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_EXP_CAT_ID)));
                    e.setAmount(c.getDouble(c.getColumnIndexOrThrow(DBHelper.COL_EXP_AMOUNT)));
                    list.add(e);
                }
            } finally {
                c.close();
                db.close();
            }
        }
        return list;
    }

    // Update & Delete...
    public int updateExpense(Expense e) { /* ... */ return 0; }
    public int deleteExpense(int id)   { /* ... */ return 0; }
}
