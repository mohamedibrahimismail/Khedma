package com.example.miestro.google_maps;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class DownloadImage extends AppCompatActivity {

    String image_url = "https://timedotcom.files.wordpress.com/2014/10/455886166.jpg";
    ImageView imageView;
    byte[] image_byte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);
        imageView = (ImageView)findViewById(R.id.imageView);
    }

    public void DownLoadeImage(View view) {

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(image_url);
    }



    class DownloadTask extends AsyncTask<String,Integer,String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DownloadImage.this);
            progressDialog.setTitle("Download in progress .......");
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
                File new_folder = new File("sdcard/photoalbum");
                if(!new_folder.exists()){
                    new_folder.mkdir();
                }
                File input_file = new File(new_folder,"downloaded_image.jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                 OutputStream outputStream = new FileOutputStream(input_file);
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
                 outputStream.close();
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
            progressDialog.hide();
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
           // String path = "sdcard/photoalbum/downloaded_image.jpg";
            Bitmap bitmap = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
            imageView.setImageBitmap(bitmap);
            //imageView.setImageDrawable(Drawable.createFromPath(path));
        }
    }
}
