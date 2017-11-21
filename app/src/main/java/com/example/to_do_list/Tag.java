package com.example.to_do_list;

import org.litepal.crud.DataSupport;

/**
 * Created by abc on 2017/11/16.
 */

public class Tag extends DataSupport{
    private String content;

    public Tag(String content) {
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
