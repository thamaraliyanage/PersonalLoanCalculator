package com.imat.personalloancalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CalculationHistory.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Calculations";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_AMOUNT = "amount";
    static final String COLUMN_TERM = "term";
    static final String COLUMN_RATE = "rate";
    static final String COLUMN_MONTHLY_REPAYMENT = "monthly_repayment";
    static final String COLUMN_TOTAL_PAYABLE_AMOUNT = "total_payable_amount";


    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_AMOUNT + " REAL, " +
                        COLUMN_TERM + " INTEGER, " +
                        COLUMN_RATE + " REAL, " +
                        COLUMN_MONTHLY_REPAYMENT + " REAL, " +
                        COLUMN_TOTAL_PAYABLE_AMOUNT + " REAL);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addCalculation(double amount, int term, double rate, double monthly_payment, double total_payable_amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_AMOUNT, amount);
        cv.put(COLUMN_TERM, term);
        cv.put(COLUMN_RATE, rate);
        cv.put(COLUMN_MONTHLY_REPAYMENT, monthly_payment);
        cv.put(COLUMN_TOTAL_PAYABLE_AMOUNT, total_payable_amount);

        db.insert(TABLE_NAME, null, cv);
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
    }
}
