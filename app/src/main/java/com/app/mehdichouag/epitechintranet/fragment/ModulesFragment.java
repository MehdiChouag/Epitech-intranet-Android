package com.app.mehdichouag.epitechintranet.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.adapter.ModulesAdapter;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.data.LogUser;
import com.app.mehdichouag.epitechintranet.model.Modules;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mehdichouag on 27/01/15.
 */
public class ModulesFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = ModulesFragment.class.getSimpleName();

    private boolean isFetchData = false;
    private List<Modules> mModulesList;
    private List<Integer> mSchoolYear = new ArrayList<>();
    private Spinner mSpinner;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private ModulesAdapter mAdapter;

    private String mUrl;


    public ModulesFragment() {
        // Required empty public constructor
    }

    public static ModulesFragment newInstance() {
        return new ModulesFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        final String TOKEN_PARAMETER = "token";

        mUrl = Uri.parse(getActivity().getString(R.string.api_modules)).buildUpon()
                .appendQueryParameter(TOKEN_PARAMETER, LogUser.getInstance(activity).getToken())
                .build().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_modules, container, false);
        final RequestQueue requestQueue = MyApplication.getInstance().getRequestQueue();

        bindView(rootView);
        if (requestQueue.getCache().get(mUrl) != null){
            try {
                JSONObject obj = new JSONObject(new String(requestQueue.getCache().get(mUrl).data));
                parseJSON(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            fetchData();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getInstance().cancelPendingRequests(TAG);
    }

    private void bindView(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.modules_list);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_download);
        mSpinner = (Spinner) rootView.findViewById(R.id.spinner_year);
        mSpinner.setOnItemSelectedListener(this);
        mSpinner.setEnabled(false);
    }

    private void fetchData() {
        mProgressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonResquest = new JsonObjectRequest(Request.Method.GET, mUrl, null, this, this);
        MyApplication.getInstance().addToRequestQueue(jsonResquest, TAG);
    }

    @Override
    public void onResponse(JSONObject response) {
        parseJSON(response);
    }

    private void parseJSON(JSONObject response) {

        final String KEY_MODULES = "modules";
        final String KEY_SCHOOL_YEAR = "scolaryear";
        final String KEY_GRADE = "grade";
        final String KEY_CREDITS = "credits";
        final String KEY_TITLE = "title";

        List<Modules> modulesList = new ArrayList<>();

        if (response.has(KEY_MODULES)) {
            try {
                JSONArray array = response.getJSONArray(KEY_MODULES);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    final String title = UtilsJSON.getStringFromJSON(obj, KEY_TITLE);

                    if (!title.isEmpty()) {
                        Modules model = new Modules();
                        final int year = UtilsJSON.getIntFromJSON(obj, KEY_SCHOOL_YEAR);

                        if (!mSchoolYear.contains(Integer.valueOf(year)))
                            mSchoolYear.add(year);

                        model.setCredit(UtilsJSON.getIntFromJSON(obj, KEY_CREDITS));
                        model.setGrade(UtilsJSON.getStringFromJSON(obj, KEY_GRADE));
                        model.setSchoolYear(year);
                        model.setTitle(title);
                        modulesList.add(model);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(mSchoolYear);
        mModulesList = modulesList;
        mAdapter = new ModulesAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);


        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_modules, mSchoolYear);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setEnabled(true);

        final int def = spinnerAdapter.getPosition(getHigherYear());
        mSpinner.setSelection(def);

        FilterByYear(getHigherYear());
        mProgressBar.setVisibility(View.INVISIBLE);
        isFetchData = true;
    }

    private void FilterByYear(int year)
    {
        List<Modules> moduleByYear = new ArrayList<>();
        for (Modules modules : mModulesList) {
            if (modules.getSchoolYear() == year)
                moduleByYear.add(modules);
        }
        mAdapter.setModulesList(moduleByYear);
    }

    private int getHigherYear() {
        int year = 0;
        for (Integer in : mSchoolYear)
            year = (year < in) ? in : year;
        return year;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (isFetchData) {
            final int year = Integer.valueOf(parent.getItemAtPosition(position).toString());
            FilterByYear(year);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
