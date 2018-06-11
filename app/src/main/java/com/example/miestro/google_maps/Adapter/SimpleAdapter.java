package com.example.miestro.google_maps.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miestro.google_maps.MapActivity;
import com.example.miestro.google_maps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MIESTRO on 28/05/2018.
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>{

    List<Integer> logo;
   static String[] jobs={"نجار","سباك","سواق","حداد","كهربائي","ميكانيكي","مشاويري"};

    public SimpleAdapter(){
        logo = new ArrayList<>();

        logo.add(R.drawable.carpenter);
        logo.add(R.drawable.plumber);
        logo.add(R.drawable.driver);
        logo.add(R.drawable.smith);
        logo.add(R.drawable.electrician);
        logo.add(R.drawable.mechanic);
        logo.add(R.drawable.man);

      /*  dataSource = new ArrayList<>();
        for (int i=0;i<10;i++){

            dataSource.add(i);

        }
*/
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ltem_layout,parent,false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        holder.imageView.setImageResource(logo.get(position));
        holder.textView.setText(jobs[position]);


    }

    @Override
    public int getItemCount() {
        return jobs.length;
    }

    public static  class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView ;
        public ImageView imageView;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            Typeface tf_regular = Typeface.createFromAsset(itemView.getContext().getAssets(), "FrutigerLTArabic-45Light.ttf");
            textView = (TextView)itemView.findViewById(R.id.job_item);
            this.textView.setTypeface(tf_regular);
            imageView = (ImageView)itemView.findViewById(R.id.job_image);



        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            //Toast.makeText(itemView.getContext(),jobs[position],Toast.LENGTH_SHORT).show();
            Intent intent =  new Intent(itemView.getContext(),MapActivity.class);
            intent.putExtra("job",jobs[position]);
            itemView.getContext().startActivity(intent);


        }
    }

}