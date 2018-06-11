package com.example.miestro.google_maps;

import android.app.Fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class home extends AppCompatActivity  implements MyDialog.Communicator,Change_settings{

    ImageView imageView_profile,imageView_cover;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    Boolean logout=false;
    Globalv globalv;
    TextView name_header;
    Fragment myfragment = null;
    Class fragmentclass = null;
    FragmentManager fragmentManager;
    DB_Sqlite db_sqlite;
    private static final String profile_url="http://192.168.1.4:800/khedma/";
    private static final String cover_url="http://192.168.1.4:800/khedma/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fragment myfragment = null;
        Class fragmentclass = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        globalv=(Globalv)getApplicationContext();
        db_sqlite = new DB_Sqlite(this);
        mDrawer = (DrawerLayout)findViewById(R.id.DrawerLayout);

        mToggle = new ActionBarDrawerToggle(this,mDrawer,R.string.Open,R.string.Close);
        NavigationView navigationView =(NavigationView)findViewById(R.id.navigationview);
        setupDrawercontent(navigationView);
        imageView_profile = (ImageView)findViewById(R.id.image_header);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View hView =  navigationView.getHeaderView(0);

        name_header = (TextView) hView.findViewById(R.id.textView);
        imageView_profile = (ImageView) hView.findViewById(R.id.image_header);
        imageView_cover = (ImageView) hView.findViewById(R.id.cover_img);


      //  Toast.makeText(this,profile_url+globalv.getProfile_url(),Toast.LENGTH_LONG).show();
        if(globalv.getProfile_url() !=null) {
          //  db_sqlite.updateimages(globalv.getId(),"profile",globalv.getProfile_img());
          /*  Bitmap profile_bitmap = BitmapFactory.decodeByteArray(globalv.getProfile_img(), 0, globalv.getProfile_img().length);
            imageView_profile.setImageBitmap(profile_bitmap);
*/
            Picasso.with(this).load(profile_url+globalv.getProfile_url()).into(imageView_profile);

        }

        if(globalv.getCover_url() !=null) {
            //db_sqlite.updateimages(globalv.getId(),"cover",globalv.getCover_img());
           /* Bitmap cover_bitmap = BitmapFactory.decodeByteArray(globalv.getCover_img(), 0, globalv.getCover_img().length);
            imageView_cover.setImageBitmap(cover_bitmap);
          */
            Picasso.with(home.this).load(cover_url+globalv.getCover_url()).into(imageView_cover);


        }


        name_header.setText(globalv.getUser_name());

        fragmentclass = Fragment_home.class;
        try {
            myfragment = (Fragment) fragmentclass.newInstance();
        }catch (Exception e){

        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment,myfragment).commit();
        setTitle("الرئيسية");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectItemDrawer(MenuItem menuItem){

        switch(menuItem.getItemId()){
            case R.id.home:
                fragmentclass = Fragment_home.class;
                break;
            case R.id.myservices:
                fragmentclass = Fragment_myservices.class;
                break;
            case R.id.chats:
                fragmentclass = Fragment_chats.class;
                break;
            case R.id.wallet:
                fragmentclass = Fragment_wallet.class;
                break;
            case R.id.settings:
                fragmentclass = Fragment_settings.class;
                break;
            case R.id.callus:
                fragmentclass = Fragment_callus.class;
                break;
            case R.id.logout:
               // fragmentclass = Fragment_logout.class;
                logout = true;
                break;
        }

        if(!logout) {
            try {
                myfragment = (Fragment) fragmentclass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }


            // imageView.setImageResource(R.drawable.plumber);

            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, myfragment).commit();
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
            mDrawer.closeDrawers();

        }else{

            FragmentManager manager = getFragmentManager();
            MyDialog myDialog = new MyDialog();
            myDialog.show(manager,"Mydialog");
        }
    }

    public void fragment_transaction(Fragment fragment){

    }

    private void setupDrawercontent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }

    @Override
    public void onDialogMessage(String message) {
        DB_Sqlite db_sqlite =  new DB_Sqlite(this);
        if(message.equals("yes")){

            Integer result =  db_sqlite.Delete(globalv.getId());
            if(result>0) {

                globalv.setId(null);
                globalv.setUser_name(null);
                globalv.setProfile_img(null);
                globalv.setCover_img(null);
                startActivity(new Intent(home.this, Begining.class));
                Toast.makeText(this,"تم تسجيل الخروج",Toast.LENGTH_SHORT).show();
                finish();
            }
        }else if(message.equals("no")){
            //Toast.makeText(this,"no",Toast.LENGTH_SHORT).show();
            logout = false;
        }
        mDrawer.closeDrawers();
    }

    @Override
    public void Change_settings(String type, Bitmap image ,String img_url) {
        //byte[] img = convert_to_byte(image);

        if(type.equals("profile")){

            db_sqlite.updateData(globalv.getId(),"profile",img_url);

            //globalv.setProfile_img(img);
            globalv.setProfile_url(img_url);
          //  imageView_profile.setImageBitmap(image);

            Picasso.with(this).load(profile_url+globalv.getProfile_url()).into(imageView_profile);

        }else if(type.equals("cover")){

            db_sqlite.updateData(globalv.getId(),"cover",img_url);
           // globalv.setCover_img(img);
            globalv.setCover_url(img_url);
          //  imageView_cover.setImageBitmap(image);
            Picasso.with(this).load(cover_url+globalv.getCover_url()).into(imageView_cover);
            Toast.makeText(this,"تم الحفظ بنجاح",Toast.LENGTH_SHORT).show();


            fragmentclass = Fragment_home.class;
            try {
                myfragment = (Fragment) fragmentclass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction().replace(R.id.fragment, myfragment).commit();

            setTitle("الرئيسة");
            mDrawer.closeDrawers();

        }

    }
    private byte[] convert_to_byte(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();

        return imgbytes;

    }
}
