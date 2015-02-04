package com.app.mehdichouag.epitechintranet.model;

import java.io.Serializable;

/**
 * Created by mehdichouag on 31/01/15.
 */
public class Project implements Serializable {

    private String projectEnd;
    private String projectName;
    private String moduleName;

    public Project() {
        super();
    }

    public String getProjectEnd() {
        return projectEnd;
    }

    public void setProjectEnd(String projectEnd) {
        this.projectEnd = projectEnd;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
