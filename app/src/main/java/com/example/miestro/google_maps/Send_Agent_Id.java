package com.example.miestro.google_maps;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MIESTRO on 22/04/2018.
 */

public class Send_Agent_Id extends StringRequest {
    private static final String url="http://192.168.1.4:800/khedma/get_agent_info.php";
    private Map<String,String> Mapdata;

    public Send_Agent_Id(String id, Response.Listener<String> listener){
        super(Method.POST, url, listener,null);
        Mapdata = new HashMap<>();
        Mapdata.put("id",id);
    }

    @Override
    protected Map<String, String> getParams()  {
        return Mapdata;
    }
}
