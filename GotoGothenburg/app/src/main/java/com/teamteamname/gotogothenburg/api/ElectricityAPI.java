package com.teamteamname.gotogothenburg.api;


import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.teamteamname.gotogothenburg.GPSCoord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olof on 25/09/2015.
 */
public class ElectricityAPI implements IElectricityAPI{

    private RequestQueue queue;

    private String username = "grp27";
    private String password = "BCcRl-8UlI";

    private String baseUrl = "https://ece01.ericsson.net:4443/ecity";
    private String dgw = "dgw=";
    private String sensorSpec = "sensorSpec=";
    private String resourceSpec = "resourceSpec=";
    private String t1 = "t1=";
    private String t2 = "t2=";

    // In cases we have a common queue
    public ElectricityAPI(RequestQueue queue){
        this.queue = queue;
    }

    // In case every API has it's own queue
    public ElectricityAPI(Context context){
        Cache cache = new DiskBasedCache(context.getCacheDir(),1024*1024);

        Network network = new BasicNetwork(new HurlStack());

        queue = new RequestQueue(cache,network);

        queue.start();
    }

    public void getBusLocation(String dgw, ElectricityHandler callback){
        //Requests data since 5 sec earlier.
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000 * 5);

        // Builds the URI
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append("?");
        sb.append(this.dgw);
        sb.append(dgw);
        sb.append("&");
        sb.append(sensorSpec);
        sb.append("Ericsson$GPS");
        sb.append("&");
        sb.append(this.t1);
        sb.append(t1);
        sb.append("&");
        sb.append(this.t2);
        sb.append(t2);
        String uri = sb.toString();

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        GPSCoordParser parser = new GPSCoordParser(callback);

        GetRequestWithBAuth request = new GetRequestWithBAuth(uri,parser,parser);
        queue.add(request);

    }

    private String buildURI(){
        return null;
    }

    private Map<String,String> createBasicAuth(String username, String password){
        Map<String,String> headerMap = new HashMap<String,String>();

        String credentials = username + ":" + password;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Log.i("Auth", "Authorization: Basic " + credentials);
        headerMap.put("Authorization", "Basic " + base64EncodedCredentials);

        return headerMap;
    }

    // A JSONArray-GET-request with our Basic Authorization as header.
    private class GetRequestWithBAuth extends JsonArrayRequest{

        public GetRequestWithBAuth(String uri, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
            super(Method.GET,uri,listener,errorListener);
        }

        @Override
        public Map<String,String> getHeaders() throws AuthFailureError{
            return createBasicAuth(username,password);
        }
    }

    // The following listeners parses the JSONArray response from the api into the appropriate format.
    // They then pass it to the callback object

    // Parses a response into a GPSCoord.
    private class GPSCoordParser implements Response.Listener<JSONArray>, Response.ErrorListener{

        private ElectricityHandler callback;

        public GPSCoordParser(ElectricityHandler callback){
            this.callback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ErrorResponse",error.toString());
            callback.electricityRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {
            Log.i("Response",response.toString());
            JSONObject lat = null;
            JSONObject lng = null;
            try {
                lat = response.getJSONObject(1);
                lng = response.getJSONObject(3);
            }catch(JSONException e){
                Log.e("JSONException","Error getting JSONObjects from response");
            }

            try {
                callback.electricityRequestDone(new GPSCoord((float)lat.getDouble("value"), (float)lng.getDouble("value")));
            }catch(JSONException e){
                Log.e("JSONException","Error getting values from JSONObject-response");
            }catch(NullPointerException e){
                Log.e("NullPointer","Error reading JSONObjects");
            }
        }
    }

}
