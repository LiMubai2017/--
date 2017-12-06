package com.example.to_do_list;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.to_do_list.db.Tag;
import com.example.to_do_list.db.Weather;
import com.example.to_do_list.util.Utility;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    public static List<Tag> tagList = new ArrayList<Tag>();
    private TagAdapter adapter;
    public static int rankMode = 3;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toobal = findViewById(R.id.toolbar);
        setSupportActionBar(toobal);
        Connector.getDatabase();
        initTags();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager manager = new
                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new TagAdapter(tagList);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final int dragFlags;
                final int swipeFlags;

                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    swipeFlags = 0;
                } else {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    swipeFlags = 0;
                }
                return makeMovementFlags(dragFlags, swipeFlags);
            }
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(tagList, i, i + 1);
                        int temp = tagList.get(i).getPriority();
                        tagList.get(i).setPriority(tagList.get(i+1).getPriority());
                        tagList.get(i+1).setPriority(temp);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(tagList, i, i - 1);
                        int temp = tagList.get(i).getPriority();
                        tagList.get(i).setPriority(tagList.get(i-1).getPriority());
                        tagList.get(i-1).setPriority(temp);
                    }
                    rankMode=3;
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener((v)->
        {
            Tag tag = new Tag("");
            tagList.add(tag);
            tag.save();
            Intent intent = new Intent(MainActivity.this,ThirdActivity.class);
            intent.putExtra("tagPosition",tagList.size()-1);
            startActivity(intent);
        });
    }

    private void sort() {
        if(rankMode == 1) Collections.sort(tagList,new BeginningCompare());
        if(rankMode == 2) Collections.sort(tagList,new DeadlineCompare());
        if(rankMode == 3) Collections.sort(tagList,new PriorityCompare());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rank_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.beginning_rank :
                rankMode = 1;
                sort();
                break ;
            case R.id.deadline_rank :
                rankMode = 2;
                sort();
                break ;
            case R.id.priority_rank :
                rankMode = 3;
                sort();
                break ;
        }
        return true;
    }

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
        }
    }

}
