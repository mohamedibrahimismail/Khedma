package com.example.miestro.google_maps;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MIESTRO on 08/04/2018.
 */

public class Intentservice extends IntentService {
    Context context;
    public Intentservice() {
        super("My_Worker_Thread");


    }
    private static String url = "http://192.168.1.4:800/khedma/update_agent_location.php";
    double lat =.0 ;
    double lang=.0 ;
    int id = 1;
    int i = 0;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    int running ;
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this," intent service started",Toast.LENGTH_SHORT).show();
        running = 0;
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        running = 1;
        Toast.makeText(this,"intent service stoped",Toast.LENGTH_SHORT).show();
    }

    public void getDeviceLocation(){
        //Log.d(TAG,"getDeviceLocation:- getting thw device current Location");
        Toast.makeText(getApplication(), "getDeviceLocation1" ,Toast.LENGTH_LONG).show();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplication());
       // Toast.makeText(getApplication(), "getDeviceLocation" ,Toast.LENGTH_LONG).show();
        try{
            if(mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                           // Log.d(TAG, "onComplete: found location!");
                            Toast.makeText(getApplication(), "found location!" ,Toast.LENGTH_LONG).show();
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                lat = currentLocation.getLatitude();
                                lang = currentLocation.getLongitude();
                                Toast.makeText(getApplication(), "lat:" + lat + " lang:" + lang, Toast.LENGTH_LONG).show();
                                // moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,"My Location");
                            } else {
                                Toast.makeText(getApplication(), "Please check GPS is enabled", Toast.LENGTH_LONG).show();
                                getlocationPermission();
                            }
                        } else {
                           // Log.d(getApplication(), "onComplete: location is null!");
                            Toast.makeText(getApplication(), "unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
               // getlocationPermission();
                Toast.makeText(getApplication(), "you should get location permission", Toast.LENGTH_SHORT).show();
            }
        }catch (SecurityException e){
           // Log.e(TAG,"getDeviceLocation: SecrityException: "+e.getMessage());

        }
    }


    private void getlocationPermission(){
        //Log.d(TAG,"Getting Location Permissions");
        String[] permissions ={Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if((ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            mLocationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions((Activity)getApplicationContext(),permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }




    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

       synchronized (this){


            while(true){

                if(running==1){

                    break;
                }
                try {

                    //-----------------------------------------
                    getlocationPermission();
                    getDeviceLocation();

                  //  getDeviceLocation();


                    if(lat!=.0&&lang!=.0) {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {

                                            JSONObject jsonObject = new JSONObject(response);
                                            String code = jsonObject.getString("update");
                                            Toast.makeText(getApplication(), code, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", id + "");
                                params.put("lat", lat + "");
                                params.put("lang", lang + "");
                                return params;
                            }
                        };


                        Mysingleton.getmInstatnce(getApplicationContext()).addToRequestque(stringRequest);

                    }
                        wait(20500);
                        i++;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            // You don't needed to write this code ,the services will stop the services itself.
         // stopSelf();



        }



    }
}
