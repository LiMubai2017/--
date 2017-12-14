package com.example.to_do_list.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abc on 2017/12/2.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
