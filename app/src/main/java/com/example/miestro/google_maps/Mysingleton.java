package com.example.miestro.google_maps;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by MIESTRO on 09/04/2018.
 */

public class Mysingleton {
    private static Mysingleton mInstatnce;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private Mysingleton(Context context){

        mCtx = context;
        requestQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue(){

        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return requestQueue;
    }

    public static synchronized Mysingleton getmInstatnce(Context context){
        if(mInstatnce==null){
            mInstatnce = new Mysingleton(context);

        }
        return mInstatnce;
    }


    public <T>void addToRequestque(Request<T> request){

        requestQueue.add(request);
    }


}
