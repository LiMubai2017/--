package com.example.to_do_list.presenter;

import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.Toast;

import com.example.to_do_list.R;
import com.example.to_do_list.model.Tag;
import com.example.to_do_list.model.Utility;
import com.example.to_do_list.model.Weather;
import com.example.to_do_list.view.EditView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by abc on 2017/12/14.
 */

public class EditPresenter {
    EditView view;
    private Tag tag;
    private int position;
    private int priority,year_begin,year_end,month_begin,month_end,day_begin,day_end,hour_begin,hour_end,minute_begin,minute_end;
    String weatherAddress;

    public EditPresenter(EditView view) {
        this.view = view;
    }

    public void init(int position) {
        //获得tag实例
        this.position = position;
        tag = MainPresenter.tagList.get(position);

        //初始化优先级、开始、结束时间
        priority=tag.getPriority();
        year_begin=tag.getYear_begin();month_begin=tag.getMonth_begin();day_begin=tag.getDay_begin();
        hour_begin=tag.getHour_begin();minute_begin=tag.getMinute_begin();
        year_end=tag.getYear_end();month_end=tag.getMonth_end();day_end=tag.getDay_end();
        hour_end=tag.getHour_end();minute_end=tag.getMinute_end();
    }

    public void initView() {
        view.initContent(tag);
        view.initAlarm(position,tag.isNotified());
    }

    private void getWeather(){
        String weatherUrl =view.getString(R.string.weather_url);
        Utility.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.runOnUiThread(()->{
                    view.makeToast("获取天气失败");
                    view.hideProgressDialog();
                });
                tag.setShowTemperature(false);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Weather weather = (Utility.handleWeatherResponse(responseString));
                tag.setTemperature(weather.now.temperature);
                tag.setUpdateTime(System.currentTimeMillis());
                view.runOnUiThread(()->{
                    view.changeFragment(tag.getTemperature());
                    view.hideProgressDialog();
                });
            }
        });
    }


    public void changeTimeBegin(int i, int i1) {
        hour_begin=i;
        minute_begin=i1;
        refreshAttachment();
    }

    public void changeTimeEnd(int i, int i1) {
        hour_end=i;
        minute_end=i1;
        refreshAttachment();
    }

    public void changeDataBegin(int i, int i1,int i2) {
        year_begin=i;
        month_begin=i1;
        day_begin=i2;
        refreshAttachment();
    }

    public void changeDataEnd(int i, int i1,int i2) {
        year_end=i;
        month_end=i1;
        day_end=i2;
        refreshAttachment();
    }

    public void refreshAttachment() {
        tag.set(year_begin,year_end,month_begin,month_end,day_begin,day_end,hour_begin,hour_end,minute_begin,minute_end);
        tag.setPriority(priority);
        view.updateAttachment(tag);
    }

    public void save() {
        tag.save();
        MainPresenter.tagList.set(position,tag);
    }

    public void delete() {
        MainPresenter.tagList.remove(position);
        tag.delete();
    }

    public void changeWeatherFragmentStatus() {
        if(tag.isShowTemperature()==false) {
            tag.setShowTemperature(true);
            if(System.currentTimeMillis()-tag.getUpdateTime()>300000) {
                getWeather();
                view.showProgressDialog();
            }   else {
                view.changeFragment(tag.getTemperature());
            }
        }
        else {
            tag.setShowTemperature(false);
            view.changeFragment(null);
        }
    }

    public Tag getTag() {
        return tag;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setNotified(Boolean a) {
        tag.setNotified(a);
    }

    public int getPosition() {
        return position;
    }

    public String getContent() {
        return tag.getContent();
    }

    public void setContent(String content) {
        tag.setContent(content);
    }

    public int getPriority() {
        return priority;
    }

    public int getYear_begin() {
        return year_begin;
    }

    public int getYear_end() {
        return year_end;
    }

    public int getMonth_begin() {
        return month_begin;
    }

    public int getMonth_end() {
        return month_end;
    }

    public int getDay_begin() {
        return day_begin;
    }

    public int getDay_end() {
        return day_end;
    }

    public int getHour_begin() {
        return hour_begin;
    }

    public int getHour_end() {
        return hour_end;
    }

    public int getMinute_begin() {
        return minute_begin;
    }

    public int getMinute_end() {
        return minute_end;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
