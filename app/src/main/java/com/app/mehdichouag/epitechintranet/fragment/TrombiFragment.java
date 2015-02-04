package com.app.mehdichouag.epitechintranet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.activity.TrombiDetaiActivity;
import com.app.mehdichouag.epitechintranet.adapter.TrombiAdapter;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.callback.TrombiClickListener;
import com.app.mehdichouag.epitechintranet.data.LogUser;
import com.app.mehdichouag.epitechintranet.fragment.dialog.DialogActionTrombi;
import com.app.mehdichouag.epitechintranet.model.Trombi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

/**
 * Created by mehdichouag on 01/02/15.
 */
public class TrombiFragment extends Fragment implements Response.Listener<JSONObject>, TrombiClickListener {

    public final static String TAG = TrombiFragment.class.getSimpleName();

    /* KEY JSON */
    private final String URI_TOKEN = "token";
    private final String URI_LOCATION = "location";
    private final String URI_YEAR = "year";
    private final String URI_PROMO = "promo";
    private final String URI_OFFSET = "offset";

    private String mUrl;

    private List<Trombi> mTrombiList;

    private int mTotalEntries = -1;
    private TrombiAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar mProgressBar;

    private boolean mLoading = true;

    public TrombiFragment() {

    }

    public static TrombiFragment newIstance() {
        return new TrombiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mUrl = Uri.parse(getString(R.string.api_trombi)).buildUpon()
                .appendQueryParameter(URI_TOKEN, LogUser.getInstance(activity).getToken())
                .appendQueryParameter(URI_YEAR, "2014")
                .appendQueryParameter(URI_LOCATION, LogUser.getInstance(activity).getUserLocation())
                .appendQueryParameter(URI_PROMO, getString(R.string.promo_trombi, LogUser.getInstance(activity).getUserYear()))
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

        View rootView = inflater.inflate(R.layout.fragment_trombi, container, false);

        bindView(rootView);

        if (requestQueue.getCache().get(mUrl) != null){
            try {
                JSONObject obj = new JSONObject(new String(requestQueue.getCache().get(mUrl).data));
                onDownloadComplete(parseJSON(obj));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            fetchData();

        return rootView;
    }

    private void bindView(View rootView) {

        final int itemPerRow = getResources().getInteger(R.integer.trombi_number_item);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_download);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.trombi_recycler);
        mLayoutManager = new GridLayoutManager(getActivity(), itemPerRow);
        mRecycler.setLayoutManager(mLayoutManager);
        mAdapter = new TrombiAdapter(mTrombiList, getActivity(), this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setOnScrollListener(new TrombiScrollListener());
    }

    private void fetchData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mUrl, null, this, null);
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponse(JSONObject response) {
        onDownloadComplete(parseJSON(response));
        mProgressBar.setVisibility(View.GONE);
    }

    private void onDownloadComplete(List<Trombi> list) {
        mTrombiList = list;
        mAdapter.setTrombi(mTrombiList);
    }

    private List<Trombi> parseJSON(JSONObject response) {

        final String KEY_ITEMS = "items";
        final String KEY_TITLE = "title";
        final String KEY_LOGIN = "login";
        final String KEY_LASTNAME = "nom";
        final String KEY_FISTNAME = "prenom";
        final String KEY_PICTURE = "picture";
        final String KEY_TOTAL = "total";

        List<Trombi> trombis = new ArrayList<>();

        try {
            JSONArray array = response.getJSONArray(KEY_ITEMS);
            mTotalEntries = UtilsJSON.getIntFromJSON(response, KEY_TOTAL);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                Trombi trombi = new Trombi();

                trombi.setTitle(UtilsJSON.getStringFromJSON(obj, KEY_TITLE));
                trombi.setLogin(UtilsJSON.getStringFromJSON(obj, KEY_LOGIN));
                trombi.setNom(UtilsJSON.getStringFromJSON(obj, KEY_LASTNAME));
                trombi.setPrenom(UtilsJSON.getStringFromJSON(obj, KEY_FISTNAME));
                trombi.setPictureUrl(UtilsJSON.getStringFromJSON(obj, KEY_PICTURE));

                trombis.add(trombi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trombis;
    }

    private void fetchEndLessDate() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getEndLessUrl(), null, endLessListener, null);
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    private String getEndLessUrl() {
        return Uri.parse(getString(R.string.api_trombi)).buildUpon()
                .appendQueryParameter(URI_TOKEN, LogUser.getInstance(getActivity()).getToken())
                .appendQueryParameter(URI_YEAR, "2014")
                .appendQueryParameter(URI_LOCATION, LogUser.getInstance(getActivity()).getUserLocation())
                .appendQueryParameter(URI_PROMO, getString(R.string.promo_trombi, LogUser.getInstance(getActivity()).getUserYear()))
                .appendQueryParameter(URI_OFFSET, String.valueOf(mTrombiList.size()))
                .build().toString();
    }

    @Override
    public void OnLongClickTrombi(int position) {
        Trombi trombi = mTrombiList.get(position);
        showAlertDialog(trombi);
    }

    @Override
    public void OnClickTrombi(int position, ImageView image) {
        Trombi trombi = mTrombiList.get(position);
        Intent i = new Intent(getActivity(), TrombiDetaiActivity.class);
        i.putExtra(TrombiDetaiActivity.KEY_USER_TROMBI_OBJ, trombi);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image, getString(R.string.transition_picture_user));
            getActivity().startActivity(i, option.toBundle());
        } else
            startActivity(i);
    }

    private class TrombiScrollListener extends OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            final LinearLayoutManager manager = TrombiFragment.this.mLayoutManager;
            final RequestQueue requestQueue = MyApplication.getInstance().getRequestQueue();

            final int visibleItemCount = manager.getChildCount();
            final int totalItemCount = manager.getItemCount();
            final int pastVisibleItem = manager.findFirstVisibleItemPosition();

            if (mLoading && mTrombiList != null && mTrombiList.size() > 0)
                if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
                       if (mTrombiList.size() < mTotalEntries) {
                           mLoading = false;
                           if (requestQueue.getCache().get(getEndLessUrl()) != null) {
                               JSONObject obj;
                               try {
                                   obj = new JSONObject(new String(requestQueue.getCache().get(getEndLessUrl()).data));
                                   OnEndLessComplete(parseJSON(obj));
                                   mLoading = true;
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           } else
                               fetchEndLessDate();
                       }
                }

        }
    }

    Response.Listener<JSONObject> endLessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            mLoading = true;
            OnEndLessComplete(parseJSON(response));
        }
    };

    private void OnEndLessComplete(List<Trombi> list) {
        for (Trombi trombi : list)
            mTrombiList.add(trombi);
        mAdapter.setTrombi(mTrombiList);
    }

    private void showAlertDialog(Trombi trombi) {
        FragmentManager fm = getFragmentManager();
        DialogActionTrombi actionTrombi = DialogActionTrombi.newInstance(trombi);
        actionTrombi.show(fm, DialogActionTrombi.TAG);
    }
}
