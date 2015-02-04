package com.app.mehdichouag.epitechintranet.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.fragment.FragmentDetailTrombi;
import com.app.mehdichouag.epitechintranet.model.Trombi;

/**
 * Created by mehdichouag on 01/02/15.
 */
public class TrombiDetaiActivity extends BaseActivity {

    public static final String TAG = TrombiDetaiActivity.class.getSimpleName();

    public static final String KEY_USER_TROMBI_OBJ = "com.app.mehdichouag.epitechintranet.fragment.FragmentDetailTrombi.KEY_USER_TROMBI_OBJ";

    private FragmentDetailTrombi mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Trombi trombi  = (Trombi) getIntent().getSerializableExtra(KEY_USER_TROMBI_OBJ);
        mFragment = getAndAddFragment(trombi);
        getSupportActionBar().hide();
    }

    private FragmentDetailTrombi getAndAddFragment(Trombi trombi) {
        FragmentManager ft = getSupportFragmentManager();
        FragmentDetailTrombi fragment;

        fragment = (FragmentDetailTrombi)ft.findFragmentByTag(FragmentDetailTrombi.TAG);
        if (fragment == null) {
            fragment = FragmentDetailTrombi.newInstance(trombi);
            ft.beginTransaction().replace(R.id.container, fragment, FragmentDetailTrombi.TAG).commit();
        }
        return fragment;
    }

    @Override
    protected int getLayoutRessource() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.finishAfterTransition();
        else
            this.finish();
    }
}
