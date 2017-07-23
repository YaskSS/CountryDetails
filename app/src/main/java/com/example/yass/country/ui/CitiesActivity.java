package com.example.yass.country.ui;

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

import com.example.yass.country.adapters.CityListAdapter;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CitiesActivity extends AppCompatActivity implements
        CityListAdapter.CityListAdapterOnClickHandler {

    public static final String DETAIL_CITY = "detail";

    private RecyclerView citiesRecyclerView;
    private CityListAdapter cityListAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        String countryName = getIntent().getStringExtra(MainActivity.COUNTRY);

        citiesRecyclerView = (RecyclerView) findViewById(R.id.city_recycler_view);
        citiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cityListAdapter = new CityListAdapter(this, getAllCities(countryName), this);
        citiesRecyclerView.setAdapter(cityListAdapter);

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_detail_indicator);

        Toast.makeText(this, "Cities", Toast.LENGTH_SHORT).show();

    }

    private LinkedList<String> getAllCities(String country) {
        LinkedList<String> citiesList = new LinkedList<>();
        Cursor cursor = DbUtils.getCities(MainActivity.dataBase, country);
        JSONArray jsonArray = null;
        String json = "";
        ArrayList<String> arrayList = new ArrayList<String>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(CountryContract.CountryEntry.COLUMN_CITY)));
        }

        for (int i = 0; i < arrayList.size(); i++) {
            json = json + arrayList.get(i);
        }

        try {
            jsonArray = new JSONArray(json);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                citiesList.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return citiesList;
    }

    @Override
    public void onClick(String city) {
        new AsyncTask<URL, Void, String>() {
            LinkedList<String> keysJsonObject = new LinkedList<>();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showIndicatorLoading();
            }

            @Override
            protected String doInBackground(URL... urls) {
                URL searchUrl = urls[0];
                String searchResult = null;
                try {
                    searchResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return searchResult;
            }

            @Override
            protected void onPostExecute(String result) {
                JSONObject resultJsonObject = null;
                if (result != null && !result.equals("")) {
                    goneIndicatorLoading();
                    try {
                        resultJsonObject = new JSONObject(result);
                        Iterator<?> keys = resultJsonObject.keys();

                        while (keys.hasNext()) {
                            keysJsonObject.add(String.valueOf(keys.next()));

                        }

                      startDetailCity(resultJsonObject.getString(keysJsonObject.get(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(NetworkUtils.buildUrl(city));
    }

    private void startDetailCity(String detailCity){
        Intent intent = new Intent(this, DetailCityActivity.class).
                putExtra(DETAIL_CITY, detailCity);
        startActivity(intent);
    }

    private void showIndicatorLoading(){
        progressBar.setVisibility(View.VISIBLE);
        citiesRecyclerView.setVisibility(View.GONE);
    }

    private void goneIndicatorLoading(){
        progressBar.setVisibility(View.GONE);
        citiesRecyclerView.setVisibility(View.VISIBLE);
    }
}
