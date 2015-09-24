package com.teamteamname.gotogothenburg.API;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Olof on 21/09/2015.
 */
public class APIHandler implements IAPIHandler {

    private static APIHandler instance;

    private RequestQueue queue;

    private String username = "grp27";
    private String password = "BCcRl-8UlI";

    private String baseUrl = "https://ece01.ericsson.net:4443/ecity";
    //private Uri.Builder ub;
    private String dgw = "dgw=";
    private String sensorSpec = "sensorSpec=";
    private String resourceSpec = "resourceSpec=";
    private String t1 = "t1=";
    private String t2 = "t2=";

    private IDeviceAPI deviceAPI;

    private APIHandler(Context context){

        // Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1 MB cap (maybe need larger)

        // Set up the network to use HttpURLConnection as the HTTP client.
        com.android.volley.Network network = new BasicNetwork(new HurlStack());

        queue = new RequestQueue(cache,network);

        queue.start();

        //Setup the DeviceAPI
        deviceAPI = new AndroidDeviceAPI(context);
        /*
        ub.scheme("https");
        ub.authority(baseUrl);
        ub.appendPath("/ecity");
        */

    }

    public static IAPIHandler getInstance(Context context){
        if(instance == null){
            instance = new APIHandler(context);
        }
        return instance;
    }

    @Override
    public void getGPSPosition(GPSCoordReceiver callback, String dgw) {
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
        Log.i("URI",uri);

        GPSListener l = new GPSListener(callback);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, uri, l, l) {

            // Changes the header of the http request to our Basic Auth.
            // Might be possible with the toolbox Authentication instead
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                return createBasicAuth(username,password);
            }
        };


        queue.add(request);
    }

    private Map<String,String> createBasicAuth(String username, String password){
        Map<String,String> headerMap = new HashMap<String,String>();

        String credentials = username + ":" + password;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Log.i("Auth", "Authorization: Basic " + credentials);
        headerMap.put("Authorization", "Basic " + base64EncodedCredentials);

        return headerMap;
    }

    // Should switch HTTP parmeters
    private String createURI(){
        return null;
    }

    // I use the following listener-classes to parse the information we get from the server
    // in JSON to the appropriate format for our application.
    // The listener also handles if an error is returned.

    private class GPSListener implements Response.Listener<JSONArray>, Response.ErrorListener{

        GPSCoordReceiver callback;

        public GPSListener(GPSCoordReceiver callback){
            this.callback = callback;
        }

        @Override
        public void onResponse(JSONArray response) {
            //TODO: Parse the JSONArray into a LatLng
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
                callback.sendGPSCoord(new LatLng(lat.getDouble("value"), lng.getDouble("value")));
            }catch(JSONException e){
                Log.e("JSONException","Error getting values from JSONObject-response");
            }catch(NullPointerException e){
                Log.e("NullPointer","Error reading JSONObjects");
            }


        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ErrorResponse",error.toString());
            Log.e("ErrorResponse",error.getCause().toString());
            callback.sendGPSCoord(new LatLng(0.0,0.0));
        }
    }

    //Device com.teamteamname.gotogothenburg.API implementations.
    @Override
    public String getWiFiRouterMAC() {
        return deviceAPI.getWiFiRouterMAC();
    }

    @Override
    public void playSound(File sound) {
        deviceAPI.playSound(sound);
    }
}


