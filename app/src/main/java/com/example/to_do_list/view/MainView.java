package com.example.to_do_list.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.example.to_do_list.R;
import com.example.to_do_list.presenter.MainPresenter;
import com.example.to_do_list.presenter.TouchCallback;

public class MainView extends AppCompatActivity{
    public static TagAdapter adapter;
    RecyclerView recyclerView;
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this);
        presenter.init();
        initRes();
    }

    //初始化界面
    public void initRes() {
        setContentView(R.layout.activity_main);
        //设置toolbar
        android.support.v7.widget.Toolbar toobar = findViewById(R.id.toolbar);
        setSupportActionBar(toobar);
        //设置悬浮按钮
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener((v)->
        {
            presenter.newTag();
            Intent intent = new Intent(MainView.this,EditView.class);
            intent.putExtra("tagPosition",MainPresenter.tagList.size()-1);
            startActivity(intent);
        });
        //初始化recyclerView
        initRecyclerView();
        //初始化排序
        presenter.sort(presenter.getRankMode());
    }

    public void initRecyclerView() {
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        //适配器设置
        adapter = new TagAdapter(MainPresenter.tagList);
        recyclerView.setAdapter(adapter);
        //布局管理
        StaggeredGridLayoutManager manager = new
                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        //设置拖拽事件
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    //adapter更新
    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    //导入菜单布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rank_options,menu);
        return true;
    }

    //菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.beginning_rank :
                presenter.sort(MainPresenter.BEGINNING);
                break ;
            case R.id.deadline_rank :
                presenter.sort(MainPresenter.DEADLINE);
                break ;
            case R.id.priority_rank :
                presenter.sort(MainPresenter.PRIORITY);
                break ;
        }
        return true;
    }

    //应用每次恢复可见状态更新adapter
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    //view销毁时储存数据
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.saveTagsToDataBase();
    }
}
