package com.example.to_do_list.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abc on 2017/12/2.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
