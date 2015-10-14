package com.teamteamname.gotogothenburg.api.Electricity;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.teamteamname.gotogothenburg.GPSCoord;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.ElectricityGPSHandler;
import com.teamteamname.gotogothenburg.api.ElectricityNextStopHandler;
import com.teamteamname.gotogothenburg.api.ElectricityStopButtonHandler;
import com.teamteamname.gotogothenburg.api.ElectricityTempHandler;
import com.teamteamname.gotogothenburg.api.ElectricityWifiHandler;
import com.teamteamname.gotogothenburg.api.IElectricityAPI;
import com.teamteamname.gotogothenburg.route.Stops;
import com.teamteamname.gotogothenburg.map.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Olof on 25/09/2015.
 */
public class ElectricityAPI implements IElectricityAPI {

    private RequestQueue queue;
    private static IElectricityAPI instance;

    private static String apiKey;

    private ElectricityAPI(RequestQueue queue){
        this.queue = queue;
    }

    public static void init(Context context, RequestQueue queue){
        if(instance == null) {
            instance = new ElectricityAPI(queue);
            apiKey = context.getResources().getString(R.string.electricity_api_key);
        }
    }

    public static IElectricityAPI getInstance(){
        return instance;
    }

    @Override
    public void getBusLocation(Bus bus, ElectricityGPSHandler callback){
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
    public void getNextStop(Bus bus, ElectricityNextStopHandler callback) {
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
    public void getAmbientTemperature(Bus bus, ElectricityTempHandler callback) {
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
    public void getCabinTemperature(Bus bus, ElectricityTempHandler callback) {
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
    public void getStopPressed(Bus bus, ElectricityStopButtonHandler callback) {
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
    public void getNbrOfWifiUsers(Bus bus, ElectricityWifiHandler callback) {
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

    /**
     * Method only used in testing, to be able to initialize the RequestQueue for each test.
     */
    public static void resetState(){
        instance = null;
    }

    // Help methods:

    // sensorSpec and resourceSpec can't be given at the same time.
    private String buildURI(String dgw, String sensorSpec, String resourceSpec, long t1, long t2){
        StringBuilder sb = new StringBuilder(47);

        sb.append("https://ece01.ericsson.net:4443/ecity?");
        if(dgw != null){
            sb.append("dgw=").append(dgw).append('&');
        }
        if(sensorSpec != null){
            sb.append("sensorSpec=").append(sensorSpec).append('&');
        }else if(resourceSpec != null){
            sb.append("resourceSpec=").append(resourceSpec).append('&');
        }
        sb.append("t1=").append(t1).append("&t2=").append(t2);

        return sb.toString();
    }

    // Returns the JSONObject with latest value (according to the timestamps) for a specific resource out of a array which could contain other resources.
    private JSONObject getLatestJSONValue(String resourceName, JSONArray array){

        PriorityQueue<JSONObject> objectsWithResourceName = new PriorityQueue<JSONObject>(2,new Comparator<JSONObject>(){

            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                Log.i("ParseLatest",lhs.toString());
                Log.i("ParseLatest",rhs.toString());
                try {
                    return rhs.getInt("timestamp")-lhs.getInt("timestamp");
                }catch (JSONException e){
                    Log.e("ParsingError","No timestamp in Object");
                }
                return 0;
            }
        });
        try{
            for(int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                if(object.getString("resourceSpec").equals(resourceName)){
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

    private Map<String,String> createBasicAuth(String auth){
        Map<String,String> headerMap = new HashMap<String,String>();

        Log.i("Auth", "Authorization: Basic " + auth);
        headerMap.put("Authorization", "Basic " + auth);

        return headerMap;
    }

    // A JSONArray-GET-request with our Basic Authorization as header.
    private class RequestWithBAuth extends JsonArrayRequest{

        public RequestWithBAuth(String uri, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
            super(Method.GET,uri,listener,errorListener);
        }

        @Override
        public Map<String,String> getHeaders() throws AuthFailureError{
            return createBasicAuth(apiKey);
        }

    }

    // The following listeners parses the JSONArray response from the api into the appropriate format.
    // They then pass it to the callback object

    // Parses a response into a GPSCoord.
    private class GPSCoordParser implements Response.Listener<JSONArray>, Response.ErrorListener{

        private ElectricityGPSHandler callback;

        public GPSCoordParser(ElectricityGPSHandler callback){
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

        ElectricityNextStopHandler callback;

        public NextStopParser(ElectricityNextStopHandler callback){
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
            boolean stopExists = false;

            try {
                for(Stops stop:Stops.values()){
                    //Checks caps to match
                    if(nextStop.getString("value").toUpperCase().equals(stop.toString())){
                        callback.electricityNextStopResponse(stop);
                        stopExists = true;
                    }
                }
                if(!stopExists){
                    throw new IllegalArgumentException("No stop" + nextStop.getString("value") + "exists");
                }
            }catch(JSONException e){
                Log.e("JSONException","Error getting values from JSONObject-response");
            }catch(NullPointerException e){
                Log.e("NullPointer","Error reading JSONObjects");
            }
        }
    }

    private class AmbientTempParser implements Response.Listener<JSONArray>, Response.ErrorListener{

        ElectricityTempHandler callback;

        public AmbientTempParser(ElectricityTempHandler callback){ this.callback = callback; }

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

        ElectricityTempHandler callback;

        public CabinTempParser(ElectricityTempHandler callback){ this.callback = callback; }

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

        ElectricityStopButtonHandler callback;

        public StopPressedParser(ElectricityStopButtonHandler callback){ this.callback = callback; }

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

        ElectricityWifiHandler callback;

        public WifiUsersParser(ElectricityWifiHandler callback){ this.callback = callback; }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("WifiUsersParser", error.toString());
            callback.electricityRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {
            Log.i("Response", response.toString());

            JSONObject wifiUsers = getLatestJSONValue("Total_Online_Users_Value",response);
            callback.electricityWifiUsersResponse(30);
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
