package com.app.mehdichouag.epitechintranet.model;

import java.io.Serializable;

/**
 * Created by mehdichouag on 28/01/15.
 */
public class Modules implements Serializable {

    private int mSchoolYear;
    private int mCredit;
    private String mTitle;
    private String mGrade;

    public Modules() {

    }

    public int getSchoolYear() {
        return mSchoolYear;
    }

    public void setSchoolYear(int mSchoolYear) {
        this.mSchoolYear = mSchoolYear;
    }

    public int getCredit() {
        return mCredit;
    }

    public void setCredit(int mCredit) {
        this.mCredit = mCredit;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getGrade() {
        return mGrade;
    }

    public void setGrade(String mGrade) {
        this.mGrade = mGrade;
    }
}
