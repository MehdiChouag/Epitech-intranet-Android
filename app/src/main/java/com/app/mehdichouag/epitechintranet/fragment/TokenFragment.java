package com.app.mehdichouag.epitechintranet.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.data.LogUser;
import com.app.mehdichouag.epitechintranet.model.TaskModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TokenFragment extends DialogFragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    public static final String TAG = TokenFragment.class.getSimpleName();

    public static final String TASK_KEY = "task";

    private TaskModel mTask;
    private EditText mToken;

    public TokenFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTask = (TaskModel) getArguments().getSerializable(TASK_KEY);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_token, null);
        mToken = (EditText) v.findViewById(R.id.token_text);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.send_token, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //
                    }
                })
                .setNegativeButton(R.string.cancel_token, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TokenFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToken();
                }
            });
        }
    }

    private void sendToken() {
        if (mToken.getText().length() == 8) {
            String url = Uri.parse(getResources().getString(R.string.api_validate_token))
                    .buildUpon()
                    .appendQueryParameter("token", LogUser.getInstance(TokenFragment.this.getActivity()).getToken())
                    .appendQueryParameter("scolaryear", mTask.getScolaryear())
                    .appendQueryParameter("codemodule", mTask.getCodemodule())
                    .appendQueryParameter("codeinstance", mTask.getCodeinstance())
                    .appendQueryParameter("codeacti", mTask.getCodeacti())
                    .appendQueryParameter("codeevent", mTask.getCodeevent())
                    .appendQueryParameter("tokenvalidationcode", mToken.getText().toString())
                    .build().toString();

            JsonObjectRequest jsonResquest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            MyApplication.getInstance().addToRequestQueue(jsonResquest, TAG);
        }
    }

    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(TokenFragment.this.getActivity(), getResources().getString(R.string.token_not_validated), Toast.LENGTH_SHORT).show();
        dismiss();
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        Log.v("RESPONE TOKEN", response.toString());
        try {
            if (response.has("error") && !response.getString("error").equals("null")) {
                Toast.makeText(TokenFragment.this.getActivity(), response.getString("error"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TokenFragment.this.getActivity(), getResources().getString(R.string.token_validated), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dismiss();
    }
}
