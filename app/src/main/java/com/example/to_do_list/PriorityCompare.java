package com.example.to_do_list;

import com.example.to_do_list.db.Tag;

import java.util.Comparator;

/**
 * Created by abc on 2017/11/28.
 */

public class PriorityCompare implements Comparator {

    @Override
    public int compare(Object o, Object t1) {
        Tag tag1=(Tag) o;
        Tag tag2=(Tag) t1;
        if(tag1.getPriority()>tag2.getPriority()) return -1;
        if(tag1.getPriority()<tag2.getPriority()) return 1;
        return 0;
    }
}
