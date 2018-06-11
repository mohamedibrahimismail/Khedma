package com.example.miestro.google_maps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MIESTRO on 09/04/2018.
 */

public class Myservice extends Service {

    private static String url = "http://169.254.239.242:800/khedma/update_agent_location.php";
    double lat =1;
    double lang = 1;
    int id = 1;
    final class MyThreadClass implements Runnable{


       int service_id;
        MyThreadClass(int service_id){
            this.service_id = service_id;
        }

        @Override
        public void run() {

            int i=0;
            synchronized (this){

                while(i<10){
                    try {
                        wait(1500);
                       Toast.makeText(getApplicationContext(),i,Toast.LENGTH_SHORT).show();
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopSelf(service_id);
            }

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this,"service started...",Toast.LENGTH_SHORT).show();

        //Thread thread = new Thread(new MyThreadClass(startId));
        //thread.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("update");
                            Toast.makeText(getApplication(),code,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id",id+"");
                params.put("lat",lat+"");
                params.put("lang",lang+"");
                return params;
            }
        };

        Mysingleton.getmInstatnce(getApplicationContext()).addToRequestque(stringRequest);



        return START_STICKY; }

    @Override
    public void onDestroy() {

        Toast.makeText(this,"service destroyed...",Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
