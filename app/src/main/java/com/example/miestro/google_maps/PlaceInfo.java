package com.example.miestro.google_maps;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by MIESTRO on 25/02/2018.
 */

public class PlaceInfo {

    private String name;
    private String address;
    private String phonenumber;
    private String id;
    private Uri websiteUri;
    private LatLng latLng;
    private float rating;
    private String attributions;

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", id='" + id + '\'' +
                ", websiteUri=" + websiteUri +
                ", latLng=" + latLng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                '}';
    }

    public PlaceInfo(String name, String address, String phonenumber, String id, Uri websiteUri, LatLng latLng, float rating, String attributions){
       setName(name);
       setAddress(address);
       setPhonenumber(phonenumber);
       setId(id);
       setWebsiteUri(websiteUri);
       setLatLng(latLng);
       setRating(rating);
       setAttributions(attributions);
    }
    public PlaceInfo(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }
}
