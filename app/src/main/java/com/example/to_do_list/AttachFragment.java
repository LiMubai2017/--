package com.example.to_do_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.to_do_list.db.Weather;

/**
 * Created by abc on 2017/12/3.
 */

public class AttachFragment extends Fragment {
    View view;
    public void changeWeather(String temperature) {
        TextView weatherView = view.findViewById(R.id.view_weather);
        weatherView.setText(temperature+"℃");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.weather_fragment,container,false);
        return view;
    }
}
