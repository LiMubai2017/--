package com.example.to_do_list.model;

import com.example.to_do_list.model.Tag;

import java.util.Comparator;

/**
 * Created by abc on 2017/11/22.
 * 结束时间比较
 */

public class DeadlineCompare implements Comparator<Tag> {
    public int compare(Tag tag1, Tag tag2) {
        if (tag1.getYear_end() < tag1.getYear_end())
            return -1;
        else if (tag1.getYear_end() > tag1.getYear_end())
            return 1;
        else if (tag1.getMonth_end() < tag1.getMonth_end())
            return -1;
        else if (tag1.getMonth_end() > tag1.getMonth_end())
            return 1;
        else if (tag1.getDay_end() < tag1.getDay_end())
            return -1;
        else if (tag1.getDay_end() > tag1.getDay_end())
            return 1;
        else if (tag1.getHour_end() < tag1.getHour_end())
            return -1;
        else if (tag1.getHour_end() > tag1.getHour_end())
            return 1;
        else if (tag1.getMinute_end() < tag1.getMinute_end())
            return -1;
        else if (tag1.getMinute_end() < tag1.getMinute_end())
            return 1;
        return 0;
    }
}
