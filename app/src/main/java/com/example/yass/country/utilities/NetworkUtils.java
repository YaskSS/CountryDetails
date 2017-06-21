package com.example.yass.country.utilities;

import android.content.ContentValues;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by yass on 6/20/17.
 */

public class NetworkUtils {

    final static String COUNTY_BASE_URL =
            "https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json";

    final static String CITY_BASE_URL =
            "http://api.geonames.org/wikipediaSearchJSON?";

    final static String USER_NAME = "serhey";
    final static String PARAM_QUERY_CITY = "q";
    final static String PARAM_QUERY_ROWS = "maxRows";
    final static String PARAM_QUERY_USER = "username";

    public static URL buildUrl() {

        URL url = null;
        try {
            url = new URL(COUNTY_BASE_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrl(String city){
        URL url = null;

        Uri buildUri = Uri.parse(CITY_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY_CITY, city)
                .appendQueryParameter(PARAM_QUERY_ROWS, "10")
                .appendQueryParameter(PARAM_QUERY_USER, USER_NAME)
                .build();

        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = null;

        try {

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            return buffer.toString();

        } finally {
            urlConnection.disconnect();
        }
    }
}
