package com.example.rest_api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import java.util.ArrayList;
import java.util.List;

public class DataBaseManager extends SQLiteOpenHelper {

    public DataBaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    private static final String DATABASE_NAME = "DB_CONVERTER";
    private static final String RATES_SPECIFIC = "RATES_SPECIFIC";
    private static final String RATES = "RATES";



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE RATES (ID INTEGER PRIMARY KEY AUTOINCREMENT, COD TEXT, RATE TEXT);");
        db.execSQL("CREATE TABLE RATES_SPECIFIC (ID INTEGER PRIMARY KEY AUTOINCREMENT, RATE TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addCurrencyRate(String cod, String rate) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COD", cod);
        values.put("RATE", rate);
        database.insert("RATES", null, values);
    }

    public void addCurrencyRateForChart(String rate) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("RATE", rate);
        database.insert("RATES_SPECIFIC", null, values);
    }

    public String getRateValue(String currency) {
        SQLiteDatabase database = getReadableDatabase();
        String rate="";
        Cursor cursor = database.query("RATES", new String[] {"RATE", "COD"}, "COD = ?", new String[] {currency}, null, null, null );

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            rate = cursor.getString(cursor.getColumnIndex("RATE"));
        }
        return rate;
    }

    public Cursor getRateValueForChart() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query("RATES_SPECIFIC", new String[] {"RATE"}, null, null, null, null, null );
        return cursor;
    }

    public void truncateRatesSpecificTable(String table) {
        SQLiteDatabase database = getReadableDatabase();
        database.execSQL("DELETE FROM "+table+";");
    }
}
