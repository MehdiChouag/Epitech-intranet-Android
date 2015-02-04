package com.app.mehdichouag.epitechintranet.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.adapter.NavDrawerListAdapter;
import com.app.mehdichouag.epitechintranet.drawer.CustomDrawerToggle;
import com.app.mehdichouag.epitechintranet.drawer.DrawerToggleCallBack;
import com.app.mehdichouag.epitechintranet.fragment.HomeFragment;
import com.app.mehdichouag.epitechintranet.fragment.MarksFragment;
import com.app.mehdichouag.epitechintranet.fragment.ModulesFragment;
import com.app.mehdichouag.epitechintranet.fragment.PlanningFragment;
import com.app.mehdichouag.epitechintranet.fragment.ProjectFragment;
import com.app.mehdichouag.epitechintranet.fragment.SusiesFragment;
import com.app.mehdichouag.epitechintranet.fragment.TrombiFragment;
import com.app.mehdichouag.epitechintranet.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements DrawerToggleCallBack, AdapterView.OnItemClickListener {

    public static final String WINDOWS_TITLE = "com.app.mehdichouag.epitechintranet.activity.WINDOWS_TITLE";
    public static final String POSITION_FRAGMENT = "com.app.mehdichouag.epitechintranet.activity.POSITION_FRAGMENT";

    private int mPositionDrawer = -1;

    /*  Drawer Resources  (title, icon) */
    private String[] mFragmentTitle;
    private TypedArray mIconRessource;

    /* ActionBar title depending the drawer state */
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    private ActionBar mActionBar;
    private DrawerLayout mDrawer;
    private ListView mDrawList;
    private CustomDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBar();
        setUi();

        mTitle = mDrawerTitle = getTitle();
        if (savedInstanceState == null) {
            setFragmentDrawer(0);
        } else {
            setTitle(savedInstanceState.getCharSequence(WINDOWS_TITLE));
            mPositionDrawer = savedInstanceState.getInt(POSITION_FRAGMENT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(WINDOWS_TITLE, mTitle);
        outState.putInt(POSITION_FRAGMENT, mPositionDrawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected int getLayoutRessource() {
        return R.layout.activity_main;
    }

    /* Fill the Drawer's List */
    private void setDrawerList() {

        List<NavDrawerItem> nav = new ArrayList<>();
        mFragmentTitle = getResources().getStringArray(R.array.drawer_item);
        mIconRessource = getResources().obtainTypedArray(R.array.drawer_icon);

        for (int i = 0; i < mFragmentTitle.length; i++) {
            nav.add(new NavDrawerItem(mFragmentTitle[i], mIconRessource.getResourceId(i, R.drawable.ic_launcher)));
        }

        mDrawList.setAdapter(new NavDrawerListAdapter(this, R.layout.item_list_drawer, nav));
    }

    /* Setting the ActionBar */
    private void setActionBar() {
        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDefaultDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        }
    }

    /* Setting up the DrawerLayout */
    private void setUi() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawList = (ListView) findViewById(R.id.drawerList);

        mDrawList.setOnItemClickListener(this);

        mDrawerToggle = new CustomDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setDrawerToggleListener(this);
        mDrawer.setDrawerListener(mDrawerToggle);
        setDrawerList();
    }

    /* Change the fragment by the position click on the drawer */
    private void setFragmentDrawer(int position) {
        Fragment fragment = null;

        if (mPositionDrawer == position)
            return;

        mPositionDrawer = position;

        switch (position) {
            case 0:
                fragment = HomeFragment.newInstance();
                break;
            case 1:
                fragment = PlanningFragment.newInstance();
                break;
            case 2:
                fragment = MarksFragment.newInstance();
                break;
            case 3:
                fragment = ProjectFragment.newInstance();
                break;
            case 4:
                fragment = ModulesFragment.newInstance();
                break;
            case 5:
                fragment = SusiesFragment.newInstance();
                break;
            case 6:
                fragment = TrombiFragment.newIstance();
            default:
                break;
        }
        setTitle(mFragmentTitle[position]);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        mActionBar.setTitle(title);
    }

    /* Callback Drawer close */
    @Override
    public void onDrawerClosed() {
        mActionBar.setTitle(mTitle);
    }

    /* Callback Drawer open */
    @Override
    public void onDrawerOpened() {
        mActionBar.setTitle(mDrawerTitle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setFragmentDrawer(position);
        mDrawer.closeDrawers();
    }
}
