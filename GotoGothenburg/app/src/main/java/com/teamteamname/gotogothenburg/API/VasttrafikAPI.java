package com.teamteamname.gotogothenburg.API;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.R;

import org.json.JSONArray;

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
        StringBuilder sb = new StringBuilder();
        sb.append(baseURL);
        sb.append(autocomplete + "?");
        sb.append("authKey=" + apiKey + "&");
        sb.append("input=" + input);
        String uri = sb.toString();

        AutocompleteParser parser = new AutocompleteParser(callback);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, uri, parser, parser);
        queue.add(request);
    }

    @Override
    public void getNearbyStops(VasttrafikHandler callback, LatLng origin, int maxDist) {

    }

    private class AutocompleteParser implements Response.Listener<JSONArray>, Response.ErrorListener{

        private VasttrafikHandler callback;

        public AutocompleteParser(VasttrafikHandler callback){
            this.callback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            callback.vasttrafikRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONArray response) {

        }
    }
}
