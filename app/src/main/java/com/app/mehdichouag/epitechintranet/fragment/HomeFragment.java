package com.app.mehdichouag.epitechintranet.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.data.LogUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getSimpleName();

    /* CONSTANT THE MAX LASTEST MESSAGE TO DISPLAY */

    private static final int MAX_LASTEST_MESSAGE = 3;

    /* KEY JSON FOR THE LOG TIME */
    private final String KEY_ACTIVE_LOG = "active_log";
    private final String KEY_MIN_LOG = "nslog_norm";

    /* KEY JSON FOR THE HISTORY */
    private final String KEY_LASTEST_MESSAGE = "title";
    private final String KEY_LASTEST_MESSAGE_NAME = "user";

    private String mUrlImageUser;
    private String mUserName;
    private JSONArray mUserHistory;
    private JSONObject mUserTimeLog;

    private Context mContext;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle b = new Bundle();

        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        MyApplication application = MyApplication.getInstance();

        mUrlImageUser = mContext.getString(R.string.api_user_picture, LogUser.getInstance(activity).getUserLogin());
        mUserName = LogUser.getInstance(activity).getUserName();

        try {
            mUserHistory = new JSONArray(application.getUserHistory());
            mUserTimeLog = new JSONObject(application.getUserLog());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        bindView(rootView);

        return rootView;
    }

    private void bindView(View rootView) {

        ImageView userPic = (ImageView) rootView.findViewById(R.id.user_image);
        Picasso.with(mContext).load(mUrlImageUser).placeholder(R.drawable.user_placeholder).into(userPic);

        TextView userName = (TextView) rootView.findViewById(R.id.user_name);
        userName.setText(mUserName);

        TextView logTime = (TextView) rootView.findViewById(R.id.log_time);
        logTime.setText(getUserTimeLog());

        LinearLayout lastestMessage = (LinearLayout) rootView.findViewById(R.id.linear_lastest_message);

        for (int i = 0; i <  MAX_LASTEST_MESSAGE; i++) {
            try {
                JSONObject obj = mUserHistory.getJSONObject(i);
                lastestMessage.addView(getViewLastestMessage(obj));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getUserTimeLog() {
        DecimalFormat format = new DecimalFormat("#");

        final String active = UtilsJSON.getStringFromJSON(mUserTimeLog, KEY_ACTIVE_LOG);
        final String min = UtilsJSON.getStringFromJSON(mUserTimeLog, KEY_MIN_LOG);
        final String sign = (Float.valueOf(active) > Float.valueOf(min)) ? ">" : "<";

        return mContext.getString(R.string.time_log, format.format(Double.valueOf(active)), sign, min);
    }

    private View getViewLastestMessage(JSONObject object) throws JSONException {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_lastest_message, null);
        JSONObject nameObj = object.getJSONObject(KEY_LASTEST_MESSAGE_NAME);

        final String title = UtilsJSON.getStringFromJSON(object, KEY_LASTEST_MESSAGE);
        final String name = UtilsJSON.getStringFromJSON(nameObj, KEY_LASTEST_MESSAGE);

        TextView viewTitle = (TextView) v.findViewById(R.id.title_lastest_message);
        viewTitle.setText(title);

        TextView viewName = (TextView) v.findViewById(R.id.name_lastest_message);
        viewName.setText(name);
        return v;
    }
}
