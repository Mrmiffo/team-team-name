package com.teamteamname.gotogothenburg.api;


import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.teamteamname.gotogothenburg.GPSCoord;
import com.teamteamname.gotogothenburg.map.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Olof on 25/09/2015.
 */
public class ElectricityAPI implements IElectricityAPI{

    private RequestQueue queue;
    private static ElectricityAPI instance;

    private String username = "grp27";
    private String password = "BCcRl-8UlI";

    private ElectricityAPI(RequestQueue queue){
        this.queue = queue;
    }

    public static void init(Context context, RequestQueue queue){
        if(instance == null){
            instance = new ElectricityAPI(queue);

            //TODO: Fetch api username and password from values.strings
        }
    }

    public static ElectricityAPI getInstance(){
        return instance;
    }

    public void getBusLocation(Bus bus, ElectricityHandler callback){
        //Requests data since 5 sec earlier.
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000 * 5);

        // Builds the URI
        String uri = buildURI(bus.getDgw(),"Ericsson$GPS2",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        GPSCoordParser parser = new GPSCoordParser(callback);

        RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);

    }

    @Override
    public void getNextStop(Bus bus, ElectricityHandler callback) {
        //Requests data since 10 sec earlier.
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000 * 10);

        // Builds the URI
        String uri = buildURI(bus.getDgw(),"Ericsson$Next_Stop",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        NextStopParser parser = new NextStopParser(callback);

        RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    @Override
    public void getAmbientTemperature(Bus bus, ElectricityHandler callback) {
        //Requests data since 10 sec earlier.
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000 * 10);

        // Builds the URI
        String uri = buildURI(bus.getDgw(),"Ericsson$Ambient_Temperature",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        AmbientTempParser parser = new AmbientTempParser(callback);

        RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    @Override
    public void getCabinTemperature(Bus bus, ElectricityHandler callback) {
        //Requests data since 2,5 min earlier.
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000 * 150);

        // Builds the URI
        String uri = buildURI(bus.getDgw(),"Ericsson$Driver_Cabin_Temperature",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        CabinTempParser parser = new CabinTempParser(callback);

        RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    @Override
    public void getStopPressed(Bus bus, ElectricityHandler callback) {
        //Requests data since 2,5 min earlier.
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000 * 150);

        // Builds the URI
        String uri = buildURI(bus.getDgw(),"Ericsson$Stop_Pressed",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        StopPressedParser parser = new StopPressedParser(callback);

        RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    @Override
    public void getNbrOfWifiUsers(Bus bus, ElectricityHandler callback) {
        //Requests data since 12 sec earlier.
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000 * 12);

        // Builds the URI
        String uri = buildURI(bus.getDgw(),"Ericsson$Online_Users",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        WifiUsersParser parser = new WifiUsersParser(callback);

        RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    // Help methods:

    // sensorSpec and resourceSpec can't be given at the same time.
    private String buildURI(String dgw, String sensorSpec, String resourceSpec, long t1, long t2){
        StringBuilder sb = new StringBuilder();

        sb.append("https://ece01.ericsson.net:4443/ecity?");
        if(dgw != null){
            sb.append("dgw=");
            sb.append(dgw);
            sb.append("&");
        }
        if(sensorSpec != null){
            sb.append("sensorSpec=");
            sb.append(sensorSpec);
            sb.append("&");
        }else if(resourceSpec != null){
            sb.append("resourceSpec=");
            sb.append(resourceSpec);
            sb.append("&");
        }
        sb.append("t1=");
        sb.append(t1);
        sb.append("&");
        sb.append("t2=");
        sb.append(t2);

        return sb.toString();
    }

    // Returns the JSONObject with latest value (according to the timestamps) for a specific resource out of a array which could contain other resources.
    private JSONObject getLatestJSONValue(String resourceName, JSONArray array){

        PriorityQueue<JSONObject> objectsWithResourceName = new PriorityQueue<JSONObject>(2,new Comparator<JSONObject>(){

            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                try {
                    if (lhs.getInt("timestamp") > rhs.getInt("timestamp")) {
                        return -1;
                    } else if (lhs.getInt("timestamp") < rhs.getInt("timestamp")) {
                        return 1;
                    } else {
                        return 0;
                    }
                }catch (JSONException e){
                    Log.e("ParsingError","No timestamp in Object");
                }
                return 0;
            }
        });
        try{
            for(int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                if(object.has(resourceName)){
                    objectsWithResourceName.add(object);
                }

            }

            return objectsWithResourceName.poll();

        }catch(JSONException e){
            Log.e("ParsingError", "Not able to find resource in array.");
        }

        //Should only return if there was an parsing error.
        return new JSONObject();
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
    private class RequestWithBAuth extends JsonArrayRequest{

        public RequestWithBAuth(String uri, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
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
            Log.e("GPSCoordParser",error.toString());
            callback.electricityRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {
            Log.i("Response",response.toString());

            JSONObject lat = getLatestJSONValue("Latitude2_Value",response);
            JSONObject lng = getLatestJSONValue("Longitude2_Value",response);

            try {
                callback.electricityGPSResponse(new GPSCoord((float) lat.getDouble("value"), (float) lng.getDouble("value")));
            }catch(JSONException e){
                Log.e("JSONException","Error getting values from JSONObject-response");
            }catch(NullPointerException e){
                Log.e("NullPointer","Error reading JSONObjects");
            }
        }
    }

    private class NextStopParser implements Response.Listener<JSONArray>, Response.ErrorListener{

        ElectricityHandler callback;

        public NextStopParser(ElectricityHandler callback){
            this.callback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("NextStopParser",error.toString());
            callback.electricityRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {
            Log.i("Response",response.toString());

            JSONObject nextStop = getLatestJSONValue("Bus_Stop_Name_Value",response);

            try {
                callback.electricityNextStopResponse(nextStop.getString("value"));
            }catch(JSONException e){
                Log.e("JSONException","Error getting values from JSONObject-response");
            }catch(NullPointerException e){
                Log.e("NullPointer","Error reading JSONObjects");
            }
        }
    }

    private class AmbientTempParser implements Response.Listener<JSONArray>, Response.ErrorListener{

        ElectricityHandler callback;

        public AmbientTempParser(ElectricityHandler callback){ this.callback = callback; }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("AmbientTempParser", error.toString());
            callback.electricityRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {
            Log.i("Response",response.toString());

            JSONObject temperature = getLatestJSONValue("Ambient_Temperature_Value",response);

            try {
                callback.electricityAmbientTemperatureResponse(temperature.getDouble("value"));
            }catch(JSONException e){
                Log.e("JSONException","Error getting values from JSONObject-response");
            }catch(NullPointerException e){
                Log.e("NullPointer","Error reading JSONObjects");
            }
        }
    }

    private class CabinTempParser implements Response.Listener<JSONArray>, Response.ErrorListener{

        ElectricityHandler callback;

        public CabinTempParser(ElectricityHandler callback){ this.callback = callback; }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("CabinTempParser", error.toString());
            callback.electricityRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {
            Log.i("Response",response.toString());

            JSONObject temperature = getLatestJSONValue("Driver_Cabin_Temperature_Value",response);

            try {
                callback.electricityCabinTemperature(temperature.getDouble("value"));
            }catch(JSONException e){
                Log.e("JSONException","Error getting values from JSONObject-response");
            }catch(NullPointerException e){
                Log.e("NullPointer","Error reading JSONObjects");
            }
        }
    }

    private class StopPressedParser implements Response.Listener<JSONArray>, Response.ErrorListener{

        ElectricityHandler callback;

        public StopPressedParser(ElectricityHandler callback){ this.callback = callback; }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("StopPressedParser", error.toString());
            callback.electricityRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {
            Log.i("Response",response.toString());

            JSONObject stopPressed = getLatestJSONValue("Stop_Pressed_Value",response);

            try {
                callback.electricityStopPressedResponse(stopPressed.getBoolean("value"));
            }catch(JSONException e){
                Log.e("JSONException","Error getting values from JSONObject-response");
            }catch(NullPointerException e){
                Log.e("NullPointer","Error reading JSONObjects");
            }
        }
    }

    private class WifiUsersParser implements Response.Listener<JSONArray>, Response.ErrorListener{

        ElectricityHandler callback;

        public WifiUsersParser(ElectricityHandler callback){ this.callback = callback; }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("WifiUsersParser", error.toString());
            callback.electricityRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {
            Log.i("Response",response.toString());

            JSONObject wifiUsers = getLatestJSONValue("Total_Online_Users_Value",response);

            try {
                callback.electricityWifiUsersResponse(wifiUsers.getInt("value"));
            }catch(JSONException e){
                Log.e("JSONException","Error getting values from JSONObject-response");
            }catch(NullPointerException e){
                Log.e("NullPointer","Error reading JSONObjects");
            }
        }
    }

}
