package com.teamteamname.gotogothenburg.api.electricity.mock;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NoCache;
import com.teamteamname.gotogothenburg.api.electricity.mock.MockNetwork;

import org.json.JSONArray;

import java.util.Map;

import lombok.Getter;

/**
 * A mockclass of a RequestQueue that catches added requests aswell as sending mockresponses
 * to be parsed. Used to test requests and responses.
 */
public class MockRequestQueue extends RequestQueue {

    @Getter private String uri;
    @Getter private Map<String,String> header;
    @Getter private Response.ErrorListener errorListener;
    @Getter private Response.Listener<JSONArray> parser;
    @Getter private int method;


    public MockRequestQueue(){
        super(new NoCache(), new MockNetwork());
    }


    /**
     * Catches a request and reads it's variables. To be used to test whether the request was correctly created.
     * Overrides the add method of a traditional RequestQueue.
     * @param request
     * @param <T>
     * @return
     */
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
