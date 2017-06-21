package com.example.yass.country.data;

import android.provider.BaseColumns;

/**
 * Created by yass on 6/20/17.
 */

public class CountryContract {

    public static final class CountryEntry implements BaseColumns {
        public static final String TABLE_NAME = "country";
        public static final String COLUMN_COUNTRY_NAME = "countryName";
        public static final String COLUMN_CITY = "cities";
    }
}
