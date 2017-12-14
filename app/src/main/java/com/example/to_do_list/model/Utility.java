package com.example.to_do_list.model;

import android.util.Log;

import com.example.to_do_list.model.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by abc on 2017/12/2.
 */

public class Utility {

    //发送网络请求
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    //Gson网络请求返回数据
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherCotent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherCotent,Weather.class);
        } catch(Exception e) {
            Log.d("handle fail","fail");
            e.printStackTrace();
        }
        return null;
    }
}
