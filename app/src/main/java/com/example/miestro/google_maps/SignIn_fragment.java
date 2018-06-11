package com.example.miestro.google_maps;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MIESTRO on 19/02/2018.
 */

public class SignIn_fragment extends Fragment implements View.OnClickListener {
    SignIn_Communicator signIn_communicator;
    Button login_button,register_button;
    EditText mname;
    EditText mpassword;
    DB_Sqlite db_sqlite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signin,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mname = (EditText)getActivity().findViewById(R.id.login_name);
        signIn_communicator =(SignIn_Communicator)getActivity();
        mpassword =(EditText)getActivity().findViewById(R.id.login_password);
        login_button = (Button)getActivity().findViewById(R.id.login_loginbtn);
        login_button.setOnClickListener(this);
        register_button =(Button)getActivity().findViewById(R.id.login_registbtn);
        register_button.setOnClickListener(this);

        db_sqlite = new DB_Sqlite(getContext());


    }

    @Override
    public void onClick(View v) {
         int id = v.getId();

         switch (id){
             case R.id.login_loginbtn:
                 login();
                 break;
             case R.id.login_registbtn:
                 signIn_communicator.SignIn_switcher("register","","","","");
                 break;
         }






    }

    public void login(){

        final String name=mname.getText().toString().trim();
        String password=mpassword.getText().toString().trim();


        Response.Listener<String> responselisener=new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                //  textView.setText(""+response);
                try {

                    JSONObject jsonResponse =new JSONObject(response);
                   // boolean success = jsonResponse.getBoolean("success");
                    int ID = jsonResponse.getInt("ID");
                    String profile_img_url = jsonResponse.getString("profile");
                    String cover_img_url = jsonResponse.getString("cover");
                    //Toast.makeText(getActivity(),jsonResponse.getString("profile") , Toast.LENGTH_SHORT).show();

                    if(ID > 0){

                        //Toast.makeText(getActivity(),"تم تسجيل الدخول",Toast.LENGTH_SHORT).show();
                        //linearLayout.setVisibility(View.INVISIBLE);
                       // globalv.setUser_name(name);
                        //startActivity(new Intent(getActivity(),home.class));

                        Boolean result = db_sqlite.insertData(ID+"",name);
                       if(result) {
                          // Toast.makeText(getActivity(),profile_img_url, Toast.LENGTH_SHORT).show();
                           Toast.makeText(getActivity(), "تم تسجيل الدخول", Toast.LENGTH_SHORT).show();
                           signIn_communicator.SignIn_switcher("signin", name, ID + "",profile_img_url,cover_img_url);
                       }


                    }else{
                        Toast.makeText(getActivity(),"البيانات غير صحيحة",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };

        Send_Data_Login send_data = new Send_Data_Login(name,password,responselisener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(send_data);


    }

    public void add_data_to_sqlite(){

    }


}
