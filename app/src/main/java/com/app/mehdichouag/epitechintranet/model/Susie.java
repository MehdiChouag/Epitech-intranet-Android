package com.app.mehdichouag.epitechintranet.model;

import java.io.Serializable;

public class Susie implements Serializable {
    private String title;
    private String startDate;
    private String endDate;
    private String susieName;
    private SusieType type;
    private int nbPlace;
    private int registered;
    private int id;

    public Susie() {

    }

    public Susie(String title, String startdate, String endDate, String susieName, SusieType type) {
        this.title = title;
        this.startDate = startdate;
        this.endDate = endDate;
        this.type = type;
        this.susieName = susieName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSusieName() {
        return susieName;
    }

    public void setSusieName(String susieName) {
        this.susieName = susieName;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getNbPlace() {
        return nbPlace;
    }

    public void setNbPlace(int nbPlace) {
        this.nbPlace = nbPlace;
    }

    public int getRegistered() {
        return registered;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public SusieType getType() {
        return type;
    }

    public void setType(String type) {
        switch (type) {
            case "fun":
                this.type = SusieType.FUN;
                break;
            default:
                this.type = SusieType.UNDEFINED;
                break;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void register() {

    }

    public void unregister() {

    }

    public enum SusieType {
        UNDEFINED,
        CULTURE,
        READING,
        FUN,
    }
}
