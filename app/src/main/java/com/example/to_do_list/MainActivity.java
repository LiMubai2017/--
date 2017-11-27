package com.example.to_do_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static List<Tag> tagList = new ArrayList<Tag>();
    private TagAdapter adapter;
    public static int rankMode = 1;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Connector.getDatabase();
        initTags();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        //LinearLayoutManager manager = new LinearLayoutManager(this);
        StaggeredGridLayoutManager manager = new
                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new TagAdapter(tagList);
        recyclerView.setAdapter(adapter);
        Button buttonAdd = findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tag tag = new Tag("");
                tagList.add(tag);
                tag.save();
                Intent intent = new Intent(MainActivity.this,ThirdActivity.class);
                intent.putExtra("tagPosition",tagList.size()-1);
                startActivity(intent);
            }
        });
        findViewById(R.id.beginning_rank_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rankMode = 1;
                sort();
            }
        });
        findViewById(R.id.deadline_rank_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rankMode = 2;
                sort();
            }
        });
    }

    private void sort() {
        if(rankMode == 1) Collections.sort(tagList,new BeginningCompare());
        if(rankMode == 2) Collections.sort(tagList,new DeadlineCompare());
        adapter.notifyDataSetChanged();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rank_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.beginning_rank :
                break ;
            case R.id.deadline_rank :
                break ;
            case R.id.priority_rank :
                break ;
        }
        return true;
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        sort();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(Tag temp:tagList)
            temp.save();
    }

    private void initTags() {
        tagList = DataSupport.findAll(Tag.class);
        if(tagList.size()==0) {
            Tag tag = new Tag("欢迎使用木白便签");
            tagList.add(tag);
            tag.save();
        }
    }
}
