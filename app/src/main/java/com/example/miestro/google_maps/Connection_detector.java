package com.example.miestro.google_maps;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by MIESTRO on 09/04/2018.
 */

public class Connection_detector {

    Context context;
    public Connection_detector(Context context){
        this.context = context;
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(MyBoundService.CONNECTIVITY_SERVICE);

        boolean x = false;
        if(connectivityManager !=null){

            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info != null){

                if(info.getState() ==NetworkInfo.State.CONNECTED){
                    x =  true;
                }

            }
        }

        return x;

    }

}
