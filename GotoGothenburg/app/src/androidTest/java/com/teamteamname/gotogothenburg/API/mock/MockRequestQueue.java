package com.teamteamname.gotogothenburg.api.mock;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NoCache;

import org.json.JSONArray;

import java.util.Map;

import lombok.Getter;

/**
 * Created by Olof on 29/09/2015.
 */
public class MockRequestQueue extends RequestQueue {

    @Getter private String uri;
    @Getter private Map<String,String> header;
    @Getter private Response.ErrorListener errorListener;
    @Getter private Response.Listener<JSONArray> parser;
    @Getter private int method;


    public MockRequestQueue(){
        super(new NoCache(), new com.teamteamname.gotogothenburg.api.mock.MockNetwork());
    }

    public void reset(){
        uri = null;
        header = null;
        errorListener = null;
        parser = null;
        method = 0;
    }

    // Used to check if the request is produced in the right way.
    // Also calls the
    @Override
    public <T> Request<T> add(Request<T> request){
        JsonArrayRequest jsonArrayRequest = (JsonArrayRequest)request;
        uri = jsonArrayRequest.getUrl();
        try {
            header = jsonArrayRequest.getHeaders();
        }catch(AuthFailureError e){
            Log.e("AuthFail","Cant get header from request.");
        }
        method = jsonArrayRequest.getMethod();
        errorListener = jsonArrayRequest.getErrorListener();
        parser = (Response.Listener)jsonArrayRequest.getErrorListener();

        return request;
    }

}
