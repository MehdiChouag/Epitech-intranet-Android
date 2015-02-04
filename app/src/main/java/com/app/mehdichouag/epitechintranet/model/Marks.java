package com.app.mehdichouag.epitechintranet.model;

import java.io.Serializable;

/**
 * Created by mehdichouag on 19/01/15.
 */
public class Marks implements Serializable {
    private String mProjectName;
    private double mMark;

    public Marks(String project, double marks) {
        this.mMark = marks;
        this.mProjectName = project;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public double getMark() {
        return mMark;
    }
}
