package com.example.miestro.google_maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by MIESTRO on 06/04/2018.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private  String mTitle;
    private  String mSnippet;



    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }


  /*  public MyItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }
*/

    public void setTitle(String mTitle) {
        this.mTitle =mTitle ;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
