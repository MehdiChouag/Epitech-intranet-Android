package com.app.mehdichouag.epitechintranet.drawer;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by mehdichouag on 05/01/15.
 */
public class CustomDrawerToggle extends ActionBarDrawerToggle {

    DrawerToggleCallBack mListener;

    public CustomDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    public void setDrawerToggleListener(DrawerToggleCallBack listener){
        mListener = listener;
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        if (mListener != null)
            mListener.onDrawerClosed();
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        if (mListener != null)
            mListener.onDrawerOpened();
    }
}
