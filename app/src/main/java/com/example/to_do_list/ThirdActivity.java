package com.example.to_do_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener{

    private String content;
    int position;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        editText = findViewById(R.id.edit_text);
        Button button_delete = findViewById(R.id.button_delete);
        Button button_edit = findViewById(R.id.button_edit);
        Intent intent = getIntent();
        content = intent.getStringExtra("tagTitle");
        position = intent.getIntExtra("tagPosition",0);
        editText.setText(content);
        button_delete.setOnClickListener(this);
        button_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int mode=0;
        String newConten = editText.getText().toString();
        switch(view.getId()) {
            case R.id.button_edit :
                if("".equals(newConten)==true) mode=-1;
                else
                    if(content.equals(newConten)==false) mode=1;
                break;
            case R.id.button_delete :
                mode=-1;
        }

        List<Tag> tagList = DataSupport.findAll(Tag.class);
        if(mode==-1) tagList.get(position).delete();
        else
            if(mode==1) {
                tagList.get(position).setContent(newConten);
                tagList.get(position).save();
            }
        finish();
    }
}
