package com.example.miestro.google_maps;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;


public class Fragment_settings extends Fragment{
    ImageView cover,profile;
    Change_settings change_settings;
    Button change_profile,change_cover,save_changes;
    private final int Img_profile_request = 1;
    private final int Img_cover_request = 2;
    private Bitmap profile_bitmap,cover_bitmap;
    private static final String uploadurl="http://192.168.1.4:800/khedma/uploade_images.php";
    private static final String profile_url="http://192.168.1.4:800/khedma/";
    private static final String cover_url="http://192.168.1.4:800/khedma/";
    Globalv globalv;
    ProgressBar progressBar;
    DB_Sqlite db_sqlite;

    public Fragment_settings() {
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
        return inflater.inflate(R.layout.fragment_sittings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        globalv=(Globalv)getActivity().getApplicationContext();
        db_sqlite = new DB_Sqlite(getContext());
        change_settings = (Change_settings)getActivity();
        cover = (ImageView)getActivity().findViewById(R.id.cover_img);
        profile = (ImageView)getActivity().findViewById(R.id.profile_img);
        change_profile = (Button)getActivity().findViewById(R.id.btn_profile_img);
        change_cover = (Button)getActivity().findViewById(R.id.btn_cover_img);
        save_changes = (Button)getActivity().findViewById(R.id.save_changes);
        progressBar = (ProgressBar)getActivity().findViewById(R.id.progressbar);


        if(globalv.getProfile_url() !=null) {
/*
           profile_bitmap = BitmapFactory.decodeByteArray(globalv.getProfile_img(), 0, globalv.getProfile_img().length);
           profile.setImageBitmap(profile_bitmap);
*/
            Picasso.with(getContext()).load(profile_url+globalv.getProfile_url()).into(profile);

       }
        if(globalv.getCover_url() !=null){

         /*  cover_bitmap = BitmapFactory.decodeByteArray(globalv.getCover_img(), 0, globalv.getCover_img().length);
            cover.setImageBitmap(cover_bitmap);
*/
            Picasso.with(getContext()).load(cover_url+globalv.getCover_url()).into(cover);

        }

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_changes.setClickable(false);
                save_changes.setText("الرجاء الانتظار...");
                //progressBar.setVisibility(View.VISIBLE);
                if(profile_bitmap==null){
                  Toast.makeText(getContext(),"من فضلك اختر صورة شخصية",Toast.LENGTH_SHORT).show();

                }else {
                    uploadeImage(profile_bitmap, "profile");
                }

                if(cover_bitmap==null){
                    Toast.makeText(getContext(),"من فضلك اختر صورة غلاف",Toast.LENGTH_SHORT).show();
                    save_changes.setClickable(true);
                    save_changes.setText("حفظ");
                }else{

                 uploadeImage(cover_bitmap,"cover");
                }


            }
        });

        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(Img_profile_request);
            }
        });


        change_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(Img_cover_request);
            }
        });


    }

    private void selectImage(int request){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent,request);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==Img_profile_request && resultCode==RESULT_OK && data !=null ){
        // Get selected gallery image
        Uri selectedPicture = data.getData();
        // Get and resize profile image
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedPicture, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

        ExifInterface exif = null;
        try {
            File pictureFile = new File(picturePath);
            exif = new ExifInterface(pictureFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ExifInterface.ORIENTATION_NORMAL;

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                loadedBitmap = rotateBitmap(loadedBitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                loadedBitmap = rotateBitmap(loadedBitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                loadedBitmap = rotateBitmap(loadedBitmap, 270);
                break;
        }


        profile.setImageBitmap(loadedBitmap);
            profile_bitmap = loadedBitmap;

        }else if(requestCode ==Img_cover_request && resultCode==RESULT_OK && data !=null){

            // Get selected gallery image
            Uri selectedPicture = data.getData();
            // Get and resize profile image
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedPicture, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

            ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    break;
            }


            cover.setImageBitmap(loadedBitmap);
            cover_bitmap = loadedBitmap;

        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }






    private  void uploadeImage(final Bitmap bitmap ,final String type){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    if(Response.equals("Image uploaded Successfully")){
                        if(type.equals("profile"))
                        {
                              // byte[] img = convert_to_byte(profile_bitmap);

                               // db_sqlite.updateData(globalv.getId(),"profile",img);
                             //   globalv.setProfile_img(img);
                               // Toast.makeText(getContext(),"تم حفظ صورة الشخصية",Toast.LENGTH_SHORT).show();
                                change_settings.Change_settings("profile",profile_bitmap,"user_profile_potos/"+globalv.getId()+".jpg");
                                save_changes.setClickable(true);
                                save_changes.setText("حفظ");


                        }

                        else {
                          //  byte[] img = convert_to_byte(cover_bitmap);


                                  //  db_sqlite.updateData(globalv.getId(),"cover",img);
                                   // globalv.setCover_img(img);
                                 //   Toast.makeText(getContext(),"تم حفظ صورة الغلاف",Toast.LENGTH_SHORT).show();
                                    change_settings.Change_settings("cover",cover_bitmap,"user_cover_potos/"+globalv.getId()+".jpg");
                                    save_changes.setClickable(true);
                                    save_changes.setText("حفظ");

                        }

                    }else {
                        Toast.makeText(getContext(),"فشل في الحفط",Toast.LENGTH_SHORT).show();
                        save_changes.setClickable(true);
                        save_changes.setText("حفظ");

                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> parames = new HashMap<>();
                parames.put("id",globalv.getId());
                parames.put("image",image_to_string(bitmap));
                parames.put("type",type);

                return parames;

            }
        };


        Mysingleton.getmInstatnce(getContext()).addToRequestque(stringRequest);





    }

    private String image_to_string(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes,Base64.DEFAULT);


    }

    private byte[] convert_to_byte(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();

        return imgbytes;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }




}
