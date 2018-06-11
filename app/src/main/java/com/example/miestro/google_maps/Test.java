package com.example.miestro.google_maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    public TextView textView;
    private static String url = "http://192.168.1.4:800/khedma/get_agent_location.php";
    ArrayList<agent_item> listagent = new ArrayList<agent_item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView = (TextView)findViewById(R.id.text);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("agents");
                           // text_total_comments.setText(String.valueOf(jsonArray.length()));

                            for(int i =0; i<jsonArray.length();i++){
                                JSONObject respons = jsonArray.getJSONObject(i);
                                int id = Integer.parseInt(respons.getString("id"));
                                double lat =Double.parseDouble(respons.getString("lat"));
                                double lang = Double.parseDouble(respons.getString("lang"));
                                //textView.append(id+" "+lat+" "+lang+"\n");
                                listagent.add(new agent_item(id,lat,lang));


                            }

                            if(listagent!=null) {
                                show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", "VOLLY");
            }
        }

        );

        Mysingleton.getmInstatnce(getApplicationContext()).addToRequestque(jsonObjectRequest);




     /*   getlocationPermission();

        if (mLocationPermissionGranted) {
            getDeviceLocation();
        }*/
    }


    public void show(){
        for(int i=0;i<listagent.size();i++){
            agent_item item = listagent.get(i);
            textView.append("id:"+item.id+" lat:"+item.lat+" lang:"+item.lang+"\n");

        }
    }


    public void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation:- getting thw device current Location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"onComplete: found location!");
                            Location currentLocation = (Location)task.getResult();
                            if (currentLocation!=null) {
                                //moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,"My Location");
                                Toast.makeText(Test.this,"lat:"+currentLocation.getLatitude()+" lang:"+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(Test.this,"Please check GPS is enabled",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Log.d(TAG,"onComplete: location is null!");
                            Toast.makeText(Test.this,"unable to get location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG,"getDeviceLocation: SecrityException: "+e.getMessage());

        }

    }


    private void getlocationPermission(){
        Log.d(TAG,"Getting Location Permissions");
        String[] permissions ={Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if((ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            mLocationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult is called");
        mLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length>0){
                    for (int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG,"onRequestPermissionsResult :- permission failed");
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.d(TAG,"onRequestPermissionsResult :- permission Granted");
                    //initialize our map


                }
        }
    }

}
