package com.example.roseanna.weatherapp_json;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by roseanna on 4/10/16.
 */
public class WeatherDay {
    private JSONObject js, cityInfo;
    private HashMap<String, String> items;
    public WeatherDay(JSONObject cityInfo, JSONObject js){
        this.cityInfo = cityInfo;
        this.js = js;
        items = new HashMap<>();
        try {
            fill();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fill() throws JSONException {


        long unixDate        = js.getLong("dt")*1000;
        Log.i("unixDate", String.valueOf(unixDate));
        SimpleDateFormat s  = new SimpleDateFormat("MMMM d, yyyy");
        s.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        String titleDate    = s.format(unixDate);
        Log.i("date", titleDate);

        String humidity     = String.valueOf(js.getInt("humidity"));

        JSONObject weather  = (JSONObject) js.getJSONArray("weather").get(0);
        String main         = weather.getString("main");
        String description  = weather.getString("description");

        JSONObject temp     = js.getJSONObject("temp");
        String min          = String.valueOf(temp.getInt("min"));
        String max          = String.valueOf(temp.getInt("max"));

        String cityName     = cityInfo.getString("name");

        items.put("title", titleDate);
        items.put("humidity", humidity);
        items.put("main", main);
        items.put("description", description);
        items.put("min", min);
        items.put("max", max);
        items.put("city", cityName);

    }
    public String getVal(String key){
        return items.get(key);
    }

}
