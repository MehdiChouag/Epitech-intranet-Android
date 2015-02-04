package com.app.mehdichouag.epitechintranet.fragment;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.adapter.PlanningAdapter;
import com.app.mehdichouag.epitechintranet.data.LogUser;
import com.app.mehdichouag.epitechintranet.model.TaskModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanningFragment extends Fragment implements AbsListView.OnScrollListener {
    private ListView mListView;
    private PlanningAdapter mAdapter;
    private ProgressBar mProgressBar;

    /* ON SCROLL VARS */
    private int visibleThreshold = 15;
    private int previousTotal = 0;
    private boolean loading = true;
    private Date beginDate;
    private int nbDaysToLoad = 7;

    public PlanningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlanningFragment.
     */
    public static PlanningFragment newInstance() {
        return new PlanningFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_planning, container, false);
        mListView = (ListView) rootView.findViewById(R.id.planning_list_view);
        mListView.setOnScrollListener(this);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_download_planning);
        mAdapter = new PlanningAdapter(PlanningFragment.this.getActivity());
        beginDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 7);
        String endDate = sdf.format(c.getTime());
        new FetchPlanning(true).execute(sdf.format(beginDate), endDate);
        c.add(Calendar.DATE, 1);
        beginDate = c.getTime();
        return rootView;
    }

    /**
     * Callback method to be invoked while the list view or grid view is being scrolled. If the
     * view is being scrolled, this method will be called before the next frame of the scroll is
     * rendered. In particular, it will be called before any calls to
     *
     * @param view        The view whose scroll state is being reported
     * @param scrollState The current scroll state. One of
     *                    {@link #SCROLL_STATE_TOUCH_SCROLL} or {@link #SCROLL_STATE_IDLE}.
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * Callback method to be invoked when the list or grid has been scrolled. This will be
     * called after the scroll has completed
     *
     * @param view             The view whose scroll state is being reported
     * @param firstVisibleItem the index of the first visible cell (ignore if
     *                         visibleItemCount == 0)
     * @param visibleItemCount the number of visible cells
     * @param totalItemCount   the number of items in the list adaptor
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(beginDate);
            c.add(Calendar.DATE, nbDaysToLoad);
            new PlanningFragment.FetchPlanning(false).execute(sdf.format(beginDate), sdf.format(c.getTime()));
            c.add(Calendar.DATE, 1);
            this.beginDate = c.getTime();
            loading = true;
        }
    }

    public class FetchPlanning extends AsyncTask<String, Void, List<TaskModel>> {
        /* JSON KEYS */
        private final String URI_TOKEN = "token";
        private final String URI_START_DATE = "start";
        private final String URI_END_DATE = "end";
        /* INNER CLASS VARS*/
        private boolean mProgressBarActive;

        public FetchPlanning(boolean progressBarActive) {
            this.mProgressBarActive = progressBarActive;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

        @Override
        protected void onPreExecute() {
            if (mProgressBarActive) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<TaskModel> doInBackground(String... params) {
            final String INTRA_KEY_DURATION = "nb_hours";
            final String INTRA_KEY_START_DATE = "start";
            final String INTRA_KEY_TITLE = "acti_title";
            final String INTRA_KEY_MODULE = "codemodule";
            final String INTRA_KEY_ROOM = "room";
            final String INTRA_KEY_ROOM_CODE = "code";
            final String INTRA_KEY_CODE_ACTI = "codeacti";
            final String INTRA_KEY_CODE_EVENT = "codeevent";
            final String INTRA_KEY_CODE_INSTANCE = "codeinstance";
            final String INTRA_KEY_CODE_MODULE = "codemodule";
            final String INTRA_KEY_SCOLAR_YEAR = "scolaryear";
            final String INTRA_KEY_ALLOW_TOKEN = "allow_token";
            final String INTRA_KEY_ALLOW_REGISTER = "allow_register";
            final String INTRA_KEY_MODULE_REGISTERED = "module_registered";
            final String INTRA_KEY_EVENT_REGISTERED = "event_registered";

            if (getActivity() == null)
                return null;

            String url = Uri.parse(getResources().getString(R.string.api_get_planning))
                    .buildUpon()
                    .appendQueryParameter(URI_TOKEN, LogUser.getInstance(PlanningFragment.this.getActivity()).getToken())
                    .appendQueryParameter(URI_START_DATE, params[0])
                    .appendQueryParameter(URI_END_DATE, params[1])
                    .build().toString();
            Log.v("URL", url);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response;
            InputStream inputStream = null;
            String json = "";

            try {
                response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = response.getEntity();
                    inputStream = httpEntity.getContent();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                inputStream.close();
                json = sb.toString();
                Log.v("JSON RESPONSE = ", json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<TaskModel> taskModelList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    TaskModel taskModel = new TaskModel();
                    taskModel.setDate(UtilsJSON.getDateFromJSON(object, INTRA_KEY_START_DATE));
                    taskModel.setDuration(UtilsJSON.getStringFromJSON(object, INTRA_KEY_DURATION));
                    if (object.has(INTRA_KEY_ROOM) && !UtilsJSON.getStringFromJSON(object, INTRA_KEY_ROOM).equals("null")) {
                        JSONObject roomaa = object.getJSONObject(INTRA_KEY_ROOM);
                        if (roomaa != null) {
                            taskModel.setRoom(UtilsJSON.getStringFromJSON(roomaa, INTRA_KEY_ROOM_CODE));
                        }
                    }
                    taskModel.setTitle(UtilsJSON.getStringFromJSON(object, INTRA_KEY_MODULE), UtilsJSON.getStringFromJSON(object, INTRA_KEY_TITLE));
                    taskModel.setCodeacti(UtilsJSON.getStringFromJSON(object, INTRA_KEY_CODE_ACTI));
                    taskModel.setCodeevent(UtilsJSON.getStringFromJSON(object, INTRA_KEY_CODE_EVENT));
                    taskModel.setCodemodule(UtilsJSON.getStringFromJSON(object, INTRA_KEY_CODE_MODULE));
                    taskModel.setCodeinstance(UtilsJSON.getStringFromJSON(object, INTRA_KEY_CODE_INSTANCE));
                    taskModel.setScolaryear(UtilsJSON.getStringFromJSON(object, INTRA_KEY_SCOLAR_YEAR));
                    if (object.has(INTRA_KEY_ALLOW_TOKEN)) {
                        taskModel.setAllowToken(object.getBoolean(INTRA_KEY_ALLOW_TOKEN));
                    }
                    if (object.has(INTRA_KEY_ALLOW_REGISTER)) {
                        taskModel.setAllowRegister(object.getBoolean(INTRA_KEY_ALLOW_REGISTER));
                    }
                    if (object.has(INTRA_KEY_MODULE_REGISTERED)) {
                        taskModel.setModuleRegistered(object.getBoolean(INTRA_KEY_MODULE_REGISTERED));
                    }
                    if (object.has(INTRA_KEY_EVENT_REGISTERED)) {
                        taskModel.setRegistered(object.getString(INTRA_KEY_EVENT_REGISTERED));
                    }
                    Log.v("TTT", taskModel.toString());
                    taskModelList.add(taskModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return taskModelList;
        }

        @Override
        protected void onPostExecute(List<TaskModel> l) {
            Collections.sort(l, new Comparator<TaskModel>() {
                @Override
                public int compare(TaskModel lhs, TaskModel rhs) {
                    return lhs.getDate().compareTo(rhs.getDate());
                }
            });
            if (mProgressBarActive) {
                mProgressBar.setVisibility(View.GONE);
            }
            TaskModel old = null;
            for (TaskModel task : l) {
                if (task.isModuleRegistered() && task.isAllowRegister()) {
                    if (old != null) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        if (formatter.format(old.getDate()).compareTo(formatter.format(task.getDate())) != 0) {
                            TaskModel taskModel = new TaskModel();
                            taskModel.setDate(task.getDate());
                            mAdapter.addSectionHeaderItem(taskModel);
                        }
                    } else {
                        TaskModel taskModel = new TaskModel();
                        taskModel.setDate(task.getDate());
                        mAdapter.addSectionHeaderItem(taskModel);
                    }
                    mAdapter.addItem(task);
                    old = task;
                }
            }
            int index = mListView.getFirstVisiblePosition();
            View v = mListView.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - mListView.getPaddingTop());
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mListView.setSelectionFromTop(index, top);
        }
    }

}
