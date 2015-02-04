package com.app.mehdichouag.epitechintranet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.data.LogUser;

import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener, TextWatcher, Response.Listener<JSONObject>, Response.ErrorListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    public static final String URI_LOGIN = "login";
    public static final String URI_PASSWORD = "password";

    /* UI COMPONENT */
    private EditText mLogin;
    private EditText mPassword;
    private Button mConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLogin = (EditText) findViewById(R.id.login);
        mPassword = (EditText) findViewById(R.id.password);
        mConnexion = (Button) findViewById(R.id.btn_valid);

        mLogin.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
        mConnexion.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void onClick(View v) {
        String login = mLogin.getText().toString();
        String password = mPassword.getText().toString();

        /* Disable the button while the request ! */
        mConnexion.setEnabled(false);
        mLogin.setEnabled(false);
        mPassword.setEnabled(false);

        fetchData(login, password);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String login = mLogin.getText().toString();
        String password = mPassword.getText().toString();

        if (!login.isEmpty() && password.length() == 8)
            mConnexion.setEnabled(true);
        else
            mConnexion.setEnabled(false);
    }

    @Override
    public void onResponse(JSONObject response) {
        final String API_TOKEN_KEY = "token";


        final String token = UtilsJSON.getStringFromJSON(response, API_TOKEN_KEY);

        assert token != null;
        LogUser.getInstance(LoginActivity.this).setToken(token);
        Intent i = new Intent(LoginActivity.this, SplashScreenActivity.class);
        startActivity(i);
        finish();

    }

    private void buildAlertDialogConnection() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.login_alert_title))
                .setMessage(getResources().getString(R.string.login_alert_message))
                .setNeutralButton(getResources().getString(R.string.login_alert_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setEnableField();
                    }
                });
        alertDialog.create().show();
    }

    private void setEnableField() {
        mConnexion.setEnabled(true);
        mLogin.setEnabled(true);
        mPassword.setEnabled(true);

        mLogin.setText("");
        mPassword.setText("");
    }

    private void fetchData(String login, String password) {

        String url = Uri.parse(getResources().getString(R.string.api_get_token))
                .buildUpon()
                .appendQueryParameter(URI_LOGIN, login)
                .appendQueryParameter(URI_PASSWORD, password)
                .build().toString();

        Log.d("TEST", url);

        JsonObjectRequest jsonResquest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        MyApplication.getInstance().addToRequestQueue(jsonResquest, TAG);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        buildAlertDialogConnection();
    }
}
