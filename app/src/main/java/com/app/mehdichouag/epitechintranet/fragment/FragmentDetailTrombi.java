package com.app.mehdichouag.epitechintranet.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.Utils.UtilsJSON;
import com.app.mehdichouag.epitechintranet.application.MyApplication;
import com.app.mehdichouag.epitechintranet.data.LogUser;
import com.app.mehdichouag.epitechintranet.model.Trombi;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by mehdichouag on 01/02/15.
 */
public class FragmentDetailTrombi extends Fragment implements Response.Listener<JSONObject> {

    public static final String TAG = FragmentDetailTrombi.class.getSimpleName();

    public static final String KEY_BUNDLE_TROMBI = "com.app.mehdichouag.mehdichouag.epitechintranet.fragment.KEY_BUNDLE_TROMBI";

    private static final String KEY_JSON_EMAIL = "internal_email";
    private static final String KEY_JSON_PHONE = "telephone";
    private static final String KEY_JSON_GPA = "gpa";

    /* UI COMPONENT */
    private ImageView mUserPicture;
    private TextView mUserName;
    private ProgressBar mProgressBar;
    private LinearLayout mGpa;
    private LinearLayout mCardContainer;

    private HashMap<String, String> mInfos;
    private Trombi mTrombi;

    private String mUrl;

    public FragmentDetailTrombi() {
    }

    public static FragmentDetailTrombi newInstance(Trombi trombi) {
        FragmentDetailTrombi fragment = new FragmentDetailTrombi();
        Bundle b = new Bundle();

        b.putSerializable(KEY_BUNDLE_TROMBI, trombi);

        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String URI_TOKEN = "token";
        final String URI_USER = "user";

        Bundle b = getArguments();
        if (b != null) {
            mTrombi = (Trombi) b.getSerializable(KEY_BUNDLE_TROMBI);
            mUrl = Uri.parse(getString(R.string.api_get_user)).buildUpon()
                    .appendQueryParameter(URI_TOKEN, LogUser.getInstance(getActivity()).getToken())
                    .appendQueryParameter(URI_USER, mTrombi.getLogin())
                    .build().toString();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trombi_detail, container, false);
        final RequestQueue requestQueue = MyApplication.getInstance().getRequestQueue();

        bindView(view);

        if (requestQueue.getCache().get(mUrl) != null) {
            try {
                JSONObject obj = new JSONObject(new String(requestQueue.getCache().get(mUrl).data));
                parseJSON(obj);
                onParseComplete();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            fetchData();
        return view;
    }


    private void bindView(View view) {
        mUserPicture = (ImageView) view.findViewById(R.id.trombi_picture);
        Picasso.with(getActivity()).load(getString(R.string.api_user_picture, mTrombi.getLogin()))
                .into(mUserPicture);

        mUserName = (TextView) view.findViewById(R.id.name_user);
        mUserName.setText(mTrombi.getTitle());

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_download);

        mGpa = (LinearLayout) view.findViewById(R.id.gpa_container);

        mCardContainer = (LinearLayout) view.findViewById(R.id.card_container);
    }

    private void fetchData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mUrl, null, this, null);
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
        mProgressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResponse(JSONObject response) {
        parseJSON(response);
        mProgressBar.setVisibility(View.GONE);
        onParseComplete();
    }

    private void parseJSON(JSONObject response) {
        HashMap<String, String> infos = new HashMap<>();

        final String KEY_JSON_PHONE_VALUE = "value";
        final String KEY_JSON_USER_INFOS = "userinfo";

        try {

            infos.put(KEY_JSON_EMAIL, UtilsJSON.getStringFromJSON(response, KEY_JSON_EMAIL));

            JSONObject userInfosObj = response.getJSONObject(KEY_JSON_USER_INFOS);
            JSONArray userGpaArray = response.getJSONArray(KEY_JSON_GPA);
            JSONObject userGpaObj = userGpaArray != null ? userGpaArray.getJSONObject(userGpaArray.length() - 1) : null;

            if (userInfosObj != null && userInfosObj.has(KEY_JSON_PHONE)) {
                infos.put(KEY_JSON_PHONE, UtilsJSON.getStringFromJSON(userInfosObj.getJSONObject(KEY_JSON_PHONE), KEY_JSON_PHONE_VALUE));
            }
            if (userGpaObj != null && userGpaObj.has(KEY_JSON_GPA)) {
                infos.put(KEY_JSON_GPA, String.valueOf(UtilsJSON.getDoubleFromJSON(userGpaObj, KEY_JSON_GPA)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mInfos = infos;
    }

    private void onParseComplete() {
        mCardContainer.setVisibility(View.VISIBLE);

        CardView mail = (CardView) mCardContainer.findViewById(R.id.card_mail);
        CardView contact = (CardView) mCardContainer.findViewById(R.id.card_add_contact);
        CardView phone = (CardView) mCardContainer.findViewById(R.id.card_call);

        if (mInfos == null)
            mInfos = new HashMap<>();

        if (mInfos.get(KEY_JSON_EMAIL) != null) {
            mail.setVisibility(View.VISIBLE);
            mail.setOnClickListener(mailListener);
            ((ImageView) mail.findViewById(R.id.ic_card)).setImageResource(R.drawable.ic_send_mail);
            ((TextView) mail.findViewById(R.id.action_card)).setText(getString(R.string.send_email_trombi, mTrombi.getPrenom()));
        }

        contact.setVisibility(View.VISIBLE);
        contact.setOnClickListener(contactListener);
        ((ImageView) contact.findViewById(R.id.ic_card)).setImageResource(R.drawable.ic_add_contact);
        ((TextView) contact.findViewById(R.id.action_card)).setText(getString(R.string.add_contact_trombi, mTrombi.getPrenom()));

        if (mInfos.get(KEY_JSON_GPA) != null) {
            mGpa.setVisibility(View.VISIBLE);
            ((TextView) mGpa.findViewById(R.id.gpa_user)).setText(mInfos.get(KEY_JSON_GPA));
        }

        if (mInfos.get(KEY_JSON_PHONE) != null) {
            phone.setVisibility(View.VISIBLE);
            phone.setOnClickListener(phoneListener);
            ((ImageView) phone.findViewById(R.id.ic_card)).setImageResource(R.drawable.ic_phone);
            ((TextView) phone.findViewById(R.id.action_card)).setText(getString(R.string.call_user_trombi, mTrombi.getPrenom()));
        }
    }

    View.OnClickListener mailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{mInfos.get(KEY_JSON_EMAIL)});
            email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_email_trombi, mTrombi.getPrenom()));
            email.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_email_trombi));
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
    };

    View.OnClickListener phoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mInfos.get(KEY_JSON_PHONE)));
            startActivity(intent);
        }
    };

    View.OnClickListener contactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

            intent.putExtra(ContactsContract.Intents.Insert.NAME, mTrombi.getTitle());
            intent.putExtra(ContactsContract.Intents.Insert.COMPANY, "Epitech");
            if (mInfos.get(KEY_JSON_PHONE) != null)
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, mInfos.get(KEY_JSON_PHONE));
            if (mInfos.get(KEY_JSON_EMAIL) != null)
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, mInfos.get(KEY_JSON_EMAIL));
            startActivity(intent);
        }
    };
}
