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

    final private TripHandler tripCallback;
    final private ErrorHandler errorCallback;
    final private String url;
    final private RequestQueue queue;

    public TripParser(TripHandler tripCallback, ErrorHandler errorCallback, String url, RequestQueue queue) {
        this.tripCallback = tripCallback;
        this.errorCallback = errorCallback;
        this.url = url;
        this.queue = queue;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        final TripParser parser = new TripParser(tripCallback, errorCallback, url, queue);
        final JsonObjectRequest request = new JsonObjectRequest(url, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            final JSONArray ja = getTrip(response);
            if(ja == null){
                onErrorResponse(new VolleyError());
            } else {
                createGeoManager(ja);
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
        }
    }

    private JSONArray getTrip(JSONObject input) throws JSONException {
        if (input.has("TripList")) {
            final JSONObject tripList = (JSONObject) input.get("TripList");
            if (tripList.has("Trip")) {
                final JSONArray trip = (JSONArray) tripList.get("Trip");
                if (((JSONObject) trip.get(0)).has("Leg")) {
                    return (JSONArray) ((JSONObject) trip.get(0)).get("Leg");
                }
            }
        }
        return null;
    }

    private void createGeoManager(JSONArray ja) throws JSONException {
        final List<String> urls = new ArrayList<>();
        final List<Boolean> walks = new ArrayList<>();
        final List<Change> trips = new ArrayList<>();

        for (int i = 0; i < ja.length(); i++) {
            final JSONObject temp = (JSONObject)ja.get(i);
            final String url = getGeoRefUrl(temp);
            if(url == null){
                onErrorResponse(new VolleyError());
                return;
            }
            final boolean walk = temp.get("type").equals("WALK");
            final Change trip = getTripInfo(temp);

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
            final JSONObject temp = (JSONObject) jo.get("Origin");

            final String line = jo.has("sname") ? (String)jo.get("sname") : "Walk";
            final String stopName = temp.has("name") ? (String)temp.get("name") : "Stop name missing";
            final String track =  temp.has("track") ? (String)temp.get("track") : "";
            return new Change(line, stopName, track, null);
        }
        return null;
    }
}
