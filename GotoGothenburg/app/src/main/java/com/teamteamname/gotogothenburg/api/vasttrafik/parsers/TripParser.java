package com.teamteamname.gotogothenburg.api.vasttrafik.parsers;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.ErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.TripHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-10-14.
 */
public class TripParser implements Response.Listener<JSONObject>, Response.ErrorListener {

    private TripHandler tripCallback;
    private ErrorHandler errorCallback;
    private String url;
    private RequestQueue queue;

    public TripParser(TripHandler tripCallback, ErrorHandler errorCallback, String url, RequestQueue queue) {
        this.tripCallback = tripCallback;
        this.errorCallback = errorCallback;
        this.url = url;
        this.queue = queue;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        TripParser parser = new TripParser(tripCallback, errorCallback, url, queue);
        JsonObjectRequest request = new JsonObjectRequest(url, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray ja = getTrip(response);
            if(ja != null){
                createGeoManager(ja);
            } else {
                onErrorResponse(new VolleyError());
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
        }
    }

    private JSONArray getTrip(JSONObject input) throws JSONException {
        if (input.has("TripList")) {
            JSONObject tripList = (JSONObject) input.get("TripList");
            if (tripList.has("Trip")) {
                JSONArray trip = (JSONArray) tripList.get("Trip");
                if (((JSONObject) trip.get(0)).has("Leg")) {
                    return (JSONArray) ((JSONObject) trip.get(0)).get("Leg");
                }
            }
        }
        return null;
    }

    private void createGeoManager(JSONArray ja) throws JSONException {
        List<String> urls = new ArrayList<>();
        List<Boolean> walks = new ArrayList<>();
        List<Change> trips = new ArrayList<>();

        for (int i = 0; i < ja.length(); i++) {
            JSONObject temp = (JSONObject)ja.get(i);
            String url = getGeoRefUrl(temp);
            if(url == null){
                onErrorResponse(new VolleyError());
                return;
            }
            boolean walk = temp.get("type").equals("WALK");
            Change trip = getTripInfo(temp);

            urls.add(url);
            walks.add(walk);
            trips.add(trip);
        }
        new GeoManager(tripCallback, errorCallback, queue, ja.length(), urls, trips, walks);
    }

    private String getGeoRefUrl(JSONObject jo) throws JSONException {
        if (jo.has("GeometryRef")) {
            return (String) ((JSONObject) jo.get("GeometryRef")).get("ref");
        }
        return null;
    }

    private Change getTripInfo(JSONObject jo) throws JSONException{
        if(jo.has("Origin")){
            JSONObject temp = (JSONObject) jo.get("Origin");

            String line = jo.has("sname") ? (String)jo.get("sname") : "Walk";
            String stopName = temp.has("name") ? (String)temp.get("name") : "Stop name missing";
            String track =  temp.has("track") ? (String)temp.get("track") : "";
            return new Change(line, stopName, track, null);
        }
        return null;
    }
}
