package com.example.to_do_list.presenter;

import com.example.to_do_list.model.BeginningCompare;
import com.example.to_do_list.model.DeadlineCompare;
import com.example.to_do_list.model.PriorityCompare;
import com.example.to_do_list.model.Tag;
import com.example.to_do_list.view.MainView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by abc on 2017/12/14.
 */

public class MainPresenter {
    public static final int BEGINNING = 1;
    public static final int DEADLINE = 2;
    public static final int PRIORITY = 3;
    private MainView view;
    public static List<Tag> tagList;
    public static int rankMode = PRIORITY;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    //初始化
    public void init() {
        initTagList();
    }

    //初始化Tag列表
    public void initTagList() {
        //从数据库中获取，若没有则初始化一个Tag
        Connector.getDatabase();
        tagList = DataSupport.findAll(Tag.class);
        if(tagList == null)tagList = new ArrayList<Tag>();
        if(tagList.size()==0) {
            Tag tag = new Tag("欢迎使用木白便签");
            tagList.add(tag);
        }
    }

    //创建新Tag实例
    public void newTag() {
        Tag tag = new Tag("");
        tagList.add(tag);
    }

    //储存TagList到数据库
    public void saveTagsToDataBase() {
        for(Tag temp:tagList)
            temp.save();
    }

    //Tag排序并刷新View
    public void sort(int rankMode) {
        this.rankMode = rankMode;
        if(rankMode == BEGINNING) Collections.sort(tagList,new BeginningCompare());
        if(rankMode == DEADLINE) Collections.sort(tagList,new DeadlineCompare());
        if(rankMode == PRIORITY) Collections.sort(tagList,new PriorityCompare());
        view.refresh();
    }

}
