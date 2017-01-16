package com.netforceinfotech.kunsang.ctm360watch;

import android.app.Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        userSessionManager = new UserSessionManager(context);

        initView();

        if (!userSessionManager.getToken().equalsIgnoreCase("")) {
            getData(userSessionManager.getToken());
            frameLayout.setVisibility(View.GONE);
        }

        setFormatedTime();
        setTime();
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
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

    private void getData(final String token) {
        progressBar.setVisibility(View.VISIBLE);
        String url = getString(R.string.url);
        setupSelfSSLCert();
        // disable SSLv3 unless that's the only protocol the engine can handle
        Ion.getDefault(this).getHttpClient().getSSLSocketMiddleware().addEngineConfigurator(new AsyncSSLEngineConfigurator() {
            @Override
            public void configureEngine(SSLEngine engine, AsyncHttpClientMiddleware.GetSocketData data, String host, int port) {
                String[] protocols = engine.getEnabledProtocols();
                if (protocols != null && protocols.length > 1) {
                    List<String> enabledProtocols = new ArrayList<String>(Arrays.asList(protocols));
                    if (enabledProtocols.remove("SSLv3")) {
                        protocols = enabledProtocols.toArray(new String[enabledProtocols.size()]);
                        engine.setEnabledProtocols(protocols);
                    }
                }
            }


        });
        Ion ion = Ion.getDefault(context);
        try {
            ion.configure().createSSLContext("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        final Trust trust = new Trust();
        final TrustManager[] trustmanagers = new TrustManager[]{trust};
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustmanagers, new SecureRandom());
            Ion.getInstance(context, "rest").getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustmanagers);
            Ion.getInstance(context, "rest").getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            ion.getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
            ion.getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustmanagers);
            Ion.getDefault(context).getConscryptMiddleware().enable(true);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (final KeyManagementException e) {
            e.printStackTrace();
        }

        Ion.with(context)
                .load("POST", url)
                .setBodyParameter("watch_token", token)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBar.setVisibility(View.GONE);
                        // do stuff with the result or error
                        if (result == null) {
                            frameLayout.setVisibility(View.VISIBLE);
                            showMessage("something wrong");
                        } else {
                            frameLayout.setVisibility(View.GONE);
                            Log.i("result", result.toString());
                            if (result.get("flag").getAsString().equalsIgnoreCase("success")) {
                                userSessionManager.setToken(token);
                                userSessionManager.setJsonData(result.toString());
                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                showMessage(result.get("msg").getAsString());
                                frameLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

    }

    public void setupSelfSSLCert() {
        final Trust trust = new Trust();
        final TrustManager[] trustmanagers = new TrustManager[]{trust};
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustmanagers, new SecureRandom());
            Ion.getInstance(context, "rest").getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustmanagers);
            Ion.getInstance(context, "rest").getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (final KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private static class Trust implements X509TrustManager {

        /**
         * {@inheritDoc}
         */
        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType)
                throws CertificateException {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType)
                throws CertificateException {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.v("myTag", "Main activity received message: " + message);
            // Display message in UI
            userSessionManager.setToken(message);
            getData(message);
        }
    }
}
