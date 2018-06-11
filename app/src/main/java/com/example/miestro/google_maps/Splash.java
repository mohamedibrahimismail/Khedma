package com.example.miestro.google_maps;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MIESTRO on 19/02/2018.
 */

public class Splash extends Fragment {

    Splash_Communicator splash_communicator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Thread thread = new Thread(){
            @Override
            public void run() {
                try {

                    sleep(2000);
                    splash_communicator =(Splash_Communicator)getActivity();
                    splash_communicator.splash_switcher();

                    //Toast.makeText(getContext(),"waked",Toast.LENGTH_SHORT).show();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        thread.start();
       // Toast.makeText(getActivity()," waked ",Toast.LENGTH_SHORT).show();

    }


}
