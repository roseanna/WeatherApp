package com.example.roseanna.weatherapp_json;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by roseanna on 4/10/16.
 */
public class WeatherAdapter extends ArrayAdapter{
    private ArrayList<WeatherDay> days;
    private Context context;
    public WeatherAdapter(Context context, ArrayList<WeatherDay> days) {
        super(context, R.layout.weather, days);
        this.context = context;
        this.days = days;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView            = inflater.inflate(R.layout.weather, parent, false);
        TextView title          = (TextView) rowView.findViewById(R.id.DayTitle);
        TextView main           = (TextView) rowView.findViewById(R.id.main);
        TextView description    = (TextView) rowView.findViewById(R.id.description);
        TextView min            = (TextView) rowView.findViewById(R.id.TempMin);
        TextView max            = (TextView) rowView.findViewById(R.id.TempMax);
        ImageView imageView     = (ImageView) rowView.findViewById(R.id.pic);

        title.setText(days.get(position).getVal("title"));
        main.setText(days.get(position).getVal("main"));
        description.setText(days.get(position).getVal("description"));
        min.setText("Min temp: " + days.get(position).getVal("min"));
        max.setText("Max temp: " + days.get(position).getVal("max"));
        switch (days.get(position).getVal("main")){
            case "Rain":
                imageView.setImageResource(R.drawable.rain);
                break;
            case "Clouds":
                imageView.setImageResource(R.drawable.cloud);
                break;
            default:
                imageView.setImageResource(R.drawable.sun);
        }
        return rowView;
    }
}
