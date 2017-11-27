package com.example.to_do_list;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.suke.widget.SwitchButton;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

import static android.app.AlarmManager.RTC_WAKEUP;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener{

    private Tag tag;
    int position;
    EditText editText;
    int mode=0;
    TextView beginningTime_view;
    TextView deadLine_view;
    int year_begin,year_end,month_begin,month_end,day_begin,day_end,hour_begin,hour_end,minute_begin,minute_end;
    com.suke.widget.SwitchButton notifyButton;
    AlarmManager alarmManager;
    private Context allContext;
    Intent notifyIntent ;
    PendingIntent pendingIntent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        allContext = getApplicationContext();
        setContentView(R.layout.activity_third);
        editText = findViewById(R.id.edit_text);
        Button button_delete = findViewById(R.id.button_delete);
        notifyButton = findViewById(R.id.notify_button);
        beginningTime_view = (TextView) findViewById(R.id.beginningname_view);
        deadLine_view = (TextView) findViewById(R.id.deadline_view);
        Intent intent = getIntent();
        position = intent.getIntExtra("tagPosition",0);
        notifyIntent = new Intent(allContext,NotifyActivity.class);
        notifyIntent.putExtra("tagPosition",position);
        pendingIntent = PendingIntent.getActivity(allContext,position,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        tag = MainActivity.tagList.get(position);
        alarmManager = (AlarmManager) allContext.getSystemService(Context.ALARM_SERVICE);
        year_begin=tag.getYear_begin();month_begin=tag.getMonth_begin();day_begin=tag.getDay_begin();
        hour_begin=tag.getHour_begin();minute_begin=tag.getMinute_begin();
        year_end=tag.getYear_end();month_end=tag.getMonth_end();day_end=tag.getDay_end();
        hour_end=tag.getHour_end();minute_end=tag.getMinute_end();
        editText.setText(tag.getContent());
        if(tag.isNotified()) notifyButton.setChecked(true);
        updateTime();
        beginningTime_view.setOnClickListener(this);
        deadLine_view.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        notifyButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                notifyIntent.putExtra("tagPosition", position);
                if(isChecked) {
                    MainActivity.tagList.get(position).setNotified(true);
                    Calendar notifyCalendar = Calendar.getInstance();
                    notifyCalendar.set(year_begin,month_begin,day_begin,hour_begin,minute_begin);
                    if(Build.VERSION.SDK_INT>=19) {
                        alarmManager.setExact(RTC_WAKEUP, notifyCalendar.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.set(RTC_WAKEUP, notifyCalendar.getTimeInMillis(), pendingIntent);
                    }
                    Toast.makeText(ThirdActivity.this,"将在"+year_begin+"-"+(month_begin+1)+"-"+day_begin+"   "+
                            hour_begin+":"+minute_begin+"提醒您",Toast.LENGTH_SHORT).show();
                } else {
                    MainActivity.tagList.get(position).setNotified(false);
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(ThirdActivity.this,"提醒取消",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateTime() {
        beginningTime_view.setText(
                "开始时间  "+year_begin+"-"+(month_begin+1)+"-"+day_begin+"   "+
                        hour_begin+":"+minute_begin);
        deadLine_view.setText(
                "截止时间  "+year_end+"-"+(month_end+1)+"-"+day_end +"   "+
                        hour_end+":"+minute_end);
        tag.set(year_begin,year_end,month_begin,month_end,day_begin,day_end,hour_begin,hour_end,minute_begin,minute_end);
        tag.save();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_delete :
                mode=-1;
                finish();
                break;
            case R.id.beginningname_view:
                new TimePickerDialog(ThirdActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour_begin=i;
                        minute_begin=i1;
                        updateTime();
                    }
                },hour_begin,minute_begin,true).show();
                new DatePickerDialog(ThirdActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year_begin=i;
                        month_begin=i1;
                        day_begin=i2;
                    }
                },year_begin,month_begin,day_begin).show();
                break;
            case R.id.deadline_view:
                new TimePickerDialog(ThirdActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour_end=i;
                        minute_end=i1;
                        updateTime();
                    }
                },hour_end,minute_end,true).show();
                new DatePickerDialog(ThirdActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year_end=i;
                        month_end=i1;
                        day_end=i2;
                    }
                },year_end,month_end,day_end).show();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String newConten = editText.getText().toString();
        if("".equals(newConten)==true) mode=-1;
        else
        if(tag.getContent().equals(newConten)==false) mode=1;

        if(mode==-1){
            MainActivity.tagList.get(position).delete();
            MainActivity.tagList.remove(position);
        }
        else
        {
            if(mode==1) MainActivity.tagList.get(position).setContent(newConten);
        }
    }
}
