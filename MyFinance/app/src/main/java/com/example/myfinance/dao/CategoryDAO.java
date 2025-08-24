package com.example.myfinance.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfinance.DBHelper;
import com.example.myfinance.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private DBHelper helper;

    public CategoryDAO(Context ctx) {
        helper = new DBHelper(ctx);
    }

    /** 새 카테고리 저장 **/
    public long addCategory(Category c) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_CAT_NAME, c.getName());
        long id = db.insert(DBHelper.TABLE_CATEGORY, null, cv);
        db.close();
        return id;
    }

    /** 모든 카테고리 조회 **/
    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
                DBHelper.TABLE_CATEGORY,
                null,           // all columns
                null, null,     // no WHERE
                null, null,
                DBHelper.COL_CAT_NAME + " ASC"
        );
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    Category cat = new Category();
                    cat.setId(c.getInt(c.getColumnIndexOrThrow(DBHelper.COL_CAT_ID)));
                    cat.setName(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_CAT_NAME)));
                    list.add(cat);
                }
            } finally {
                c.close();
                db.close();
            }
        }
        return list;
    }

    /** ID দিয়ে একক ক্যাটাগরি **/
    public Category getById(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
                DBHelper.TABLE_CATEGORY,
                null,
                DBHelper.COL_CAT_ID + " = ?",
                new String[]{ String.valueOf(id) },
                null, null, null
        );
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    Category cat = new Category();
                    cat.setId(id);
                    cat.setName(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_CAT_NAME)));
                    return cat;
                }
            } finally {
                c.close();
                db.close();
            }
        }
        return null;
    }

    /** ক্যাটাগরি আপডেট **/
    public int updateCategory(Category c) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_CAT_NAME, c.getName());
        int rows = db.update(
                DBHelper.TABLE_CATEGORY,
                cv,
                DBHelper.COL_CAT_ID + " = ?",
                new String[]{ String.valueOf(c.getId()) }
        );
        db.close();
        return rows;
    }

    /** ক্যাটাগরি ডিলিট **/
    public int deleteCategory(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rows = db.delete(
                DBHelper.TABLE_CATEGORY,
                DBHelper.COL_CAT_ID + " = ?",
                new String[]{ String.valueOf(id) }
        );
        db.close();
        return rows;
    }
}
