package com.app.mehdichouag.epitechintranet.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.adapter.ProjectAdapter;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.data.LogUser;
import com.app.mehdichouag.epitechintranet.model.Project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdichouag on 31/01/15.
 */
public class ProjectFragment extends ListFragment implements Response.Listener<JSONArray> {

    public static final String TAG = ProjectFragment.class.getSimpleName();

    private List<Project> mProjectList;
    private ProjectAdapter mAdapter;

    /* URL of the request */
    private String mUrl;

    public ProjectFragment() {
    }

    public static ProjectFragment newInstance() {
        return new ProjectFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        final String URI_TOKEN = "token";

        mUrl = Uri.parse(getString(R.string.api_get_projects)).buildUpon()
                .appendQueryParameter(URI_TOKEN, LogUser.getInstance(activity).getToken())
                .build().toString();
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RequestQueue requestQueue = MyApplication.getInstance().getRequestQueue();

        if (requestQueue.getCache().get(mUrl) != null) {
            try {
                JSONArray obj = new JSONArray(new String(requestQueue.getCache().get(mUrl).data));
                parseJSON(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            fetchData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void fetchData() {
        JsonArrayRequest jsonResquest = new JsonArrayRequest(mUrl, this, null);
        MyApplication.getInstance().addToRequestQueue(jsonResquest, TAG);
    }

    @Override
    public void onResponse(JSONArray response) {
        parseJSON(response);
    }

    private void onParsingComplete() {
        mAdapter = new ProjectAdapter(getActivity(), mProjectList);
        setListAdapter(mAdapter);
    }

    private void parseJSON(JSONArray response) {
        final String KEY_TYPE_PROJECT = "type_acti";
        final String KEY_MODULE_NAME = "title_module";
        final String KEY_PROJECT_NAME = "acti_title";
        final String KEY_PROJECT_END = "end_acti";

        List<Project> projects = new ArrayList<>();


        for (int i = 0;  i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                if (UtilsJSON.getStringFromJSON(obj, KEY_TYPE_PROJECT).contains("Projet")) {
                    Project project = new Project();

                    project.setModuleName(UtilsJSON.getStringFromJSON(obj, KEY_MODULE_NAME));
                    project.setProjectName(UtilsJSON.getStringFromJSON(obj, KEY_PROJECT_NAME));
                    project.setProjectEnd(UtilsJSON.getStringFromJSON(obj, KEY_PROJECT_END));

                    projects.add(project);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mProjectList = projects;
        onParsingComplete();
    }
}
