package com.example.vitnhtk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Diary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "diary";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_POSITIVE = "positive";
    private static final String COLUMN_NEGATIVE = "negative";
    private static final String COLUMN_PROGRESS = "progress";
    private static final String COLUMN_LEARNING = "learning";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE + " TEXT PRIMARY KEY, " +
                COLUMN_POSITIVE + " TEXT, " +
                COLUMN_NEGATIVE + " TEXT, " +
                COLUMN_PROGRESS + " TEXT, " +
                COLUMN_LEARNING + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean saveDiary(String date, String positive, String negative, String progress, String learning) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_POSITIVE, positive);
        values.put(COLUMN_NEGATIVE, negative);
        values.put(COLUMN_PROGRESS, progress);
        values.put(COLUMN_LEARNING, learning);

        long result = db.replace(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getDiaryByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + "=?", new String[]{date});
    }
}
