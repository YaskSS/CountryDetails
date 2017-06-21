package com.example.yass.country.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yass.country.R;
import com.example.yass.country.ui.CitiesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailCityActivity extends AppCompatActivity {

    private TextView summaryTextView;
    private TextView titleTextView;
    private TextView languageTextView;
    private TextView codeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_city);
        String detailCity = getIntent().getStringExtra(CitiesActivity.DETAIL_CITY);

        summaryTextView = (TextView) findViewById(R.id.summary_text_view);
        titleTextView = (TextView) findViewById(R.id.title_text_view);
        languageTextView = (TextView) findViewById(R.id.lang_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);

        showDetail(detailCity);

        Toast.makeText(this, "City details", Toast.LENGTH_SHORT).show();
    }

    private void showDetail(String detail) {

        JSONObject jsonObject = null;
        String summary = null;
        String title = null;
        String lang = null;
        String code = null;
        JSONArray jsonArray = null;
        try {

            jsonArray = new JSONArray(detail);
            jsonObject = jsonArray.getJSONObject(1);

            summary = jsonObject.getString("summary");
            title = jsonObject.getString("title");
            lang = jsonObject.getString("lang");
            code = jsonObject.getString("countryCode");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        summaryTextView.setText(summary);
        titleTextView.setText(title);
        languageTextView.setText(lang);
        codeTextView.setText(code);
    }
}
