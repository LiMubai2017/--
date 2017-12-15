package com.example.to_do_list.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.to_do_list.R;
import com.example.to_do_list.model.Tag;
import com.example.to_do_list.presenter.MainPresenter;

public class NotifyView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        //初始化跳转Intent
        Intent intent = getIntent();
        final int position = intent.getIntExtra("tagPosition",0);

        //获得Tag实例
        final Tag tag = MainPresenter.tagList.get(position);
        tag.setNotified(false);

        //设置对话框
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(NotifyView.this);
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("到点啦").setMessage(tag.getContent());
        normalDialog.setPositiveButton("查看",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent detailsIntent = new Intent(NotifyView.this,EditView.class);
                        detailsIntent.putExtra("tagPosition",position);
                        startActivity(detailsIntent);
                        finish();;
                    }
                });
        normalDialog.setNeutralButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainPresenter.tagList.remove(position);
                tag.delete();
                finish();
            }
        });
        normalDialog.setNegativeButton("忽略",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainPresenter.tagList.get(position).setNotified(false);
                        finish();
                    }
                });
        normalDialog.show();
    }

    public void skipView() {

    }
}
