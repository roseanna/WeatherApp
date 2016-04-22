package com.example.roseanna.weatherapp_json;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText cityName;
    private ListView weatherLV;
    private Button findButton;
    private String city;
    private ArrayList<WeatherDay> weatherArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = (EditText) findViewById(R.id.cityName);
        findButton = (Button) findViewById(R.id.find);
        weatherLV = (ListView) findViewById(R.id.listview);
        findButton.setOnClickListener(this);
        weatherArray = new ArrayList<>();

    }

    public void onClick(View v) {
        city = cityName.getText().toString();
        String cityURL = city;
        if (cityURL.contains(" ")){
            cityURL = cityURL.replace(" ","");
        }
        if (cityURL.length() < 1) {
            Toast.makeText(this, "Please enter a city", Toast.LENGTH_SHORT).show();
            return;
        }
        WeatherScrape ws = new WeatherScrape();
        ws.execute(cityURL);
    }

    public void populate() {
        if (!cityFound()) {
            Toast.makeText(this, "City not found, try again", Toast.LENGTH_SHORT).show();
            weatherArray.clear();
        }
        update();
    }


    public void update(){
        WeatherAdapter wa = new WeatherAdapter(this, weatherArray);
        weatherLV.setAdapter(wa);
    }
    public boolean cityFound(){
        WeatherDay tester = weatherArray.get(0);
        String cityName = tester.getVal("city");
        if (cityName.toLowerCase().equals(city.toLowerCase()))
            return true;
        return false;
    }
    private class WeatherScrape extends AsyncTask<String, String, String>{
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        StringBuilder result = new StringBuilder();

        protected void onPreExecute() {
            progressDialog.setMessage("Downloading your data...");
            progressDialog.show();
            if (!weatherArray.isEmpty())
                weatherArray.clear();

        }

        @Override
        protected String doInBackground(String... params) {
            String jsonUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + params[0] +
                    "&appid=db2984e6c6fc42705b18364b786a4d09&units=imperial&cnt=3";
            try {
                URL url = new URL(jsonUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null){
                    result.append(line);
                }

                JSONObject jObject      = new JSONObject(result.toString());
                JSONObject cityInfo     = jObject.getJSONObject("city");
                JSONArray jArray = jObject.getJSONArray("list");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject singleDay = jArray.getJSONObject(i);
                    WeatherDay wd = new WeatherDay(cityInfo, singleDay);
                    weatherArray.add(wd);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result){
            progressDialog.dismiss();
            populate();
        }
    }
}
