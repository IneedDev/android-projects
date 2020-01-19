package rest_api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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

        db.execSQL("CREATE TABLE STATION (ID_STATION INTEGER PRIMARY KEY, STATION_NAME TEXT, LAT REAL, LON REAL)");
        db.execSQL("CREATE TABLE STATION_SENSOR (ID_STATION_SENSOR INTEGER PRIMARY KEY, ID_STATION INTEGER, FOREIGN KEY (ID_STATION) REFERENCES STATION (ID_STATION) )");


        //find all - primary key id 14
        db.execSQL("CREATE TABLE IF NOT EXISTS findAll (\n" +
                "    `list_id` INT,\n" +
                "    `list_stationName` VARCHAR(44),\n" +
                "    `list_gegrLat` NUMERIC(8, 6),\n" +
                "    `list_gegrLon` NUMERIC(8, 6),\n" +
                "    `list_city_id` INT,\n" +
                "    `list_city_name` VARCHAR(23),\n" +
                "    `list_city_commune_communeName` VARCHAR(23),\n" +
                "    `list_city_commune_districtName` VARCHAR(23),\n" +
                "    `list_city_commune_provinceName` VARCHAR(19),\n" +
                "    `list_addressStreet` VARCHAR(37)\n" +
                ");");

        //foreign key to stationId from find all
        //list_is primary key 92
        db.execSQL("CREATE TABLE IF NOT EXISTS 14 (\n" +
                "    `list_id` INT,\n" +
                "    `list_stationId` INT,\n" +
                "    `list_param_paramName` VARCHAR(19) CHARACTER SET utf8,\n" +
                "    `list_param_paramFormula` VARCHAR(4) CHARACTER SET utf8,\n" +
                "    `list_param_paramCode` VARCHAR(4) CHARACTER SET utf8,\n" +
                "    `list_param_idParam` INT\n" +
                ");");

        //foreign key to 92 - dodac kolumne
        //id zwykle to primary key autoincrement
        db.execSQL("CREATE TABLE IF NOT EXISTS 92 (\n" +
                "    `key` VARCHAR(4) CHARACTER SET utf8,\n" +
                "    `values_date` DATETIME,\n" +
                "    `values_value` NUMERIC(7, 5)\n" +
                ");");

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
