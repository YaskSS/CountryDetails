package com.example.yass.country.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yass on 6/20/17.
 */

public class CountryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "countries.db";

    private static final int DATABASE_VERSION = 1;

    public CountryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_COUNTRIES_TABLE = "CREATE TABLE " + CountryContract.CountryEntry.TABLE_NAME + " (" +
                CountryContract.CountryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CountryContract.CountryEntry.COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                CountryContract.CountryEntry.COLUMN_CITY  + " TEXT NOT NULL" +
                "); " ;

        sqLiteDatabase.execSQL(SQL_CREATE_COUNTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CountryContract.CountryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
