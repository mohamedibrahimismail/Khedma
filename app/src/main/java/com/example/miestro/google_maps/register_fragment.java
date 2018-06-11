package com.example.miestro.google_maps;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MIESTRO on 19/02/2018.
 */

public class register_fragment extends Fragment implements View.OnClickListener  {
    boolean error = true;
    Register_Communicator register_communicator;
    Button button;
    EditText mname,memail,mpassword,mrepassword;
    ImageView error_name,error_email,error_password,error_repassword;
    DB_Sqlite db_sqlite;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mname = (EditText)getActivity().findViewById(R.id.name);
        memail = (EditText)getActivity().findViewById(R.id.email);
        mpassword= (EditText)getActivity().findViewById(R.id.pass1);
        mrepassword = (EditText)getActivity().findViewById(R.id.pass2);
        register_communicator = (Register_Communicator)getActivity();
        error_name = (ImageView)getActivity().findViewById(R.id.register_nameerror);
        error_email = (ImageView)getActivity().findViewById(R.id.register_emailerror);
        error_password = (ImageView)getActivity().findViewById(R.id.register_passworderror);
        error_repassword = (ImageView)getActivity().findViewById(R.id.register_repassworderror);
        button = (Button)getActivity().findViewById(R.id.registe_button);
        button.setOnClickListener(this);
        db_sqlite = new DB_Sqlite(getContext());

    }


    public boolean validation(){
        if (mname.getText().toString().equals("")){
            error_name.setVisibility(View.VISIBLE);
            error =false;
        }
        if (memail.getText().toString().equals("")){
            error_email.setVisibility(View.VISIBLE);
            error =false;
        }
        if (mpassword.getText().toString().equals("")){
            error_password.setVisibility(View.VISIBLE);
            error =false;
        }
        if (mrepassword.getText().toString().equals("")){
            error_repassword.setVisibility(View.VISIBLE);
            error =false;
        }

        return error;
    }


    public void setinvisible(){
        error_name.setVisibility(View.GONE);
        error_email.setVisibility(View.GONE);
        error_password.setVisibility(View.GONE);
        error_repassword.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        setinvisible();
        if( validation()) {

            final String name = mname.getText().toString().trim();
            String email = memail.getText().toString().trim();
            String pass1 = mpassword.getText().toString().trim();
            String pass2 = mrepassword.getText().toString().trim();


            if (!pass1.equals(pass2)) {

                Toast.makeText(getActivity(), getActivity().getString(R.string.registfrafment_incorrectPasswprd), Toast.LENGTH_LONG).show();
            } else {

                Response.Listener<String> responselisener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //  textView.setText(""+response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String result = jsonResponse.getString("register");
                            if (result.equals("successful registered")) {
                                String id = jsonResponse.getString("id");

                                Boolean insert_sqlite = db_sqlite.insertData(id,name);
                                if(insert_sqlite) {

                                register_communicator.Register_switcher(id,name);
                                Toast.makeText(getActivity(), getActivity().getString(R.string.successfulregistered), Toast.LENGTH_LONG).show();
                                }
                            } else if (result.equals("choose another email")) {
                                error_email.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), getActivity().getString(R.string.chooseanotheremail), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), getActivity().getString(R.string.failedregistered), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Send_Data_Register send_data = new Send_Data_Register(name, email, pass1, responselisener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(send_data);
            }
        }else{
            error = true;
            Toast.makeText(getActivity(), getActivity().getString(R.string.pleasefilltheinfo), Toast.LENGTH_LONG).show();

        }
    }
}

