package com.example.yass.country.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yass.country.adapters.CountryListAdapter;
import com.example.yass.country.R;
import com.example.yass.country.data.CountryContract;
import com.example.yass.country.data.CountryDbHelper;
import com.example.yass.country.utilities.DbUtils;
import com.example.yass.country.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements CountryListAdapter.CountryListAdapterOnClickHandler {

    public static final String COUNTRY = "country";

    private ProgressBar loadingIndicator;

    public static SQLiteDatabase dataBase;

    private RecyclerView countryRecyclerView;
    private CountryListAdapter countryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        CountryDbHelper dbHelper = new CountryDbHelper(this);
        dataBase = dbHelper.getWritableDatabase();

        countryRecyclerView = (RecyclerView) findViewById(R.id.country_recycler_list);
        countryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor = DbUtils.getAllCountry(dataBase);

        countryListAdapter = new CountryListAdapter(this, cursor, this);
        cursor.close();
        countryRecyclerView.setAdapter(countryListAdapter);

        makeSearchQuery();
    }

    private void makeSearchQuery() {
        URL searchUrl = NetworkUtils.buildUrl();

        new QueryTask().execute(searchUrl);
    }

    @Override
    public void onClick(String country) {
        Intent intent = new Intent(this, CitiesActivity.class);
        intent.putExtra(COUNTRY, country);

        startActivity(intent);
    }

    public class QueryTask extends AsyncTask<URL, Void, String> {

        ContentValues cv = new ContentValues();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
            countryRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            loadingIndicator.setVisibility(View.INVISIBLE);


            if (searchResults != null && !searchResults.equals("")) {

                JSONObject dataJsonObj = null;
                String country = null;
                JSONArray cities = null;
                dataBase.delete(CountryContract.CountryEntry.TABLE_NAME, null, null);
                try {
                    dataJsonObj = new JSONObject(searchResults);

                    Iterator<?> keys = dataJsonObj.keys();

                    while (keys.hasNext()) {
                        country = String.valueOf(keys.next());
                        cities = dataJsonObj.getJSONArray(country);

                        cv.put(CountryContract.CountryEntry.COLUMN_COUNTRY_NAME, country);
                        cv.put(CountryContract.CountryEntry.COLUMN_CITY, cities.toString());

                        dataBase.insert(CountryContract.CountryEntry.TABLE_NAME, null, cv);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                countryRecyclerView.setVisibility(View.VISIBLE);
                countryListAdapter.swapCursor(DbUtils.getAllCountry(dataBase));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showToast();
    }

    private void showToast() {
        Toast.makeText(this, "Countries", Toast.LENGTH_SHORT).show();
    }

}
