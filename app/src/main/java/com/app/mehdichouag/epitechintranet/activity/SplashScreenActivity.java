package com.app.mehdichouag.epitechintranet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.Utils.UtilsNetwork;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.data.LogUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mehdichouag on 06/01/15.
 */
public class SplashScreenActivity extends Activity implements Response.Listener<JSONObject> {

    public static final String TAG = SplashScreenActivity.class.getSimpleName();

    private final String URI_TOKEN = "token";

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mUrl = Uri.parse(getResources().getString(R.string.api_infos))
                .buildUpon()
                .appendQueryParameter(URI_TOKEN, LogUser.getInstance(this).getToken())
                .build().toString();

        if (!UtilsNetwork.isOnline(this)) {
            buildAlertDialog();
        } else {

            if (LogUser.getInstance(SplashScreenActivity.this).isLogged())
                fecthData();
            else {
                Intent i;
                i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    private void fecthData() {
        JsonObjectRequest jsonResquest = new JsonObjectRequest(Request.Method.GET, mUrl, null, this, null);
        MyApplication.getInstance().addToRequestQueue(jsonResquest, TAG);
    }

    private void buildAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.alert_dialog_title))
                .setMessage(getResources().getString(R.string.alert_dialog_message))
                .setNeutralButton(getResources().getString(R.string.alert_dialog_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.create().show();
    }


    @Override
    public void onResponse(JSONObject response) {

        final String INTRA_KEY_HISTORY = "history";
        final String INTRA_KEY_CURRENT = "current";
        final String INTRA_KEY_INFOS = "infos";
        final String INTRA_KEY_LOGIN = "login";
        final String INTRA_KEY_NAME = "title";
        final String INTRA_KEY_LOCATION = "location";
        final String INTRA_KEY_SCHOOLYEAR = "studentyear";

        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);

        try {
            final String history = response.getJSONArray(INTRA_KEY_HISTORY).toString();
            final String log = response.getJSONObject(INTRA_KEY_CURRENT).toString();
            final JSONObject infos = response.getJSONObject(INTRA_KEY_INFOS);


            final String name = UtilsJSON.getStringFromJSON(infos, INTRA_KEY_NAME);
            final String login = UtilsJSON.getStringFromJSON(infos, INTRA_KEY_LOGIN);
            final String location = UtilsJSON.getStringFromJSON(infos, INTRA_KEY_LOCATION);
            final int studentYear = UtilsJSON.getIntFromJSON(infos, INTRA_KEY_SCHOOLYEAR);

            LogUser.getInstance(this).setUserInfos(name, login, location, studentYear);

            MyApplication.getInstance().setUserHistory(history);
            MyApplication.getInstance().setUserLog(log);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().cancelPendingRequests(TAG);
    }

}
