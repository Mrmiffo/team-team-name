package com.teamteamname.gotogothenburg.api;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.teamteamname.gotogothenburg.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olof on 25/09/2015.
 */
public class ElectricityAPI implements IGetBusLocation,IGetNextStop,IGetAmbientTemp,IGetCabinTemp,IGetStopPressed,IGetNbrOfWifiUsers {

    final private RequestQueue queue;
    //The query time is set to 60sec as this seems to be about the time it takes for the bus to update this value. Still sometimes no value exist.
    private final int queryLength = 60;

    private String apiKey;

    public ElectricityAPI(Context context, RequestQueue queue){
        this.queue = queue;
        apiKey = context.getResources().getString(R.string.electricity_api_key);
    }

    @Override
    public void getBusLocation(Bus bus, GPSHandler callback){
        final long secondsToQuery = queryLength;
        final String sensorSpec = "Ericsson$GPS2";
        final GPSCoordParser parser = new GPSCoordParser(callback);
        addNewRequest(bus, sensorSpec, null, parser, secondsToQuery);

    }

    @Override
    public void getNextStop(Bus bus, NextStopHandler callback) {
        final long secondsToQuery = queryLength;
        final String sensorSpec = "Ericsson$Next_Stop";
        final NextStopParser parser = new NextStopParser(callback);
        addNewRequest(bus, sensorSpec, null, parser, secondsToQuery);
    }

    @Override
    public void getAmbientTemperature(Bus bus, TempHandler callback) {
        final long secondsToQuery = queryLength;
        final String sensorSpec = "Ericsson$Ambient_Temperature";
        final AmbientTempParser parser = new AmbientTempParser(callback);
        addNewRequest(bus, sensorSpec, null, parser, secondsToQuery);
    }

    @Override
    public void getCabinTemperature(Bus bus, TempHandler callback) {
        //Request data since 2,5 min earlier
        final long secondsToQuery = 150;
        final String sensorSpec = "Ericsson$Driver_Cabin_Temperature";
        final CabinTempParser parser = new CabinTempParser(callback);
        addNewRequest(bus, sensorSpec, null, parser, secondsToQuery);

    }

    @Override
    public void getStopPressed(Bus bus, StopButtonHandler callback) {
        //Request data since 2,5 min earlier
        final long secondsToQuery = 150;
        final String sensorSpec = "Ericsson$Stop_Pressed";
        final StopPressedParser parser = new StopPressedParser(callback);
        addNewRequest(bus, sensorSpec, null, parser, secondsToQuery);
    }

    @Override
    public void getNbrOfWifiUsers(Bus bus, WifiHandler callback) {
        final long secondsToQuery = queryLength;
        final String sensorSpec = "Ericsson$Online_Users";
        final WifiUsersParser parser = new WifiUsersParser(callback);
        addNewRequest(bus,sensorSpec,null,parser,secondsToQuery);
    }

    /**
     * Helper method that creates a new RequestsWithBAuth and sends it to the requestqueue.
     * @param bus
     * @param sensorSpec
     * @param resourceSpec
     * @param parser
     * @param querySeconds
     * Amount of seconds from which to query for data.
     */
    private void addNewRequest(Bus bus, String sensorSpec, String resourceSpec, ElectricityParser parser, long querySeconds){
        final long t2 = System.currentTimeMillis();
        final long t1 = t2 - (1000 * querySeconds);

        // Builds the URI
        final String uri = buildURI(bus.getDgw(),sensorSpec,resourceSpec,t1,t2);

        Log.i("URI", uri);

        final RequestWithBAuth request = new RequestWithBAuth(uri,parser,parser);
        queue.add(request);
    }

    /**
     * Builds the URI for electricity's API using the given parameters.
     * sensorSpec and resourceSpec should not both be given a value (one of them must be null).
     * @param dgw
     * @param sensorSpec
     * @param resourceSpec
     * @param t1
     * @param t2
     * @return
     */
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

    /**
     * Creates a header using the given string as Basic Authorization.
     * @param auth
     * @return
     */
    private Map<String,String> createBasicAuth(String auth){
        final Map<String,String> headerMap = new HashMap<String,String>();

        Log.i("Auth", "Authorization: Basic " + auth);
        headerMap.put("Authorization", "Basic " + auth);

        return headerMap;
    }

    /**
     * A request with Basic Authorization
     */
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
