package com.teamteamname.gotogothenburg.api.electricity;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.IGetAmbientTemp;
import com.teamteamname.gotogothenburg.api.IGetBusLocation;
import com.teamteamname.gotogothenburg.api.IGetCabinTemp;
import com.teamteamname.gotogothenburg.api.IGetNbrOfWifiUsers;
import com.teamteamname.gotogothenburg.api.IGetNextStop;
import com.teamteamname.gotogothenburg.api.IGetStopPressed;
import com.teamteamname.gotogothenburg.api.GPSHandler;
import com.teamteamname.gotogothenburg.api.NextStopHandler;
import com.teamteamname.gotogothenburg.api.StopButtonHandler;
import com.teamteamname.gotogothenburg.api.TempHandler;
import com.teamteamname.gotogothenburg.api.WifiHandler;

import com.teamteamname.gotogothenburg.api.Bus;

import com.teamteamname.gotogothenburg.api.electricity.parsers.AmbientTempParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.CabinTempParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.GPSCoordParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.NextStopParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.StopPressedParser;
import com.teamteamname.gotogothenburg.api.electricity.parsers.WifiUsersParser;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olof on 25/09/2015.
 */
public class ElectricityAPI implements IGetBusLocation,IGetNextStop,IGetAmbientTemp,IGetCabinTemp,IGetStopPressed,IGetNbrOfWifiUsers {

<<<<<<< HEAD
    private RequestQueue queue;
=======
    private final RequestQueue queue;
    private static ElectricityAPI instance;
>>>>>>> PMD fixes
    //The query time is set to 60sec as this seems to be about the time it takes for the bus to update this value. Still sometimes no value exist.
    private final int QUERY_LENGTH = 60;

    private String apiKey;

    public ElectricityAPI(Context context, RequestQueue queue){
        this.queue = queue;
        apiKey = context.getResources().getString(R.string.electricity_api_key);
    }

    @Override
    public void getBusLocation(Bus bus, GPSHandler callback){
        //Requests data since 5 sec earlier.
        final long t2 = System.currentTimeMillis();
        final long t1 = t2 - (1000 * QUERY_LENGTH);

        // Builds the URI
        final String uri = buildURI(bus.getDgw(),"Ericsson$GPS2",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        final GPSCoordParser parser = new GPSCoordParser(callback);

        final RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);

    }

    @Override
    public void getNextStop(Bus bus, NextStopHandler callback) {
        //Requests data since 10 sec earlier.
        final long t2 = System.currentTimeMillis();
        final long t1 = t2 - (1000 * QUERY_LENGTH);

        // Builds the URI
        final String uri = buildURI(bus.getDgw(),"Ericsson$Next_Stop",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        final NextStopParser parser = new NextStopParser(callback);

        final RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    @Override
    public void getAmbientTemperature(Bus bus, TempHandler callback) {
        //Requests data since 10 sec earlier.
        final long t2 = System.currentTimeMillis();
        final long t1 = t2 - (1000 * QUERY_LENGTH);

        // Builds the URI
        final String uri = buildURI(bus.getDgw(),"Ericsson$Ambient_Temperature",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        final AmbientTempParser parser = new AmbientTempParser(callback);

        final RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    @Override
    public void getCabinTemperature(Bus bus, TempHandler callback) {
        //Requests data since 2,5 min earlier.
        final long t2 = System.currentTimeMillis();
        final long t1 = t2 - (1000 * 150);

        // Builds the URI
        final String uri = buildURI(bus.getDgw(),"Ericsson$Driver_Cabin_Temperature",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        final CabinTempParser parser = new CabinTempParser(callback);

        final RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    @Override
    public void getStopPressed(Bus bus, StopButtonHandler callback) {
        //Requests data since 2,5 min earlier.
        final long t2 = System.currentTimeMillis();
        final long t1 = t2 - (1000 * 150);

        // Builds the URI
        final String uri = buildURI(bus.getDgw(),"Ericsson$Stop_Pressed",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        final StopPressedParser parser = new StopPressedParser(callback);

        final RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    @Override
    public void getNbrOfWifiUsers(Bus bus, WifiHandler callback) {
        //Requests data since 12 sec earlier.
        final long t2 = System.currentTimeMillis();
        final long t1 = t2 - (1000 * QUERY_LENGTH);

        // Builds the URI
        final String uri = buildURI(bus.getDgw(),"Ericsson$Online_Users",null,t1,t2);

        Log.i("URI", uri);

        // Adds a parser for handling the JSONArray and gives it a class to inform when done.
        final WifiUsersParser parser = new WifiUsersParser(callback);

        final RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    // sensorSpec and resourceSpec can't be given at the same time.
    private String buildURI(String dgw, String sensorSpec, String resourceSpec, long t1, long t2){
        final StringBuilder sb = new StringBuilder(47);

        sb.append("https://ece01.ericsson.net:4443/ecity?");
        if(dgw != null){
            sb.append("dgw=").append(dgw).append('&');
        }
        if(sensorSpec != null){
            sb.append("sensorSpec=").append(sensorSpec).append('&');
        }
        if(resourceSpec != null){
            sb.append("resourceSpec=").append(resourceSpec).append('&');
        }
        sb.append("t1=").append(t1).append("&t2=").append(t2);

        return sb.toString();
    }

    private Map<String,String> createBasicAuth(String auth){
        final Map<String,String> headerMap = new HashMap<String,String>();

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
