package com.teamteamname.gotogothenburg.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

/**
 * Created by Mattias Ahlstedt on 2015-09-28.
 */
public class VasttrafikAPI implements IVasttrafikAPI{

    private static IVasttrafikAPI instance;

    private String baseURL;
    private String apiKey;

    private String autocomplete = "location.name";
    private String nearbyStops = "location.nearbystops";
    private String trip = "trip";
    private String format = "format=json";

    @Setter private int walkSpeed;
    @Setter private int maxWalkDist;
    @Setter private int addChangeTime;
    @Setter private int maxChanges;
    @Setter private boolean wheelchair;
    @Setter private boolean stroller;
    @Setter private boolean lowFloor;
    @Setter private boolean rampLift;

    private RequestQueue queue;

    private VasttrafikAPI(Context context, RequestQueue queue){
        this.queue = queue;
        this.baseURL = context.getResources().getString(R.string.vasttrafik_base_url);
        this.apiKey = context.getResources().getString(R.string.vasttrafik_api_key);
    }

    public static void init(Context context, RequestQueue queue){
        if(instance == null) {
            VasttrafikAPI api = new VasttrafikAPI(context, queue);
            instance = api;
        }
    }

    public static IVasttrafikAPI getInstance(){
        return instance;
    }

    @Override
    public void getCoordinates(VasttrafikHandler callback, LatLng origin, LatLng dest) {

    }

    @Override
    public void getCoordinates(VasttrafikHandler callback, String originStop, String destStop) {

    }

    @Override
    public void getAutocomplete(VasttrafikHandler callback, String input) {

        String sanitizedInput = sanitize(input);

        StringBuilder sb = new StringBuilder();
        sb.append(baseURL);
        sb.append(autocomplete + "?");
        sb.append("authKey=" + apiKey + "&");
        sb.append(format + "&");
        sb.append("input=" + sanitizedInput);
        String uri = sb.toString();

        AutocompleteParser parser = new AutocompleteParser(callback);
        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void getNearbyStops(VasttrafikHandler callback, LatLng origin, int maxDist) {

    }

    private String sanitize(String input){
        input = input.replace('ö', 'o');
        input = input.replace('å', 'a');
        input = input.replace('ä', 'a');
        input = input.replace('%', '\0');
        return input;
    }

    private class AutocompleteParser implements Response.Listener<JSONObject>, Response.ErrorListener{

        private VasttrafikHandler callback;

        public AutocompleteParser(VasttrafikHandler callback){
            this.callback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            callback.vasttrafikRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONObject response) {
            List<String> suggestions = new ArrayList<String>();
            JSONObject locationList;
            JSONObject temp;
            JSONArray stopLocation;
            JSONArray coordLocation;
            try{
                if(response.has("LocationList")) {
                    locationList = (JSONObject) response.get("LocationList");
                    if (locationList.has("StopLocation")) {
                        stopLocation = (JSONArray) locationList.get("StopLocation");
                        for (int i = 0; i < 2; i++) {
                            temp = stopLocation.getJSONObject(i);
                            if(temp.has("name")) {
                                suggestions.add((String) temp.get("name"));
                            }
                        }
                    }

                    if (locationList.has("CoordLocation")) {
                        coordLocation = (JSONArray) locationList.get("CoordLocation");
                        for (int i = 0; i < 2; i++) {
                            temp = coordLocation.getJSONObject(i);
                            if(temp.has("name")) {
                                suggestions.add((String) temp.get("name"));
                            }
                        }
                    }
                }
            } catch (JSONException e){
                Log.e("JSONException", e.toString());
            }
            callback.vasttrafikRequestDone(suggestions);
        }
    }
}
