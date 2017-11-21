package com.example.to_do_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private List<Tag> tagList = new ArrayList<Tag>();
    private TagAdapter adapter;
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
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tagList = DataSupport.findAll(Tag.class);
        adapter.setmTagList(tagList);
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
