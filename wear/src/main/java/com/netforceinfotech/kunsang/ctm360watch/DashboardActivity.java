package com.netforceinfotech.kunsang.ctm360watch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netforceinfotech.kunsang.ctm360watch.general.GeneralActivity;
import com.netforceinfotech.kunsang.ctm360watch.general.UserSessionManager;

import java.text.SimpleDateFormat;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private Bundle bundle;
    Context context;
    TextView textViewName;
    UserSessionManager userSessionManager;
    String member_name = "";
    TextView textViewDay, textViewDate, textViewTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        userSessionManager = new UserSessionManager(context);
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(userSessionManager.getJsonData()).getAsJsonObject();
        member_name = o.get("member_name").getAsString();
        initView();
        setFormatedTime();
        setTime();

    }

    private void initView() {
        textViewDay = (TextView) findViewById(R.id.textViewDay);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText(member_name);
        findViewById(R.id.imageViewSetting).setOnClickListener(this);
        findViewById(R.id.linearLayoutStatus).setOnClickListener(this);
        findViewById(R.id.linearLayoutSeverity).setOnClickListener(this);
        findViewById(R.id.linearLayoutSummary).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewSetting:
                userSessionManager.clearData();
                intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.linearLayoutStatus:
                intent = new Intent(context, GeneralActivity.class);
                bundle = new Bundle();
                bundle.putString("title", "status");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.linearLayoutSeverity:
                intent = new Intent(context, GeneralActivity.class);
                bundle = new Bundle();
                bundle.putString("title", "severity");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.linearLayoutSummary:
                intent = new Intent(context, GeneralActivity.class);
                bundle = new Bundle();
                bundle.putString("title", "summary");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
    private void setTime() {
        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                setFormatedTime();
                ha.postDelayed(this, 30000);
            }
        }, 10000);
    }

    private void setFormatedTime() {
        long date = System.currentTimeMillis();
        textViewDay.setText(getFormattedDay(date));
        textViewDate.setText(getFormattedDate(date));
        textViewTime.setText(getFormattedTime(date));

    }

    private String getFormattedTime(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date);
        return time;

    }

    private String getFormattedDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String day = sdf.format(date);
        return day;
    }

    private String getFormattedDay(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        String day = sdf.format(date);
        return day;
    }

}
