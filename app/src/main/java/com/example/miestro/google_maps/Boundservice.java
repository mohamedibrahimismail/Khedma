package com.example.miestro.google_maps;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by MIESTRO on 08/04/2018.
 */

public class Boundservice extends Service {

    private final IBinder mBinder = new LocalService();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class LocalService extends Binder{

     Boundservice getService(){

         return Boundservice.this;
     }

    }

    final class  MythreadClass implements Runnable{



        @Override
        public void run() {

            int i=0;
            synchronized (this){

                while(i<5){

                    try {
                        wait(1500);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }

        }

    }

    public String sleep(){
        Thread thread = new Thread(new Boundservice.MythreadClass());
        thread.start();
        return "finished";
    }

    public String getSecondMessage(){
        return "second message";
    }
}
