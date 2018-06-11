package com.example.miestro.google_maps;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Begining extends AppCompatActivity implements Splash_Communicator,Register_Communicator,SignIn_Communicator,FragmentManager.OnBackStackChangedListener {
    Boolean is_opend = false;
    SignIn_fragment signIn_fragment;
    Globalv globalv;
    FragmentManager manager;
    DB_Sqlite db_sqlite;
    public String URl_Site="http://192.168.1.4:800/khedma/";
    private static final String profile_url="http://192.168.1.4:800/khedma/";
    private static final String cover_url="http://192.168.1.4:800/khedma/";

    // private SharedPreferences sharedPreferences;
   // private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beginning);
        getSupportActionBar().hide();
        db_sqlite = new DB_Sqlite(this);
        globalv=(Globalv)getApplicationContext();
        if(savedInstanceState!=null){
          this.is_opend=savedInstanceState.getBoolean("is_opend");
        }
        if(!is_opend) {
            manager = getFragmentManager();
            manager.addOnBackStackChangedListener(this);
            Splash splash = new Splash();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, splash, "spalsh");
            transaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_opend",true);
    }

    @Override
    public void splash_switcher() {

     //   String name = sharedPreferences.getString("name","");

        ArrayList<user> listdata = db_sqlite.getAllrecord();

        if(!listdata.isEmpty()){

            user user = listdata.get(0);
            globalv.setId(user.getId());
            globalv.setUser_name(user.getName());
            globalv.setProfile_url(user.getProfile_url());
            globalv.setCover_url(user.getCover_url());

            startActivity(new Intent(Begining.this,home.class));
            finish();
        }
        else {
            SignIn_fragment rg = new SignIn_fragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, rg, "register_fragment");
            // transaction.addToBackStack("add_register_fragment");
            transaction.commit();
            //startActivity(new Intent(MainActivity.this,home.class));
            //finish();
        }

    }

    @Override
    public void Register_switcher(String id,String name) {

        globalv.setId(id);
        globalv.setUser_name(name);
        startActivity(new Intent(Begining.this,home.class));
        Toast.makeText(this,"welcome "+globalv.getUser_name(),Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void SignIn_switcher(String status,String name,String id,String profile_img_url,String cover_img_url) {

        switch (status){
            case "signin":
                globalv.setUser_name(name);
                globalv.setId(id);

                if(profile_img_url.equals(null)&&cover_img_url.equals(null) ) {
                    startActivity(new Intent(Begining.this, home.class));
                    Toast.makeText(this, "welcome " + globalv.getUser_name(), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    db_sqlite.updateData(globalv.getId(),"profile",profile_img_url);
                    db_sqlite.updateData(globalv.getId(),"cover",cover_img_url);
                    globalv.setProfile_url(profile_img_url);
                   // Toast.makeText(this, globalv.getProfile_url(), Toast.LENGTH_SHORT).show();
                    globalv.setCover_url(cover_img_url);
                    startActivity(new Intent(Begining.this, home.class));
                    Toast.makeText(this, "welcome " + globalv.getUser_name(), Toast.LENGTH_SHORT).show();
                    finish();
/*                    DownloadImageTask downloadImageTask1 = new DownloadImageTask(this);
                    downloadImageTask1.type="profile";
                    downloadImageTask1.execute(URl_Site+profile_img_url);

                    DownloadImageTask downloadImageTask2 = new DownloadImageTask(this);
                    downloadImageTask2.type="cover";
                    downloadImageTask2.execute(URl_Site+cover_img_url);
*/

                }

                break;

            case "register":
                register_fragment rg = new register_fragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container,rg,"register_fragment");
                transaction.addToBackStack("add_register_fragment");
                transaction.commit();
                break;
        }


    }

    @Override
    public void onBackStackChanged() {



    }

  /*  @Override
    public void Communicator_Caching(String id, String type, byte[] image) {
        if(type.equals("profile")) {
            db_sqlite.updateimages(globalv.getId(),"profile",image);
        }else if(type.equals("cover")){
            db_sqlite.updateimages(globalv.getId(),"cover",image);
        }
    }

/*
    class DownloadImageTask extends AsyncTask<String,Integer,String> {
        Communicator_Caching communicator_caching ;
        String type="";
        byte[] image_byte;
        ProgressDialog progressDialog;

        public DownloadImageTask(Activity activity){

            communicator_caching = (Communicator_Caching)activity;

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Begining.this);
            progressDialog.setTitle("تحميل بياناتك....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String path = params[0];
            int file_length = 0;
            try {

                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();
               // File new_folder = new File("sdcard/photoalbum");
               /* if(!new_folder.exists()){
                    new_folder.mkdir();
                }
                *///File input_file = new File(new_folder,"downloaded_image.jpg");
       /*         InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                //OutputStream outputStream = new FileOutputStream(input_file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while((count=inputStream.read(data))!=-1)
                {

                    total+= count;
                    // outputStream.write(data,0,count);
                    byteArrayOutputStream.write(data,0,count);
                    int progress = (Integer)total*100/file_length;
                    publishProgress(progress);

                }
                inputStream.close();
                //outputStream.close();
                byteArrayOutputStream.close();

                image_byte = byteArrayOutputStream.toByteArray();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Download Complete...";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

            progressDialog.setProgress(values[0]);



        }


        @Override
        protected void onPostExecute(String result) {

            if(type.equals("profile")) {
                globalv.setProfile_img(image_byte);
                communicator_caching.Communicator_Caching(globalv.getId(),"profile",image_byte);
               // db_sqlite.updateimages(globalv.getId(),"profile",image_byte);
            }else if(type.equals("cover")){
                globalv.setCover_img(image_byte);
               // db_sqlite.updateimages(globalv.getId(),"cover",image_byte);
                communicator_caching.Communicator_Caching(globalv.getId(),"cover",image_byte);
                progressDialog.hide();
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Begining.this, home.class));
                Toast.makeText(getApplicationContext(), "welcome " + globalv.getUser_name(), Toast.LENGTH_SHORT).show();
                finish();

            }

            // String path = "sdcard/photoalbum/downloaded_image.jpg";
          /*  Bitmap bitmap = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
            imageView.setImageBitmap(bitmap);
            imageView.setImageDrawable(Drawable.createFromPath(path));*/
       // }
    //}


}
