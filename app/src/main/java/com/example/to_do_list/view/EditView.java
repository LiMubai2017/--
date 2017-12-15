package com.example.to_do_list.view;

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

import com.example.to_do_list.R;
import com.example.to_do_list.model.Tag;
import com.example.to_do_list.presenter.EditPresenter;
import com.suke.widget.SwitchButton;

import java.util.Calendar;

import static android.app.AlarmManager.RTC_WAKEUP;

public class EditView extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;
    private TextView beginningTime,beginningDate,endDate,endTime,priority_view;
    com.suke.widget.SwitchButton notifyButton;

    AlarmManager alarmManager;
    private Context allContext;
    ProgressDialog progressDialog;
    EditPresenter presenter;

    String[] priorityChoics={"低","较低","一般","较高","高"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allContext = getApplicationContext();
        initRes();
        initPresenter();
    }

    public void initPresenter() {
        presenter = new EditPresenter(this);
        Intent intent = getIntent();
        int position = intent.getIntExtra("tagPosition",0);
        presenter.init(position);
        presenter.initView();
    }

    public void initRes() {
        setContentView(R.layout.activity_third);

        //View组件获得实例
        editText = findViewById(R.id.edit_text);
        ImageButton button_delete = findViewById(R.id.button_delete);
        notifyButton = findViewById(R.id.notify_button);
        beginningDate = (TextView) findViewById(R.id.beginningDate_view);
        beginningTime=(TextView) findViewById(R.id.beginningTime_view);
        endDate=(TextView) findViewById(R.id.endDate_view);
        endTime=(TextView) findViewById(R.id.endTime_view);
        priority_view=(TextView) findViewById(R.id.priority_view);
        android.support.v7.widget.Toolbar toobal2 = findViewById(R.id.toolbar2);

        //设置标题栏
        setSupportActionBar(toobal2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //注册监听事件
        beginningTime.setOnClickListener(this);
        beginningDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        endTime.setOnClickListener(this);
        priority_view.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        findViewById(R.id.button_weather).setOnClickListener(this);

    }

    public void initContent(Tag tag) {
        //获得tag实例、设置内容、天气

        editText.setText(tag.getContent());
        if(tag.isShowTemperature()) changeFragment(tag.getTemperature());
        else changeFragment(null);

        //初始化progressDialog
        progressDialog = new ProgressDialog(EditView.this);
        progressDialog.setTitle("获取当地天气");
        progressDialog.setMessage("全力加载中");

        //显示优先级、开始、结束时间
        updateAttachment(presenter.getTag());
    }

    public void initAlarm(int position,boolean isNotified) {
        //根据Tag提醒状态设置switchButton
        if(isNotified) notifyButton.setChecked(true);

        //设置闹钟提醒pendingIntent
        Intent notifyIntent ;
        PendingIntent pendingIntent ;
        notifyIntent = new Intent(allContext,NotifyView.class);
        notifyIntent.putExtra("tagPosition",presenter.getPosition());
        pendingIntent = PendingIntent.getActivity(allContext,presenter.getPosition(),notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) allContext.getSystemService(Context.ALARM_SERVICE);

        notifyButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                notifyIntent.putExtra("tagPosition",presenter.getPosition());
                if(isChecked) {
                    presenter.setNotified(true);
                    Calendar notifyCalendar = Calendar.getInstance();
                    notifyCalendar.set(presenter.getYear_begin(),presenter.getMonth_begin(),presenter.getDay_begin(),presenter.getHour_begin(),presenter.getMinute_end());
                    if(Build.VERSION.SDK_INT>=19) {
                        alarmManager.setExact(RTC_WAKEUP, notifyCalendar.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.set(RTC_WAKEUP, notifyCalendar.getTimeInMillis(), pendingIntent);
                    }
                    makeToast("将在"+presenter.getYear_begin()+"-"+(presenter.getMonth_begin()+1)+"-"+presenter.getDay_begin()+"   "+
                            presenter.getHour_begin()+":"+presenter.getMonth_begin()+"提醒您");
                } else {
                    presenter.setNotified(false);
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(EditView.this,"提醒取消",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //更新附加信息
    public void updateAttachment(Tag tag) {
        beginningDate.setText(
                "开始时间  "+tag.getYear_begin()+"-"+(tag.getMonth_begin()+1)+"-"+tag.getDay_begin()
        );
        beginningTime.setText(
                "   "+ tag.getHour_begin()+":"+tag.getMinute_begin());
        endDate.setText("结束时间  "+tag.getYear_end()+"-"+(tag.getMonth_end()+1)+"-"+tag.getDay_end());
        endTime.setText("   "+tag.getHour_end()+":"+tag.getMinute_end());
        priority_view.setText("优先级： "+priorityChoics[tag.getPriority()]);
    }

    //改变天气碎片状态,null为隐藏
    public void changeFragment(String temperature) {
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


    public void showProgressDialog() {
        if(!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.hide();
    }

    public void makeToast(String content) {
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
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
                presenter.changeWeatherFragmentStatus();
                break;
            case R.id.button_confirmdelete:
                presenter.delete();
                finish();
                break;
            case R.id.button_delete :
                showDeleteDialog();
                break;
            case R.id.beginningTime_view:
                new TimePickerDialog(EditView.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        presenter.changeTimeBegin(i,i1);
                    }
                },presenter.getHour_begin(),presenter.getMinute_begin(),true).show();
                break;
            case R.id.beginningDate_view:
                new DatePickerDialog(EditView.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        presenter.changeDataBegin(i,i1,i2);
                    }
                },presenter.getYear_begin(),presenter.getMonth_begin(),presenter.getDay_begin()).show();
                break;
            case R.id.endDate_view:
                new DatePickerDialog(EditView.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        presenter.changeDataEnd(i,i1,i2);
                    }
                },presenter.getYear_end(),presenter.getMonth_end(),presenter.getDay_end()).show();
                break;
            case R.id.endTime_view:
                new TimePickerDialog(EditView.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        presenter.changeTimeEnd(i,i1);
                    }
                },presenter.getHour_end(),presenter.getMinute_end(),true).show();
                break;
            case R.id.priority_view:
                AlertDialog.Builder priorityChoodeDialog = new AlertDialog.Builder(EditView.this);
                priorityChoodeDialog.setTitle("标签优先级");
                priorityChoodeDialog.setSingleChoiceItems(priorityChoics,presenter.getPriority(),(dialog,which)->{
                });
                priorityChoodeDialog.setPositiveButton("确定",(dialog,which)->{
                        if(which!=-1) presenter.setPriority(which);
                        presenter.refreshAttachment();
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
        int mode = 0;
        super.onPause();
        String newConten = editText.getText().toString();
        presenter.setContent(newConten);
        if("".equals(newConten)==true) mode=-1;
        else
        if((presenter.getContent()).equals(newConten)==false&&mode!=-1) mode=1;
        if(mode==-1){
            presenter.delete();
        }
        else
        {
            if(mode==1) presenter.save();
        }
    }

}
