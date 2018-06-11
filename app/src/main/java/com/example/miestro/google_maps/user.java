package com.example.miestro.google_maps;

/**
 * Created by MIESTRO on 09/06/2018.
 */

public class user {

    String name,id;

    String profile_url,cover_url;

    public user(String id, String name,String profile_url,String  cover_url){
        setId(id);
        setName(name);
        setProfile_url(profile_url);
        setCover_url(cover_url);


    }
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
