package com.app.mehdichouag.epitechintranet.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.adapter.SusiesAdapter;
import com.app.mehdichouag.epitechintranet.model.Susie;

public class DetailActivity extends BaseActivity {
    private TextView mTitle;
    private TextView mTime;
    private TextView mType;
    private TextView mRegistered;
    private TextView mAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mTitle = (TextView) findViewById(R.id.detail_title);
        mTime = (TextView) findViewById(R.id.detail_time);
        mType = (TextView) findViewById(R.id.detail_type);
        mRegistered = (TextView) findViewById(R.id.detail_registered);
        mAvailable = (TextView) findViewById(R.id.detail_available);
        Susie susie = (Susie) getIntent().getSerializableExtra(SusiesAdapter.ViewHolder.TAG_SUSIE);
        mTitle.setText(getResources().getString(R.string.detail_title_string,
                susie.getTitle(),
                susie.getSusieName()));
        mTime.setText(getResources().getString(R.string.detail_time_string,
                susie.getStartDate(),
                susie.getEndDate()));
        mType.setText(getResources().getString(R.string.detail_susie_type,
                susie.getType()));
        mRegistered.setText(getResources().getString(R.string.detail_susie_registered,
                susie.getRegistered()));
        mAvailable.setText(getResources().getString(R.string.detail_susie_available,
                susie.getNbPlace() - susie.getRegistered()));
    }

    @Override
    protected int getLayoutRessource() {
        return R.layout.activity_detail;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
