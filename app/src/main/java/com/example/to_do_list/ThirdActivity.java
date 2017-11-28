package com.example.to_do_list;

import android.app.AlarmManager;
import android.app.AlertDialog;
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
    TextView beginningTime,beginningDate,endDate,endTime,priority_view;
    int priority,year_begin,year_end,month_begin,month_end,day_begin,day_end,hour_begin,hour_end,minute_begin,minute_end;
    com.suke.widget.SwitchButton notifyButton;
    AlarmManager alarmManager;
    private Context allContext;
    Intent notifyIntent ;
    PendingIntent pendingIntent ;
    String[] priorityChoics={"低","较低","一般","较高","高"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allContext = getApplicationContext();
        setContentView(R.layout.activity_third);
        editText = findViewById(R.id.edit_text);
        Button button_delete = findViewById(R.id.button_delete);
        notifyButton = findViewById(R.id.notify_button);
        beginningDate = (TextView) findViewById(R.id.beginningDate_view);
        beginningTime=(TextView) findViewById(R.id.beginningTime_view);
        endDate=(TextView) findViewById(R.id.endDate_view);
        endTime=(TextView) findViewById(R.id.endTime_view);
        priority_view=(TextView) findViewById(R.id.priority_view);
        android.support.v7.widget.Toolbar toobal2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toobal2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        position = intent.getIntExtra("tagPosition",0);
        notifyIntent = new Intent(allContext,NotifyActivity.class);
        notifyIntent.putExtra("tagPosition",position);
        pendingIntent = PendingIntent.getActivity(allContext,position,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        tag = MainActivity.tagList.get(position);
        alarmManager = (AlarmManager) allContext.getSystemService(Context.ALARM_SERVICE);
        priority=tag.getPriority();
        year_begin=tag.getYear_begin();month_begin=tag.getMonth_begin();day_begin=tag.getDay_begin();
        hour_begin=tag.getHour_begin();minute_begin=tag.getMinute_begin();
        year_end=tag.getYear_end();month_end=tag.getMonth_end();day_end=tag.getDay_end();
        hour_end=tag.getHour_end();minute_end=tag.getMinute_end();
        editText.setText(tag.getContent());
        if(tag.isNotified()) notifyButton.setChecked(true);
        updateAttachment();
        beginningTime.setOnClickListener(this);
        beginningDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        endTime.setOnClickListener(this);
        priority_view.setOnClickListener(this);
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

    public void updateAttachment() {
        beginningDate.setText(
                "开始时间  "+year_begin+"-"+(month_begin+1)+"-"+day_begin
        );

        beginningTime.setText(
                "   "+ hour_begin+":"+minute_begin);
        endDate.setText("结束时间  "+year_end+"-"+(month_end+1)+"-"+day_end);
        endTime.setText("   "+hour_end+":"+minute_end);
        priority_view.setText("优先级： "+priorityChoics[priority]);
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
            case R.id.beginningTime_view:
                new TimePickerDialog(ThirdActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour_begin=i;
                        minute_begin=i1;
                        updateAttachment();
                    }
                },hour_begin,minute_begin,true).show();
                break;
            case R.id.beginningDate_view:
                new DatePickerDialog(ThirdActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year_begin=i;
                        month_begin=i1;
                        day_begin=i2;
                    }
                },year_begin,month_begin,day_begin).show();
                break;
            case R.id.endDate_view:
                new TimePickerDialog(ThirdActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour_end=i;
                        minute_end=i1;
                        updateAttachment();
                    }
                },hour_end,minute_end,true).show();
                break;
            case R.id.endTime_view:
                new DatePickerDialog(ThirdActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year_end=i;
                        month_end=i1;
                        day_end=i2;
                    }
                },year_end,month_end,day_end).show();
                break;
            case R.id.priority_view:
                AlertDialog.Builder priorityChoodeDialog = new AlertDialog.Builder(ThirdActivity.this);
                priorityChoodeDialog.setTitle("标签优先级");
                priorityChoodeDialog.setSingleChoiceItems(priorityChoics,priority,(dialog,which)->{
                    if(which!=-1) priority=which;
                });
                priorityChoodeDialog.setPositiveButton("确定",(dialog,which)->{
                        tag.setPriority(priority);
                        tag.save();
                        updateAttachment();
                });
                priorityChoodeDialog.show();
                break;
            case R.id.home:
                Log.d("ThirdActivity.this","test");
                finish();
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
