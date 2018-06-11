package com.example.miestro.google_maps;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MIESTRO on 16/08/2017.
 */

public class Send_Data_Register extends StringRequest {
    private static final String url="http://192.168.1.4:800/khedma/register.php";
    private Map<String,String> Mapdata;

    public Send_Data_Register(String name, String email, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, url, listener,null);
        Mapdata = new HashMap<>();
        Mapdata.put("name",name);
        Mapdata.put("email",email);
        Mapdata.put("password",password);
    }

    @Override
    public Map<String,String> getParams(){return Mapdata;}

}