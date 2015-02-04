package com.app.mehdichouag.epitechintranet.model;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskModel implements Serializable {
    private Date date;
    private String timeStart;
    private String duration;
    private String title;
    private String room;
    private String codeacti;
    private String codeevent;
    private String codeinstance;
    private String codemodule;
    private String scolaryear;
    private boolean allowToken;
    private boolean allowRegister;
    private boolean moduleRegistered;
    private String registered;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        boolean hours = false;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            this.timeStart = "";
            if (calendar.get(Calendar.HOUR_OF_DAY) != 0) {
                this.timeStart = calendar.get(Calendar.HOUR_OF_DAY) + "h";
                hours = true;
            }
            if (calendar.get(Calendar.MINUTE) != 0) {
                if (hours) {
                    this.timeStart += " " + calendar.get(Calendar.MINUTE) + "m";
                } else {
                    this.timeStart = calendar.get(Calendar.MINUTE) + "m";
                }
            }
        }
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleHeader() {
        String DATE_FORMAT_NOW = "EEEE dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(date);
    }

    public void setTitle(String moduleCode, String title) {
        this.title = moduleCode + " - " + title;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String toString() {
        return timeStart + " " + duration + " " + title + " " + room;
    }

    public String getCodeacti() {
        return codeacti;
    }

    public void setCodeacti(String codeacti) {
        this.codeacti = codeacti;
    }

    public String getCodeevent() {
        return codeevent;
    }

    public void setCodeevent(String codeevent) {
        this.codeevent = codeevent;
    }

    public String getCodeinstance() {
        return codeinstance;
    }

    public void setCodeinstance(String codeinstance) {
        this.codeinstance = codeinstance;
    }

    public String getCodemodule() {
        return codemodule;
    }

    public void setCodemodule(String codemodule) {
        this.codemodule = codemodule;
    }

    public String getScolaryear() {
        return scolaryear;
    }

    public void setScolaryear(String scolaryear) {
        this.scolaryear = scolaryear;
    }

    public boolean isAllowToken() {
        return allowToken;
    }

    public void setAllowToken(boolean allowToken) {
        this.allowToken = allowToken;
    }

    public boolean isAllowRegister() {
        return allowRegister;
    }

    public void setAllowRegister(boolean allowRegister) {
        this.allowRegister = allowRegister;
    }

    public boolean isModuleRegistered() {
        return moduleRegistered;
    }

    public void setModuleRegistered(boolean moduleRegistered) {
        this.moduleRegistered = moduleRegistered;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }
}
