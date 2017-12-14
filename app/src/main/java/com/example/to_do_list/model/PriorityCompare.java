package com.example.to_do_list.model;

import com.example.to_do_list.model.Tag;

import java.util.Comparator;

/**
 * Created by abc on 2017/11/28.
 * 优先级比较
 */

public class PriorityCompare implements Comparator<Tag> {

    @Override
    public int compare(Tag tag1, Tag tag2) {
        if(tag1.getPriority()>tag2.getPriority()) return -1;
        if(tag1.getPriority()<tag2.getPriority()) return 1;
        return 0;
    }
}
