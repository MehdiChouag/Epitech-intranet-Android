package com.app.mehdichouag.epitechintranet.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.adapter.SusiesAdapter;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.data.LogUser;
import com.app.mehdichouag.epitechintranet.model.Susie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SusiesFragment extends Fragment implements Response.Listener<JSONArray>, Response.ErrorListener {

    public static final String TAG = SusiesFragment.class.getSimpleName();

    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;
    private List<Susie> mSusieList = null;
    private SusiesAdapter mAdapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    private String mUrl;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        final String URI_TOKEN = "token";
        final String URI_START_DATE = "start";
        final String URI_END_DATE = "end";

        mUrl = Uri.parse(getResources().getString(R.string.api_get_susies))
                .buildUpon()
                .appendQueryParameter(URI_TOKEN, LogUser.getInstance(activity).getToken())
                .appendQueryParameter(URI_START_DATE, "2014-05-10")
                .appendQueryParameter(URI_END_DATE, "2015-02-12")
                .build().toString();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SusiesFragment() {
    }

    public static SusiesFragment newInstance() {
        return new SusiesFragment();
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final RequestQueue requestQueue = MyApplication.getInstance().getRequestQueue();

        View rootView = inflater.inflate(R.layout.fragment_susies, container, false);
        bindView(rootView, container.getContext());
        if (requestQueue.getCache().get(mUrl) != null){
            try {
                JSONArray obj = new JSONArray(new String(requestQueue.getCache().get(mUrl).data));
                Log.d(TAG, new String(requestQueue.getCache().get(mUrl).data));
                parseJSON(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            fetchData(true);

        return rootView;
    }

    private void bindView(View rootView, Context context) {

        final int row = context.getResources().getInteger(R.integer.number_item_per_row);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.susies_recycler);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.susies_swipe_layout);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_download);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), row);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new SusiesAdapter(mSusieList, getActivity());
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                initiateRefresh();
            }
        });
    }

    private void initiateRefresh() {
        fetchData(false);
    }

    private void onRefreshComplete() {
        // Remove all items from the ListAdapter, and then replace them with the new items

        mAdapter.setSusieList(mSusieList);
        mAdapter.notifyDataSetChanged();

        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void fetchData(boolean showProgress) {

        if (showProgress)
            mProgressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonResquest = new JsonArrayRequest(mUrl, this, this);
        int socketTimeout = 15000; //15 seconds - to timeout

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonResquest.setRetryPolicy(policy);
        MyApplication.getInstance().addToRequestQueue(jsonResquest, TAG);
    }



    @Override
    public void onResponse(JSONArray response) {
        Log.d(TAG, "DONE");
        parseJSON(response);
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(), "Timeout", Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.GONE);
    }

    private void parseJSON(JSONArray jsonArray) {
        final String INTRA_KEY_NB_PLACE = "nb_place";
        final String INTRA_KEY_REGISTERED = "registered";
        final String INTRA_KEY_START_DATE = "start";
        final String INTRA_KEY_TITLE = "title";
        final String INTRA_KEY_TYPE = "type";
        final String INTRA_KEY_MAKER = "maker";
        final String INTRA_KEY_MAKER_NAME = "title";
        final String INTRA_KEY_ID = "id";

        List<Susie> susieList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Susie susie = new Susie();
                susie.setId(UtilsJSON.getIntFromJSON(object, INTRA_KEY_ID));
                susie.setNbPlace(UtilsJSON.getIntFromJSON(object, INTRA_KEY_NB_PLACE));
                susie.setRegistered(UtilsJSON.getIntFromJSON(object, INTRA_KEY_REGISTERED));
                susie.setStartDate(UtilsJSON.getStringFromJSON(object, INTRA_KEY_START_DATE));
                susie.setTitle(UtilsJSON.getStringFromJSON(object, INTRA_KEY_TITLE));
                susie.setType(UtilsJSON.getStringFromJSON(object, INTRA_KEY_TYPE));
                JSONObject makerObject = object.getJSONObject(INTRA_KEY_MAKER);
                susie.setSusieName(UtilsJSON.getStringFromJSON(makerObject, INTRA_KEY_MAKER_NAME));
                susieList.add(susie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSusieList = susieList;
        onRefreshComplete();
    }
}
