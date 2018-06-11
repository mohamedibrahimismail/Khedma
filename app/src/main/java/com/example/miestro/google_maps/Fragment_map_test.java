package com.example.miestro.google_maps;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Fragment_map_test extends Fragment implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,ClusterManager.OnClusterItemClickListener{

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Float DEFAULT_ZOOM = 15f;
    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136));
    private PlaceInfo mPlace;

    private Dialog mdilaog;
    private TextView name, Job;
    private ImageView agent_photo;
    private ImageView[] rating=new ImageView[5];
    private Agent_Dialog_Info agent_dialog_info;


    private static String url = "http://192.168.1.4:800/khedma/get_agent_location.php";
    ArrayList<agent_item> listagent = new ArrayList<agent_item>();
    ClusterManager<MyItem> mClusterManager;


    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    private void intit(){
        Log.d(TAG,"init: initializing ");

        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
         //       .enableAutoManage(this, this)
                .build();


        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(),mGoogleApiClient,LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyevent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyevent.getAction() == KeyEvent.ACTION_DOWN
                        || keyevent.getAction() == KeyEvent.KEYCODE_ENTER){


                    //execute our method for searching
                    geoLocate();

                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: clicked gps icon ");
                getDeviceLocation();
            }
        });
    }


    private void geoLocate() {
        Log.d(TAG,"geoLocate: geolocating");
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString,1);

        }catch(IOException e){
            Log.d(TAG,"geoLocate: IOException"+e.getMessage());
        }
        if(list.size()>0){
            Address address = list.get(0);
            Log.d(TAG,"geoLocate: found a location: "+address.toString());
            //move the camera to the new location
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Map is Ready here");
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            intit();
            setUpClusterer();
        }
    }


    public void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation:- getting thw device current Location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

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
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,"My Location");
                            }else{
                                Toast.makeText(getContext(),"Please check GPS is enabled",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Log.d(TAG,"onComplete: location is null!");
                            Toast.makeText(getContext(),"unable to get location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG,"getDeviceLocation: SecrityException: "+e.getMessage());

        }
    }

    private void moveCamera(LatLng latLng,float zoom,String Title){
        Log.d(TAG,"MoveCamera to lat :"+latLng.latitude+" ,long :"+latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

      /*  if(!Title.equals("My Location")){
        MarkerOptions options = new MarkerOptions().position(latLng).title(Title);
        mMap.addMarker(options);
    }*/}



    private void getlocationPermission(){
        Log.d(TAG,"Getting Location Permissions");
        String[] permissions ={Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if((ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            mLocationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(),permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap(){
        Log.d(TAG,"Initialize the Map");
       // SupportMapFragment mFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
       // mFragment.getMapAsync(this);
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


    private void hideSoftKeyboard(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeID = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient,placeID);
            //set request for the place
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>(){
        //when request successfully received we will get the place we looking for
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            if(!places.getStatus().isSuccess()){
                Log.d(TAG,"onResult: place query didn't complete successfully: "+places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{

                mPlace =new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG,"onResult: Name :-  " +place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG,"onResult: Address :-  " +place.getAddress().toString() );
                // mPlace.setAttributions(place.getAttributions().toString());
                //Log.d(TAG,"onResult: getAttributions:-  " +place.getAttributions().toString() );
                mPlace.setId(place.getId());
                Log.d(TAG,"onResult: getId:-  " +place.getId() );
                mPlace.setLatLng(place.getLatLng());
                Log.d(TAG,"onResult: getLatLng:-  " +place.getLatLng() );
                mPlace.setRating(place.getRating());
                Log.d(TAG,"onResult: getRating:-  " +place.getRating() );
                mPlace.setPhonenumber(place.getPhoneNumber().toString());
                Log.d(TAG,"onResult: getPhoneNumber:-  " +place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG,"onResult: getWebsiteUri:-  " +place.getWebsiteUri());
                Log.d(TAG,"onResult: place:-  " +mPlace.toString() );


            }catch (NullPointerException e){
                Log.d(TAG,"onResult: NullPointerException: "+e.getMessage());
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude),DEFAULT_ZOOM,mPlace.getName());
            places.release();

        }
    };


    private void setUpClusterer() {
        // Position the map.

        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));
        mClusterManager = new ClusterManager<MyItem>(getActivity(), mMap);


        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.setOnClusterItemClickListener(this);

        addItems(mClusterManager);
    }

    private void addItems(ClusterManager<MyItem> mClusterManager) {
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

        Mysingleton.getmInstatnce(getContext()).addToRequestque(jsonObjectRequest);



    }

    public void show(){
        double lat;
        double lang;
        MyItem offsetItem;
        for(int i=0;i<listagent.size();i++){
            agent_item item = listagent.get(i);
            // textView.append("id:"+item.id+" lat:"+item.lat+" lang:"+item.lang+"\n");

            lat = item.lat ;
            lang = item.lang;
            offsetItem = new MyItem(lat, lang);
            offsetItem.setTitle(""+item.id);
            mClusterManager.addItem(offsetItem);


        }


    }

    public Fragment_map_test() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mdilaog = new Dialog(getActivity());
        mdilaog.setContentView(R.layout.dialog_contacts);
        mdilaog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        name = (TextView) mdilaog.findViewById(R.id.dilaog_name_id);
        Job = (TextView) mdilaog.findViewById(R.id.dilaog_Occupation_id);
        agent_photo = (ImageView) mdilaog.findViewById(R.id.profile_image);
        progressBar = (ProgressBar)mdilaog.findViewById(R.id.progressbar);
        relativeLayout = (RelativeLayout)mdilaog.findViewById(R.id.dialog_layout);

        rating[0] = (ImageView) mdilaog.findViewById(R.id.star1);
        rating[1] = (ImageView) mdilaog.findViewById(R.id.star2);
        rating[2] = (ImageView) mdilaog.findViewById(R.id.star3);
        rating[3] = (ImageView) mdilaog.findViewById(R.id.star4);
        rating[4] = (ImageView) mdilaog.findViewById(R.id.star5);

        mGps = (ImageView)getActivity().findViewById(R.id.ic_gps);
        mSearchText =(AutoCompleteTextView)getActivity().findViewById(R.id.input_search);
        getlocationPermission();

        if (mLocationPermissionGranted) {
            initMap();


        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public boolean onClusterItemClick(final ClusterItem clusterItem) {

        Log.d("onClusterItemClick","-------------------------------");

        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        mdilaog.show();


        Response.Listener<String> responselisener=new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                //  textView.setText(""+response);
                try {

                    JSONObject jsonResponse =new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("agents");

                    // JSONArray jsonArray = response.getJSONArray("agents");
                    for(int i =0; i<jsonArray.length();i++){
                        JSONObject respons = jsonArray.getJSONObject(i);
                        int rate = Integer.parseInt(respons.getString("rating"));
                        String name = respons.getString("name");
                        String job = respons.getString("job");
                        String img_link = respons.getString("img_link");
                        Log.d("JSONObject","---------------------------");
                        //textView.append(id+" "+lat+" "+lang+"\n");
                        // listagent.add(new agent_item(id,lat,lang));
                        agent_dialog_info = new Agent_Dialog_Info(Integer.parseInt(clusterItem.getTitle()),rate,name,job,img_link);
                    }

                    if(agent_dialog_info !=null){

                        show_dialog_info();

                    }


                    //                   boolean success = jsonResponse.getBoolean("success");



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };


        Send_Agent_Id send_data = new Send_Agent_Id(clusterItem.getTitle(),responselisener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(send_data);





        return true;
    }

    public void show_dialog_info(){


        Log.d("show_dialog_info","---------------------------");

        Picasso.with(getContext()).load(agent_dialog_info.getImg_link()).into(agent_photo);

        for(int a =0;a<5;a++){
            rating[a].setImageResource(R.drawable.star_border);
        }

        for(int a =0;a<agent_dialog_info.getRating();a++){
            rating[a].setImageResource(R.drawable.star);
        }

        name.setText(agent_dialog_info.getName());
        Job.setText( agent_dialog_info.getJob());

        progressBar.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);

        //  Toast.makeText(MapActivity.this, "Latitude " + clusterItem.getPosition().latitude, Toast.LENGTH_LONG).show();


    }
}
