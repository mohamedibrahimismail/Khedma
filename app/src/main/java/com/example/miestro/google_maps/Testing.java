package com.example.miestro.google_maps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class Testing extends AppCompatActivity {

    ImageView cover, profile;
    Button change_profile, change_cover;
    private final int Img_request = 1;
    private Bitmap bitmap;
    private static final int PERMISSION_REQUEST_CODE=1000;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();

                }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },PERMISSION_REQUEST_CODE);
        }
        cover = (ImageView) findViewById(R.id.cover_img);
        profile = (ImageView) findViewById(R.id.profile_img);
        change_profile = (Button) findViewById(R.id.btn_profile_img);
        change_cover = (Button) findViewById(R.id.btn_cover_img);

        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //profile.setImageResource(R.drawable.profil_pic);
               // selectImage();

                if(ActivityCompat.checkSelfPermission(Testing.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },PERMISSION_REQUEST_CODE);
                    return;
                }else{

                    AlertDialog dialog = new SpotsDialog(Testing.this);
                    dialog.show();
                    dialog.setMessage("Downloading....");

                    String filname = UUID.randomUUID().toString()+".jpg";

                    Picasso.with(getBaseContext())
                            .load("http://www.gstatic.com/tv/thumb/persons/33623/33623_v9_bc.jpg")
                            .into(new SaveImagehelper(getBaseContext(),
                                    dialog,
                                    getApplicationContext().getContentResolver(),
                                    filname,
                                    "Image description"));

                }

            }
        });

        change_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // cover.setImageResource(R.drawable.cover_img);
            }
        });


    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Img_request);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Img_request && resultCode == RESULT_OK && data != null) {
            Toast.makeText(this, "get path", Toast.LENGTH_LONG).show();
            Uri path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                profile.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }





}

