package com.example.to_do_list;

import com.example.to_do_list.db.Tag;

import java.util.Comparator;

/**
 * Created by abc on 2017/11/22.
 */

public class BeginningCompare implements Comparator {
    @Override
    public int compare(Object o, Object t1) {
        Tag tag1 = (Tag) o;
        Tag tag2 = (Tag) t1;
        if (tag1.getYear_begin() < tag2.getYear_begin())
            return -1;
        else if (tag1.getYear_begin() > tag2.getYear_begin())
            return 1;
        else if (tag1.getMonth_begin() < tag2.getMonth_begin())
            return -1;
        else if (tag1.getMonth_begin() > tag2.getMonth_begin())
            return 1;
        else if (tag1.getDay_begin() < tag2.getDay_begin())
            return -1;
        else if (tag1.getDay_begin() > tag2.getDay_begin())
            return 1;
        else if (tag1.getHour_begin() < tag2.getHour_begin())
            return -1;
        else if (tag1.getHour_begin() > tag2.getHour_begin())
            return 1;
        else if (tag1.getMinute_begin() < tag2.getMinute_begin())
            return -1;
        else if (tag1.getMinute_begin() < tag2.getMinute_begin())
            return 1;

        return 0;
    }
}
