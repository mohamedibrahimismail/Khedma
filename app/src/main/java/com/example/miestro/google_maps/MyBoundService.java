package com.example.miestro.google_maps;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by MIESTRO on 08/04/2018.
 */

public class MyBoundService extends Service {
    Messenger mMessenger = new Messenger(new IncomingHandler());
    static final int Job_1 =1;
    static final int Job_2 =2;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(),"Service Binding ....",Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    class IncomingHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case Job_1:
                   Toast.makeText(getApplicationContext(),"job 1",Toast.LENGTH_SHORT).show();
                   break;
               case Job_2:
                   Toast.makeText(getApplicationContext(),"job 2",Toast.LENGTH_SHORT).show();
                   break;
               default:
                   super.handleMessage(msg);
           }


        }
    }
}
