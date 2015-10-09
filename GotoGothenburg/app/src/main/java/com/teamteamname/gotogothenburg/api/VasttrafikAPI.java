package com.teamteamname.gotogothenburg.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.R;

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

    private StringBuilder setupRequest(String service){
        StringBuilder sb = new StringBuilder();
        sb.append(baseURL);
        sb.append(service + "?");
        sb.append("authKey=" + apiKey + "&");
        sb.append(format + "&");
        return sb;
    }

    @Override
    public void getCoordinates(VasttrafikHandler callback, VasttrafikLocation originLocation, VasttrafikLocation destLocation) {
        StringBuilder sb = setupRequest(trip);
        sb.append("needGeo=1&");
        sb.append("originCoordLat=" + originLocation.getLatlng().latitude + "&");
        sb.append("originCoordLong=" + originLocation.getLatlng().longitude + "&");
        sb.append("originCoordName=" + sanitize(originLocation.getName()) + "&");
        sb.append("destCoordLat=" + destLocation.getLatlng().latitude + "&");
        sb.append("destCoordLong=" + destLocation.getLatlng().longitude + "&");
        sb.append("destCoordName=" + sanitize(destLocation.getName()));
        String uri = sb.toString();

        TripParser parser = new TripParser(callback);
        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void getAutocomplete(VasttrafikHandler callback, String input) {

        String sanitizedInput = sanitize(input);

        StringBuilder sb = setupRequest(autocomplete);
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
        input = input.replaceAll("\\W", "");
        return input;
    }

    private class GeoParser implements Response.Listener<JSONObject>, Response.ErrorListener{

        private VasttrafikHandler callback;
        private int r, g, b;

        public GeoParser(VasttrafikHandler callback, int r, int g, int b){
            this.callback = callback;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            callback.vasttrafikRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONObject response) {
            JSONObject geometry;
            JSONObject points;
            JSONObject temp;
            JSONArray point;

            try{
                if(response.has("Geometry")){
                    geometry = (JSONObject)response.get("Geometry");
                    if(geometry.has("Points")){
                        points = (JSONObject)geometry.get("Points");
                        if(points.has("Point")){
                            point = (JSONArray)points.get("Point");
                            List<LatLng> polyline = new ArrayList<LatLng>();
                            for(int i = 0; i < point.length(); i++){
                                temp = (JSONObject)point.get(i);
                                Double lat = Double.parseDouble((String)temp.get("lat"));
                                Double lng = Double.parseDouble((String)temp.get("lon"));
                                LatLng latlng = new LatLng(lat, lng);
                                polyline.add(latlng);
                            }
                            callback.vasttrafikRequestDone(r, g, b, polyline.toArray(new LatLng[polyline.size()]));
                        }
                    }
                }
            } catch (JSONException e){
                Log.e("JSONException", e.toString());
            }
        }
    }

    private class TripParser implements Response.Listener<JSONObject>, Response.ErrorListener{

        private VasttrafikHandler callback;

        public TripParser(VasttrafikHandler callback){
            this.callback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            callback.vasttrafikRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONObject response) {
            JSONObject tripList;
            JSONObject temp;
            JSONArray trip;
            JSONArray leg;
            try {
                if(response.has("TripList")) {
                    tripList = (JSONObject) response.get("TripList");
                    if(tripList.has("Trip")){
                        trip = (JSONArray) tripList.get("Trip");
                        if(((JSONObject)trip.get(0)).has("Leg")){
                            leg = (JSONArray) ((JSONObject) trip.get(0)).get("Leg");
                            for(int i = 0; i < leg.length(); i++) {
                                int r = 0, g = 0, b = 0;
                                if(((JSONObject) leg.get(i)).has("type")){
                                    if(((JSONObject) leg.get(i)).get("type").equals("WALK")){
                                        r = 0;
                                        g = 0;
                                        b = 255;
                                    } else if(((JSONObject) leg.get(i)).get("type").equals("BUS")){
                                        r = 0;
                                        g = 255;
                                        b = 0;
                                    }
                                }
                                if (((JSONObject) leg.get(i)).has("GeometryRef")) {
                                    temp = (JSONObject) ((JSONObject) leg.get(i)).get("GeometryRef");
                                    String uri = (String) temp.get("ref");
                                    GeoParser parser = new GeoParser(callback, r, g, b);
                                    JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
                                    queue.add(request);
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e("JSONException", e.toString());
            }
        }
    }

    private class AutocompleteParser implements Response.Listener<JSONObject>, Response.ErrorListener{

        private VasttrafikHandler callback;
        private List<VasttrafikLocation> locations;

        public AutocompleteParser(VasttrafikHandler callback){
            this.callback = callback;
            this.locations = new ArrayList<VasttrafikLocation>();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            callback.vasttrafikRequestError(error.toString());
        }

        @Override
        public void onResponse(JSONObject response) {
            JSONObject locationList;
            try{
                if(response.has("LocationList")) {
                    locationList = (JSONObject) response.get("LocationList");
                    addLocations(locationList, "CoordLocation");
                    addLocations(locationList, "StopLocation");
                }
            } catch (JSONException e){
                Log.e("JSONException", e.toString());
            }
            callback.vasttrafikRequestDone(locations.toArray(new VasttrafikLocation[locations.size()]));
        }

        private void addLocations(JSONObject locationList, String has) throws JSONException {
            JSONArray locations;
            JSONObject temp;
            if (locationList.has(has)) {
                locations = (JSONArray) locationList.get(has);
                for (int i = 0; i < 2; i++) {
                    temp = locations.getJSONObject(i);
                    if(temp.has("name") && temp.has("lon") && temp.has("lat")) {
                        String name = (String) temp.get("name");
                        double lat = Double.valueOf((String)temp.get("lat"));
                        double lng = Double.valueOf((String)temp.get("lon"));
                        VasttrafikLocation location = new VasttrafikLocation(name, lat, lng);
                        this.locations.add(location);
                    }
                }
            }
        }
    }
}
