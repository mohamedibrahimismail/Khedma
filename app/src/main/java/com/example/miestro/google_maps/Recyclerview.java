package com.example.miestro.google_maps;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.miestro.google_maps.Adapter.SimpleAdapter;

public class Recyclerview extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


       
    }

    private void runAnimation(RecyclerView recyclerView, int type) {

        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;
        if(type ==0) { //fall down Animation

            controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_down);

        }else if(type==1){

            controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_slide_from_bottom);
        }else if(type==2) {

            controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_slide_from_right);
        }
            SimpleAdapter adapter = new SimpleAdapter();
            recyclerView.setAdapter(adapter);

            //set Anim
            recyclerView.setLayoutAnimation(controller);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();


    }

    public void falldown(View view) {

        runAnimation(recyclerView,0);

    }

    public void sidebottom(View view) {
        runAnimation(recyclerView,1);
    }

    public void SideRight(View view) {
        runAnimation(recyclerView,2);
    }
}
