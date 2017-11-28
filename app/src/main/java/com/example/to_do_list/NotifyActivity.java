package com.example.to_do_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.litepal.crud.DataSupport;

public class NotifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        Intent intent = getIntent();
        final int position = intent.getIntExtra("tagPosition",0);
        final Tag tag = MainActivity.tagList.get(position);
        tag.setNotified(false);

        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(NotifyActivity.this);
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("到点啦").setMessage(tag.getContent());
        normalDialog.setPositiveButton("查看",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent detailsIntent = new Intent(NotifyActivity.this,ThirdActivity.class);
                        detailsIntent.putExtra("tagPosition",position);
                        startActivity(detailsIntent);
                        finish();;
                    }
                });
        normalDialog.setNeutralButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.tagList.remove(position);
                tag.delete();
                finish();
            }
        });
        normalDialog.setNegativeButton("忽略",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        normalDialog.show();
    }
}
