package com.app.mehdichouag.epitechintranet.data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by mehdichouag on 06/01/15.
 */
public class LogUser {

    /*  NAME OF THE SHARE PREFS FILE */
    private static final String USER_SHARED_LOG = "com.app.mehdichouag.epitechintranet.data.USER_LOG";

    /* KEY FOR THE SHARED PREFS */
    private static final String KEY_USER_TOKEN = "com.app.mehdichouag.epitechintranet.data.USER_TOKEN";
    private static final String KEY_USER_LOGIN = "com.app.mehdichouag.epitechintranet.data.USER_LOGIN";
    private static final String KEY_USER_NAME = "com.app.mehdichouag.epitechintranet.data.USER_NAME";
    private static final String KEY_USER_LOCATION = "com.app.mehdichouag.epitechintranet.data.USER_LOCATION";
    private static final String KEY_USER_YEAR = "com.app.mehdichouag.epitechintranet.data.USER_YEAR";

    private SharedPreferences mPreference;

    /* STATIC INSTANCE FOR A SINGLETON PATTERN */
    private static LogUser mInstance = null;

    public LogUser(Activity activity){
        mPreference = activity.getSharedPreferences(USER_SHARED_LOG, 0);
    }

    public static LogUser getInstance(Activity activity){
        if (mInstance == null) {
            synchronized (LogUser.class) {
                if (mInstance == null) {
                    mInstance = new LogUser(activity);
                }
            }
        }
        return mInstance;
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(KEY_USER_TOKEN, token);
        editor.apply();
    }

    public void setUserInfos(String userName, String userLogin, String userLocation, int year) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_LOGIN, userLogin);
        editor.putString(KEY_USER_LOCATION, userLocation);
        editor.putInt(KEY_USER_YEAR, year);
        editor.apply();
    }

    public boolean isLogged(){
        String token = mPreference.getString(KEY_USER_TOKEN, null);
        if (token == null) {
            return false;
        }
        return true;
    }

    public String getToken(){
        return mPreference.getString(KEY_USER_TOKEN, null);
    }

    public String getUserName() {
        return mPreference.getString(KEY_USER_NAME, null);
    }

    public String getUserLocation() {
        return mPreference.getString(KEY_USER_LOCATION, null);
    }

    public String getUserLogin() {
        return mPreference.getString(KEY_USER_LOGIN, null);
    }

    public int getUserYear() {
        return mPreference.getInt(KEY_USER_YEAR, 1);
    }
}
