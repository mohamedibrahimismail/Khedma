package com.example.miestro.google_maps;

import android.app.Application;

/**
 * Created by MIESTRO on 19/08/2017.
 */

public class Globalv extends Application {

    private String user_name;
    private String id;
    private String profile_url;
    private String cover_url;
    private byte[] profile_img;


    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    private byte[] cover_img;

    public byte[] getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(byte[] profile_img) {
        this.profile_img = profile_img;
    }

    public byte[] getCover_img() {
        return cover_img;
    }

    public void setCover_img(byte[] cover_img) {
        this.cover_img = cover_img;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
