package com.example.to_do_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.litepal.crud.DataSupport;

import java.util.List;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button buttonAdd = (Button) findViewById(R.id.button_submit);
        final EditText editText = (EditText) findViewById(R.id.text_input);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strInput = editText.getText().toString();
                editText.setText("");
                if("".equals(strInput)!=true) {
                    Tag tag = new Tag(strInput);
                    tag.save();
                }
                finish();
            }
        });

    }
}
