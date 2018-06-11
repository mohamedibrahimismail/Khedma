package com.example.miestro.google_maps;

import android.Manifest;
import android.app.Dialog;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;

public class MainActivity extends AppCompatActivity {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    boolean network_state = false;
    public TextView ConnenctionBar;
    Button button;
    EditText showmessage;
    Boundservice myservices;
    Messenger messenger = null;
    boolean isbind=false;
    private  static  final  String TAG="MainActivity";
    private  static  final int ERROR_DIALOG_REQUEST=9001;
    private static String url = "http://169.254.239.242:800/khedma/update_agent_location.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isServicesok()){
            intit();
        }



    }



    public void intit(){
        ConnenctionBar = (TextView)findViewById(R.id.connection_bar);
        button =(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MapActivity.class));
            }
        });
        getlocationPermission();
        check_connection();
    }

    public void check_connection(){
        Connection_detector connection_detector = new Connection_detector(this);
        network_state = connection_detector.isConnected();
        if(network_state){
            Toast.makeText(this,"connected",Toast.LENGTH_SHORT).show();
            Thread thread = new Thread((new MythreadClass()));
            thread.start();



        }else{
            Toast.makeText(this,"no connection",Toast.LENGTH_SHORT).show();
        }

    }

    public void start(View view) {

        Intent intent = new Intent(this,Intentservice.class);
        startService(intent);
    }

    public void stop(View view) {
        Intent intent = new Intent(this,Intentservice.class);
        stopService(intent);
    }

    public void Test(View view) {
        Intent intent = new Intent(this,Test.class);
        startActivity(intent);
    }


    final class MythreadClass implements Runnable{

        @Override
        public void run() {

            ConnenctionBar.setText("Connected");
            ConnenctionBar.setBackgroundResource(R.color.green);
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

    public void hide(){
        ConnenctionBar.setVisibility(View.GONE);
    }

    public boolean isServicesok(){
        Log.d(TAG,"isServicesok: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            //every thing is ok and user can make map request
            Log.d(TAG,"isServicesok: Google play services is working");
            return  true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG,"isServicesok: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this,"You can't make map requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    double lat =1;
    double lang = 1;
    int id = 1;


    public void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation:- getting thw device current Location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                lat = currentLocation.getLatitude();
                                lang = currentLocation.getLongitude();
                                Toast.makeText(MainActivity.this, "lat:" + lat + " lang:" + lang, Toast.LENGTH_LONG).show();
                                // moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,"My Location");
                            } else {
                                Toast.makeText(MainActivity.this, "Please check GPS is enabled", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.d(TAG, "onComplete: location is null!");
                            Toast.makeText(MainActivity.this, "unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                getlocationPermission();
            }
        }catch (SecurityException e){
            Log.e(TAG,"getDeviceLocation: SecrityException: "+e.getMessage());

        }
    }

    public void connect(View view) {




        getDeviceLocation();



        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("update");
                            Toast.makeText(MainActivity.this,code,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
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
    //services




}
