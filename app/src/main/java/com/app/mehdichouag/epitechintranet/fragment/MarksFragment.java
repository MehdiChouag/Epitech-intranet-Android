package com.app.mehdichouag.epitechintranet.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.adapter.MarksAdapter;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.data.LogUser;
import com.app.mehdichouag.epitechintranet.model.Marks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarksFragment extends ListFragment implements Response.Listener<JSONObject>, AdapterView.OnItemLongClickListener {

    public static final String TAG = Marks.class.getSimpleName();

    private List<Marks> mMarksList;
    private MarksAdapter mAdapter;

    private String mUrl;

    public MarksFragment() {
        // Required empty public constructor
    }

    public static MarksFragment newInstance() {
        return new MarksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        final String URI_TOKEN = "token";

        mUrl = Uri.parse(getResources().getString(R.string.api_marks))
                .buildUpon()
                .appendQueryParameter(URI_TOKEN, LogUser.getInstance(activity).getToken())
                .build().toString();
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final RequestQueue requestQueue = MyApplication.getInstance().getRequestQueue();

        if (requestQueue.getCache().get(mUrl) != null) {
            try {
                JSONObject obj = new JSONObject(new String(requestQueue.getCache().get(mUrl).data));
                parseJSON(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            fetchData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getInstance().cancelPendingRequests(TAG);
    }

    public void onDowloadComplete() {

        Collections.reverse(mMarksList);
        mAdapter = new MarksAdapter(getActivity(), mMarksList);
        setListAdapter(mAdapter);
    }

    private void fetchData() {
        JsonObjectRequest jsonResquest = new JsonObjectRequest(Request.Method.GET, mUrl, null, this, null);
        MyApplication.getInstance().addToRequestQueue(jsonResquest, TAG);
    }

    @Override
    public void onResponse(JSONObject response) {
        parseJSON(response);
    }

    private void parseJSON(JSONObject jsonObject) {

        List<Marks> marksList;

        final String INTRA_KEY_PROJECT = "title";
        final String INTRA_KEY_MARKS = "final_note";
        final String INTRA_KEY_FIRST_NODE = "notes";

        try {
            if (jsonObject.has(INTRA_KEY_FIRST_NODE)) {
                marksList = new ArrayList<>();
                JSONArray array = jsonObject.getJSONArray(INTRA_KEY_FIRST_NODE);
                for (int i = 0; i < array.length(); i++) {
                    jsonObject = array.getJSONObject(i);
                    final double finalMark = UtilsJSON.getDoubleFromJSON(jsonObject, INTRA_KEY_MARKS);
                    final String projectName = UtilsJSON.getStringFromJSON(jsonObject, INTRA_KEY_PROJECT);
                    marksList.add(new Marks(projectName, finalMark));
                }
                mMarksList = marksList;
                onDowloadComplete();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent sendIntent = new Intent();
        Marks marks = mAdapter.getMarks().get(position);
        Context ctx = getActivity();

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, ctx.getString(R.string.title_sharing_marks, marks.getProjectName()));
        sendIntent.putExtra(Intent.EXTRA_TEXT, ctx.getString(R.string.content_sharing_marks, new DecimalFormat("0.##").format(marks.getMark()), marks.getProjectName()));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

        return true;
    }
}
