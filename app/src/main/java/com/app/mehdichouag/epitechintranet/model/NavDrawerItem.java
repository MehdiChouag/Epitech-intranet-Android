package com.app.mehdichouag.epitechintranet.model;

/**
 * Created by mehdichouag on 05/01/15.
 */
public class NavDrawerItem {

    private String mTitle;
    private int mIcon;

    public NavDrawerItem(String title, int icon) {
        this.mIcon = icon;
        this.mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int mIcon) {
        this.mIcon = mIcon;
    }

}
