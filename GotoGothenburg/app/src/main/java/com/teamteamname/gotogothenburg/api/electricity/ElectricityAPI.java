package com.teamteamname.gotogothenburg.api.electricity;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityGPSHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityNextStopHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityStopButtonHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityTempHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityWifiHandler;
import com.teamteamname.gotogothenburg.api.electricity.parsers.AmbientTempParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.CabinTempParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.GPSCoordParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.NextStopParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.StopPressedParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.WifiUsersParser;
import com.teamteamname.gotogothenburg.map.Bus;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

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

}