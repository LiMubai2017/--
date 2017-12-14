package com.example.to_do_list.model;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

/**
 * Created by abc on 2017/11/16.
 */

public class Tag extends DataSupport{
    private String content;
    private   int year_begin,year_end,month_begin,month_end,day_begin,day_end,hour_begin,hour_end,minute_begin,minute_end;
    private boolean isNotified;
    private int priority;
    private String temperature;
    private long updateTime;
    private boolean showTemperature=false;

    public void set(int year_begin,int year_end,int month_begin,int month_end,int day_begin,int day_end,int hour_begin,int hour_end,int minute_begin,int minute_end) {
        this.year_begin = year_begin;this.year_end = year_end;this.month_begin = month_begin;this.month_end = month_end;this.minute_end = minute_end;
        this.day_begin = day_begin;this.day_end = day_end;this.hour_begin = hour_begin;this.hour_end = hour_end;this.minute_begin = minute_begin;
    }

    public Tag(String content) {
        this.content = content;
        Calendar calendar = Calendar.getInstance();
        year_end=year_begin = calendar.get(Calendar.YEAR);
        priority=2;
        month_begin=month_end = calendar.get(Calendar.MONTH);
        day_end=day_begin=calendar.get(Calendar.DAY_OF_MONTH);
        isNotified=false;
    }

    public boolean isShowTemperature() {
        return showTemperature;
    }

    public void setShowTemperature(boolean showTemperature) {
        this.showTemperature = showTemperature;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
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

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }


    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
