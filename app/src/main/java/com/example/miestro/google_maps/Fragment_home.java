package com.example.miestro.google_maps;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.miestro.google_maps.Adapter.SimpleAdapter;


public class Fragment_home extends Fragment {
    RecyclerView recyclerView;

    public Fragment_home() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        runAnimation(recyclerView,2);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_home, container, false);
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
