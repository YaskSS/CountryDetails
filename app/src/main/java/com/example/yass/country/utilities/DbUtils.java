package com.example.yass.country.utilities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yass.country.data.CountryContract;
import com.example.yass.country.data.CountryDbHelper;

/**
 * Created by yass on 6/20/17.
 */

public class DbUtils {

    public static Cursor getAllCountry(SQLiteDatabase database) {
        return database.query(
                CountryContract.CountryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static Cursor getCities(SQLiteDatabase database, String country){
       return database.rawQuery("select * from " +
               CountryContract.CountryEntry.TABLE_NAME +
               " where " + CountryContract.CountryEntry.COLUMN_COUNTRY_NAME +" like ?",
               new String[] {country});
    }
}
