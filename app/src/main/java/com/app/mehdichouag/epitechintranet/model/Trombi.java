package com.app.mehdichouag.epitechintranet.model;

import java.io.Serializable;

/**
 * Created by mehdichouag on 01/02/15.
 */
public class Trombi implements Serializable{

    private String title;
    private String login;
    private String nom;
    private String prenom;
    private String pictureUrl;

    public Trombi() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
