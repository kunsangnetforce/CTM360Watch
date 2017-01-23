package com.netforceinfotech.kunsang.ctm360watch;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.DismissOverlayView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpClientMiddleware;
import com.koushikdutta.async.http.AsyncSSLEngineConfigurator;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.kunsang.ctm360watch.general.UserSessionManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends WearableActivity implements View.OnClickListener {

    private Timer timer;
    TextView textViewDay, textViewDate, textViewTime;
    Context context;
    UserSessionManager userSessionManager;
    FrameLayout frameLayout;
    private ProgressBar progressBar;
    BoxInsetLayout boxLayout;
    private DismissOverlayView mDismissOverlay;
    private GestureDetector mDetector;
    private IntentFilter messageFilter;
    private MessageReceiver messageReceiver;
    boolean firsttime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        userSessionManager = new UserSessionManager(context);
        initView();
        setFormatedTime();
        setTime();
        messageFilter = new IntentFilter(Intent.ACTION_SEND);
        messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    private void initView() {
        boxLayout = (BoxInsetLayout) findViewById(R.id.boxLayout);
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.long_press_intro);
        mDismissOverlay.showIntroIfNecessary();

        // Configure a gesture detector
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent ev) {
                mDismissOverlay.show();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        textViewDay = (TextView) findViewById(R.id.textViewDay);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
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
    protected void onPause() {
        try {
            timer.cancel();
        } catch (Exception ex) {

        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        try {
            timer.cancel();
        } catch (Exception ex) {

        }

        super.onDestroy();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            userSessionManager.setJsonData(message);
            showMessage("called");
            Log.i("result", userSessionManager.getJsonData());
            if (userSessionManager.getJsonData().equalsIgnoreCase("")) {
                showMessage("Get Data from mobile");
            } else {
                if (firsttime) {
                 //   startActivity(new Intent(context, DashboardActivity.class));
                    firsttime = false;
                }
            }
        }


    }
}
