package com.example.miestro.google_maps;

/**
 * Created by MIESTRO on 22/04/2018.
 */

public class Agent_Dialog_Info {
    int id,rating;
    String name;
    String job;
    String img_link;

    public Agent_Dialog_Info(int id,int rating,String name,String job,String img_link){
        setId(id);
        setRating(rating);
        setImg_link(img_link);
        setName(name);
        setJob(job);


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }


}
