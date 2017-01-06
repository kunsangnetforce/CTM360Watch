package com.netforceinfotech.kunsang.ctm360watch.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSessionManager {

    private static final String R_TOKEN = "rtoken";
    // Sharedpref file name
    private static final String PREFER_NAME = "CTM360WATCH";

    // All Shared Preferences Keys
    private static final String TOKEN = "token";
    private static final String JSON = "JSON";
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public String getToken() {
        return pref.getString(TOKEN, "");
    }

    public void setToken(String regid) {
        editor.putString(TOKEN, regid);
        editor.commit();
    }
    public String getJsonData() {
        return pref.getString(JSON, "");
    }

    public void setJsonData(String regid) {
        editor.putString(JSON, regid);
        editor.commit();
    }
    public void clearData() {
        editor.clear().commit();
    }


}