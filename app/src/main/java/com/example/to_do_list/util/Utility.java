package com.example.to_do_list.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.to_do_list.db.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by abc on 2017/12/2.
 */

public class Utility {

    public static void sendOkHttpRequest(String address,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static Weather handleWeatherResponse(String response) {
        try {
            Log.d("handle status","begin");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherCotent = jsonArray.getJSONObject(0).toString();
            Log.d("JsonHandleResponse",weatherCotent);
            return new Gson().fromJson(weatherCotent,Weather.class);
        } catch(Exception e) {
            Log.d("handle fail","fail");
            e.printStackTrace();
        }
        return null;
    }
}
