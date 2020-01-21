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

        //find all - primary key id 14
        db.execSQL("CREATE TABLE IF NOT EXISTS STATION (\n" +
                "    `list_id` INT UNIQUE,\n" +
                "    `list_stationName` TEXT,\n" +
                "    `list_gegrLat` REAL,\n" +
                "    `list_gegrLon` REAL,\n" +
                "    `list_city_id` INT,\n" +
                "    `list_city_name` TEXT,\n" +
                "    `list_city_commune_communeName` TEXT,\n" +
                "    `list_city_commune_districtName` TEXT,\n" +
                "    `list_city_commune_provinceName` TEXT,\n" +
                "    `list_addressStreet` TEXT);");

        //foreign key to stationId from find all
        //list_is primary key 92
        db.execSQL("CREATE TABLE IF NOT EXISTS MEASURING_STATION (\n" +
                "    `list_id` INT,\n" +
                "    `list_stationId` INT,\n" +
                "    `list_param_paramName` TEXT,\n" +
                "    `list_param_paramFormula` TEXT,\n" +
                "    `list_param_paramCode` TEXT,\n" +
                "    `list_param_idParam` INT);");

        //foreign key to 92 - dodac kolumne
        //id zwykle to primary key autoincrement
        db.execSQL("CREATE TABLE IF NOT EXISTS SENSOR_DATA (\n" +
                "    `list_id` INT,\n" +
                "    `key` TEXT,\n" +
                "    `values_date` TEXT,\n" +
                "    `values_value` REAL\n" +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addSensorDataByStationId(int list_id, String key, String values_date, String values_value) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("list_id", list_id);
        values.put("key", key);
        values.put("values_date", values_date);
        values.put("values_value", values_value);

        database.insert("SENSOR_DATA", null, values);
    }

    public void addFindAllData( int id,
                                String stationName,
                                String gegrLat,
                                String gegrLon,
                                int list_city_id,
                                String list_city_name,
                                String list_city_commune_communeName,
                                String list_city_commune_districtName,
                                String list_city_commune_provinceName,
                                String list_addressStreet ) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("list_id", id);
        values.put("list_stationName", stationName);
        values.put("list_gegrLat", gegrLat);
        values.put("list_gegrLon", gegrLon);

        values.put("list_city_id", list_city_id);
        values.put("list_city_name", list_city_name);
        values.put("list_city_commune_communeName", list_city_commune_communeName);
        values.put("list_city_commune_districtName", list_city_commune_districtName);
        values.put("list_city_commune_provinceName", list_city_commune_provinceName);

        values.put("list_addressStreet", list_addressStreet);


        database.insert("STATION", null, values);
    }

    public void addDataStationById( int list_id,
                                    int list_stationId,
                                    String list_param_paramName,
                                    String list_param_paramFormula,
                                    String list_param_paramCode,
                                    int list_param_idParam) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("list_id", list_id);
        values.put("list_stationId", list_stationId);
        values.put("list_param_paramName", list_param_paramName);
        values.put("list_param_paramFormula", list_param_paramFormula);

        values.put("list_param_paramCode", list_param_paramCode);
        values.put("list_param_idParam", list_param_idParam);

        database.insert("MEASURING_STATION", null, values);

    }

    public int getTableCount(String tableName) {
        SQLiteDatabase database = getReadableDatabase();
        ContentValues values = new ContentValues();
        int count = 0;
        Cursor cursor = database.query(tableName, null, null,null, null, null, null);
        return cursor.getCount();
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
