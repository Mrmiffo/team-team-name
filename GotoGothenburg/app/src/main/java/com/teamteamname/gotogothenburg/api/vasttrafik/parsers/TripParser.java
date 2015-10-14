package com.teamteamname.gotogothenburg.api.vasttrafik.parsers;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikTripHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            createGeoRequestsFromJSON(response);
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
        }
    }

    private void createGeoRequestsFromJSON(JSONObject jo) throws JSONException {
        if (jo.has("TripList")) {
            JSONObject tripList = (JSONObject) jo.get("TripList");
            ifHasTrip(tripList);
        }
    }

    private void ifHasTrip(JSONObject jo) throws JSONException {
        if (jo.has("Trip")) {
            JSONArray trip = (JSONArray) jo.get("Trip");
            ifHasLeg(trip);
        }
    }

    private void ifHasLeg(JSONArray ja) throws JSONException {
        if (((JSONObject) ja.get(0)).has("Leg")) {
            JSONArray leg = (JSONArray) ((JSONObject) ja.get(0)).get("Leg");
            addRequestsToQueue(leg);
        }
    }

    private void addRequestsToQueue(JSONArray ja) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            String uri = ifHasType((JSONObject) ja.get(i));
            if (uri == null) {
                errorCallback.vasttrafikRequestError("null response");
            }
            boolean walk = ((JSONObject) ja.get(i)).get("type").equals("WALK");
            queue.add(createRequest(uri, i == 0, walk));
        }
    }

    private String ifHasType(JSONObject jo) throws JSONException {
        if (jo.has("type")) {
            return ifHasGeoRef(jo);
        }
        return null;
    }

    private String ifHasGeoRef(JSONObject jo) throws JSONException {
        if (jo.has("GeometryRef")) {
            return (String) ((JSONObject) jo.get("GeometryRef")).get("ref");
        }
        return null;
    }

    private JsonObjectRequest createRequest(String uri, boolean newPolyline, boolean walk) {
        GeoParser parser = new GeoParser(tripCallback, errorCallback, uri, newPolyline, walk, queue);
        return new JsonObjectRequest(uri, null, parser, parser);
    }
}
