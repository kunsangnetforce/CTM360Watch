package com.netforceinfotech.kunsang.ctm360watch.general;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netforceinfotech.kunsang.ctm360watch.R;
import com.netforceinfotech.kunsang.ctm360watch.fragment.BarFragment;
import com.netforceinfotech.kunsang.ctm360watch.fragment.CircleFragment;

import java.text.SimpleDateFormat;

public class GeneralActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Context context;
    TextView textViewCharts, textViewCounter, textViewTitle;
    RadioButton radioButtonCharts, radioButtonCounter;
    UserSessionManager userSessionManager;
    String title;
    String header = "";
    TextView textViewDay, textViewDate, textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity);
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");

        context = this;
        userSessionManager = new UserSessionManager(context);
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(userSessionManager.getJsonData()).getAsJsonObject();
        switch (title) {
            case "status":
                header = o.getAsJsonObject("status").get("heading").getAsString();
                break;
            case "severity":
                header = o.getAsJsonObject("severity").get("heading").getAsString();
                break;
            case "summary":
                header = o.getAsJsonObject("summary").get("heading").getAsString();
                break;

        }
        initView();
        textViewTitle.setText(header);
        setupChartFragment();
        setFormatedTime();
        setTime();
    }

    private void initView() {
        textViewDay = (TextView) findViewById(R.id.textViewDay);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        findViewById(R.id.imageViewBack).setOnClickListener(this);
        textViewCharts = (TextView) findViewById(R.id.textViewChart);
        textViewCounter = (TextView) findViewById(R.id.textViewCounter);
        radioButtonCharts = (RadioButton) findViewById(R.id.radioCharts);
        radioButtonCounter = (RadioButton) findViewById(R.id.radioCounter);
        textViewCounter.setOnClickListener(this);
        textViewCharts.setOnClickListener(this);
        radioButtonCharts.setOnCheckedChangeListener(this);
        radioButtonCounter.setOnCheckedChangeListener(this);
    }

    private void replaceFragment(Fragment newFragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, newFragment, tag);
        transaction.commit();
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                finish();
                break;
            case R.id.textViewCounter:
                radioButtonCounter.performClick();
                break;
            case R.id.textViewChart:
                radioButtonCharts.performClick();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.radioCharts:
                if (b) {
                    textViewCharts.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    textViewCharts.setTextColor(ContextCompat.getColor(context, R.color.white));
                    textViewCounter.setBackgroundColor(ContextCompat.getColor(context, R.color.tranparent));
                    textViewCounter.setTextColor(ContextCompat.getColor(context, R.color.grey));
                    setupChartFragment();
                } else {
                    textViewCounter.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    textViewCounter.setTextColor(ContextCompat.getColor(context, R.color.white));
                    textViewCharts.setBackgroundColor(ContextCompat.getColor(context, R.color.tranparent));
                    textViewCharts.setTextColor(ContextCompat.getColor(context, R.color.grey));
                    setupCouterFragment();
                }
                break;
            case R.id.radioCounter:
                if (b) {
                    textViewCounter.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    textViewCounter.setTextColor(ContextCompat.getColor(context, R.color.white));
                    textViewCharts.setBackgroundColor(ContextCompat.getColor(context, R.color.tranparent));
                    textViewCharts.setTextColor(ContextCompat.getColor(context, R.color.grey));
                    setupCouterFragment();

                } else {
                    textViewCharts.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    textViewCharts.setTextColor(ContextCompat.getColor(context, R.color.white));
                    textViewCounter.setBackgroundColor(ContextCompat.getColor(context, R.color.tranparent));
                    textViewCounter.setTextColor(ContextCompat.getColor(context, R.color.grey));
                    setupChartFragment();
                }
                break;
        }
    }

    public void setupChartFragment() {
        BarFragment dashboardFragment = new BarFragment();
        String tagName = dashboardFragment.getClass().getName();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dashboardFragment.setArguments(bundle);
        replaceFragment(dashboardFragment, tagName);
    }

    private void setupCouterFragment() {
        CircleFragment dashboardFragment = new CircleFragment();
        String tagName = dashboardFragment.getClass().getName();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dashboardFragment.setArguments(bundle);
        replaceFragment(dashboardFragment, tagName);
    }
}
