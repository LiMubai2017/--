package com.example.to_do_list;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.to_do_list.db.Tag;
import com.example.to_do_list.db.Weather;
import com.example.to_do_list.util.Utility;
import com.suke.widget.SwitchButton;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    String weatherAddress;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allContext = getApplicationContext();
        setContentView(R.layout.activity_third);
        editText = findViewById(R.id.edit_text);
        ImageButton button_delete = findViewById(R.id.button_delete);
        notifyButton = findViewById(R.id.notify_button);
        beginningDate = (TextView) findViewById(R.id.beginningDate_view);
        beginningTime=(TextView) findViewById(R.id.beginningTime_view);
        endDate=(TextView) findViewById(R.id.endDate_view);
        endTime=(TextView) findViewById(R.id.endTime_view);
        priority_view=(TextView) findViewById(R.id.priority_view);
        android.support.v7.widget.Toolbar toobal2 = findViewById(R.id.toolbar2);
        progressDialog = new ProgressDialog(ThirdActivity.this);
        progressDialog.setTitle("获取当地天气");
        progressDialog.setMessage("全力加载中");
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
        if(tag.isShowTemperature()) changeFragment(tag.getTemperature());
        else changeFragment(null);
        beginningTime.setOnClickListener(this);
        beginningDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        endTime.setOnClickListener(this);
        priority_view.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        findViewById(R.id.button_weather).setOnClickListener(this);
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
    }

    private void changeFragment(String temperature) {
        LinearLayout fragmentLayout = findViewById(R.id.fragment_layout);
        if(temperature == null) {
            if(fragmentLayout.getVisibility()==View.VISIBLE)
            fragmentLayout.setVisibility(View.GONE);
            Log.d("test","隐藏碎片");
            return ;
        }
        if(fragmentLayout.getVisibility()==View.GONE) {
            fragmentLayout.setVisibility(View.VISIBLE);
            Log.d("test","显示碎片");
        }
        AttachFragment weatherFragment = (AttachFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        weatherFragment.changeWeather(temperature);
        Log.d("test","更新温度");
    }

    private void getWeather(){
        String weatherUrl = "http://guolin.tech/api/weather?cityid=CN101200102&key=59c732c13e9141a79deec24c07a10a7e";
        Utility.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Test","失败");
                runOnUiThread(()->{
                    Toast.makeText(ThirdActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
                tag.setShowTemperature(false);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Weather weather = (Utility.handleWeatherResponse(responseString));
                tag.setTemperature(weather.now.temperature);
                tag.setUpdateTime(System.currentTimeMillis());
                runOnUiThread(()->{
                    changeFragment(tag.getTemperature());
                    progressDialog.dismiss();
                });
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break ;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_weather:
                if(tag.isShowTemperature()==false) {
                    tag.setShowTemperature(true);
                    if(System.currentTimeMillis()-tag.getUpdateTime()>300000) {
                        getWeather();
                        progressDialog.show();
                    }   else {
                        changeFragment(tag.getTemperature());
                    }
                }
                else {
                    tag.setShowTemperature(false);
                    changeFragment(null);
                }
                break;
            case R.id.button_confirmdelete:
                mode=-1;
                finish();
                break;
            case R.id.button_delete :
                showDeleteDialog();
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
                new DatePickerDialog(ThirdActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year_end=i;
                        month_end=i1;
                        day_end=i2;
                    }
                },year_end,month_end,day_end).show();
                break;
            case R.id.endTime_view:
                new TimePickerDialog(ThirdActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour_end=i;
                        minute_end=i1;
                        updateAttachment();
                    }
                },hour_end,minute_end,true).show();
                break;
            case R.id.priority_view:
                AlertDialog.Builder priorityChoodeDialog = new AlertDialog.Builder(ThirdActivity.this);
                priorityChoodeDialog.setTitle("标签优先级");
                priorityChoodeDialog.setSingleChoiceItems(priorityChoics,priority,(dialog,which)->{
                    if(which!=-1) priority=which;
                });
                priorityChoodeDialog.setPositiveButton("确定",(dialog,which)->{
                        tag.setPriority(priority);
                        updateAttachment();
                });
                priorityChoodeDialog.show();
                break;
        }
    }

    private void showDeleteDialog() {
        Dialog dialog = new Dialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete,null);
        dialogView.findViewById(R.id.button_confirmdelete).setOnClickListener(this);
        dialogView.findViewById(R.id.button_cancel).setOnClickListener((v)->{
            dialog.hide();
        });
        dialog.setContentView(dialogView);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y=0;
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();;
    }

    @Override
    protected void onPause() {
        super.onPause();
        String newConten = editText.getText().toString();
        tag.setContent(newConten);
        if("".equals(newConten)==true) mode=-1;
        else
        if(tag.getContent().equals(newConten)==false&&mode!=-1) mode=1;

        if(mode==-1){
            MainActivity.tagList.get(position).delete();
            MainActivity.tagList.remove(position);
        }
        else
        {
            if(mode==1) MainActivity.tagList.set(position,tag);
        }
    }

}
