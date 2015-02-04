package com.app.mehdichouag.epitechintranet.application;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.mehdichouag.epitechintranet.model.Marks;
import com.app.mehdichouag.epitechintranet.model.Susie;

import java.util.List;

/**
 * Created by mehdichouag on 17/01/15.
 */
public class MyApplication extends Application {

    public static final String KEY_USER_LOG = "com.app.mehdichouag.epitechintranet.application.KEY_USER_LOG";
    public static final String KEY_USER_HISTORY = "com.app.mehdichouag.epitechintranet.application.KEY_USER_HISTORY";

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;

    private RequestQueue mRequestQueue;

    private List<Susie> mSusieList;
    private List<Marks> mMarksList;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setUserLog(String log) {
        Cache cache = getRequestQueue().getCache();
        Cache.Entry entry = new Cache.Entry();

        entry.data = log.getBytes();
        cache.put(KEY_USER_LOG, entry);
    }

    public void setUserHistory(String history) {
        Cache cache = getRequestQueue().getCache();
        Cache.Entry entry = new Cache.Entry();

        entry.data = history.getBytes();
        cache.put(KEY_USER_HISTORY, entry);
    }

    public String getUserHistory() {
        final Cache.Entry ret = getRequestQueue().getCache().get(KEY_USER_HISTORY);
        return ret != null ? new String (ret.data) : null;
    }

    public String getUserLog() {
        final Cache.Entry ret = getRequestQueue().getCache().get(KEY_USER_LOG);
        return ret != null ? new String (ret.data) : null;
    }

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public List<Susie> getSusie() {
        return mSusieList;
    }

    public void setSusieList(List<Susie> susies) {
        mSusieList = susies;
    }

}
