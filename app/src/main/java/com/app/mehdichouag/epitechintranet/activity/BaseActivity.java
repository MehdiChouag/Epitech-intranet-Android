package com.app.mehdichouag.epitechintranet.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.app.mehdichouag.epitechintranet.R;

/**
 * Created by mehdichouag on 05/01/15.
 */

public abstract class BaseActivity extends ActionBarActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRessource());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null){
            setSupportActionBar(mToolbar);
        }
    }

    protected abstract int getLayoutRessource();

}
