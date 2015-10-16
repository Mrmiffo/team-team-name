package com.teamteamname.gotogothenburg.api.vasttrafik.parsers;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikTripHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-10-14.
 */
public class TripParser implements Response.Listener<JSONObject>, Response.ErrorListener {

    private VasttrafikTripHandler tripCallback;
    private VasttrafikErrorHandler errorCallback;
    private String uri;
    private RequestQueue queue;

    public TripParser(VasttrafikTripHandler tripCallback, VasttrafikErrorHandler errorCallback, String uri, RequestQueue queue) {
        this.tripCallback = tripCallback;
        this.errorCallback = errorCallback;
        this.uri = uri;
        this.queue = queue;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        TripParser parser = new TripParser(tripCallback, errorCallback, uri, queue);
        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray ja = getTrip(response);
            if(ja != null){
                addRequestsToQueue(ja);
            } else {
                errorCallback.vasttrafikRequestError("null response");
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
        }
    }

    private VasttrafikChange createTripInfo(JSONArray trip, int i) throws JSONException{
        String line = getLineFromJSON((JSONObject) trip.get(i));
        String stopName = getStopNameFromJSON((JSONObject)trip.get(i));
        String track = getTrackFromJSON((JSONObject)trip.get(i));
        return new VasttrafikChange(line, stopName, track, null);
    }

    private String getLineFromJSON(JSONObject jo) throws JSONException{
        return jo.has("sname") ? (String)jo.get("sname") : "Walk";
    }

    private String getStopNameFromJSON(JSONObject jo) throws JSONException {
        if(jo.has("Origin")){
            if(((JSONObject)jo.get("Origin")).has("name")) {
                return (String) ((JSONObject) jo.get("Origin")).get("name");
            }
        }
        return null;
    }

    private String getTrackFromJSON(JSONObject jo) throws JSONException {
        if(jo.has("Origin")){
            JSONObject temp = (JSONObject) jo.get("Origin");
            return temp.has("track") ? (String)temp.get("track") : "";
        }
        return null;
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

    private void addRequestsToQueue(JSONArray ja) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            String uri = getGeoRefUri((JSONObject) ja.get(i));
            boolean walk = ((JSONObject) ja.get(i)).get("type").equals("WALK");
            if(uri != null) {
                queue.add(createRequest(uri, createTripInfo(ja, i), i == 0, walk));
            } else {
                errorCallback.vasttrafikRequestError("null response");
            }
        }
    }

    private String getGeoRefUri(JSONObject jo) throws JSONException {
        if (jo.has("GeometryRef")) {
            return (String) ((JSONObject) jo.get("GeometryRef")).get("ref");
        }
        return null;
    }

    private JsonObjectRequest createRequest(String uri, VasttrafikChange trip, boolean newPolyline, boolean walk) {
        GeoParser parser = new GeoParser(tripCallback, errorCallback, uri, trip, newPolyline, walk, queue);
        return new JsonObjectRequest(uri, null, parser, parser);
    }
}
