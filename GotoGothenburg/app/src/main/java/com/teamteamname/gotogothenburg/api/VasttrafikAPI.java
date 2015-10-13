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

    /**
     * Setups the first part of the url which is always needed
     * @param service the service which is to be called
     * @return a stringbuilder containing the base of the url
     */
    private StringBuilder setupRequest(String service){
        StringBuilder sb = new StringBuilder();
        sb.append(baseURL);
        sb.append(service + "?");
        sb.append("authKey=" + apiKey + "&");
        sb.append(format + "&");
        return sb;
    }

    @Override
    public void getCoordinates(VasttrafikTripHandler tripCallback, VasttrafikErrorHandler errorCallback, VasttrafikLocation originLocation, VasttrafikLocation destLocation) {
        StringBuilder sb = setupRequest(trip);
        sb.append("needGeo=1&");
        sb.append("originCoordLat=" + originLocation.getLatlng().latitude + "&");
        sb.append("originCoordLong=" + originLocation.getLatlng().longitude + "&");
        sb.append("originCoordName=" + sanitize(originLocation.getName()) + "&");
        sb.append("destCoordLat=" + destLocation.getLatlng().latitude + "&");
        sb.append("destCoordLong=" + destLocation.getLatlng().longitude + "&");
        sb.append("destCoordName=" + sanitize(destLocation.getName()));
        String uri = sb.toString();

        TripParser parser = new TripParser(tripCallback, errorCallback, uri);
        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void getAutocomplete(VasttrafikAutocompleteHandler autoCallback, VasttrafikErrorHandler errorCallback, String input) {

        String sanitizedInput = sanitize(input);

        StringBuilder sb = setupRequest(autocomplete);
        sb.append("input=" + sanitizedInput);
        String uri = sb.toString();

        AutocompleteParser parser = new AutocompleteParser(autoCallback, errorCallback, uri);
        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
        queue.add(request);
    }

    /**
     * replaces any occurences of "ö", "å", "ä" with "o" respectively "a"
     * removes any none word characters
     * @param input the string which is to be sanitized
     * @return the sanitized string
     */
    private String sanitize(String input){
        input = input.replace('ö', 'o');
        input = input.replace('å', 'a');
        input = input.replace('ä', 'a');
        input = input.replaceAll("\\W", "");
        return input;
    }

    /**
     * Parses the answer from Vasttrafik's Geometry API and calls the callback with a list of coordinates
     */
    private class GeoParser implements Response.Listener<JSONObject>, Response.ErrorListener{

        private VasttrafikTripHandler tripCallback;
        private VasttrafikErrorHandler errorCallback;
        private String uri;
        private boolean newPolyline;
        private boolean walk;

        public GeoParser(VasttrafikTripHandler tripCallback, VasttrafikErrorHandler errorCallback, String uri, boolean newPolyline, boolean walk){
            this.tripCallback = tripCallback;
            this.errorCallback = errorCallback;
            this.newPolyline = newPolyline;
            this.walk = walk;
            this.uri = uri;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            GeoParser parser = new GeoParser(tripCallback, errorCallback, uri, newPolyline, walk);
            JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
            queue.add(request);
        }

        @Override
        public void onResponse(JSONObject response) {
            try{
                List<LatLng> polyline = getPolylineFromJSON(response);
                if(polyline == null){
                    errorCallback.vasttrafikRequestError("null response");
                } else {
                    polylineDone(polyline);
                }
            } catch (JSONException e){
                Log.e("JSONException", e.toString());
            }
        }

        private List<LatLng> getPolylineFromJSON(JSONObject jo) throws JSONException {
            if(jo.has("Geometry")) {
                JSONObject geometry = (JSONObject)jo.get("Geometry");
                return ifHasPoints(geometry);
            }
            return null;
        }

        private List<LatLng> ifHasPoints(JSONObject jo) throws JSONException {
            if(jo.has("Points")) {
                JSONObject points = (JSONObject) jo.get("Points");
                return ifHasPoint(points);
            }
            return null;
        }

        private List<LatLng> ifHasPoint(JSONObject jo) throws JSONException {
            if(jo.has("Point")){
                JSONArray point = (JSONArray) jo.get("Point");
                return createPolyline(point);
            }
            return null;
        }

        private List<LatLng> createPolyline(JSONArray ja) throws JSONException {
            List<LatLng> polyline = new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject temp = (JSONObject) ja.get(i);
                Double lat = Double.parseDouble((String) temp.get("lat"));
                Double lng = Double.parseDouble((String) temp.get("lon"));
                LatLng latlng = new LatLng(lat, lng);
                polyline.add(latlng);
            }
            return polyline;
        }

        private void polylineDone(List<LatLng> polyline){
            if(walk){
                returnDashed(polyline);
            } else {
                LatLng[] polylineArray = polyline.toArray(new LatLng[polyline.size()]);
                tripCallback.vasttrafikRequestDone(newPolyline, polylineArray);
            }

        }

        private LatLng[] returnDashed(List<LatLng> polyline){
            List<LatLng> temp = new ArrayList<>();
            polyline = enhance(polyline);
            for(int i = 0; i < polyline.size(); i++){
                temp.add(polyline.get(i));
                if(i%2 == 0){
                    tripCallback.vasttrafikRequestDone(newPolyline, temp.toArray(new LatLng[temp.size()]));
                    temp = new ArrayList<>();
                }
            }
            return polyline.toArray(new LatLng[polyline.size()]);
        }

        private List<LatLng> enhance(List<LatLng> polyline){
            for(int i = 0; i < polyline.size(); i+=2){
                if(i >= polyline.size()-1){
                    break;
                }
                if(distance(polyline.get(i).latitude, polyline.get(i+1).latitude, polyline.get(i).longitude, polyline.get(i+1).longitude) > 21){
                    polyline.add(i+1, new LatLng((polyline.get(i).latitude + polyline.get(i+1).latitude)/2, (polyline.get(i).longitude + polyline.get(i+1).longitude)/2));
                    polyline = enhance(polyline);
                }
                if(distance(polyline.get(i).latitude, polyline.get(i+1).latitude, polyline.get(i).longitude, polyline.get(i+1).longitude) < 10){
                    polyline.remove(i+1);
                    polyline = enhance(polyline);
                }
            }
            return polyline;
        }

        private double distance(double lat1, double lat2, double lon1, double lon2) {
            final int R = 6371; // Radius of the earth

            Double latDistance = Math.toRadians(lat2 - lat1);
            Double lonDistance = Math.toRadians(lon2 - lon1);
            Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c * 1000; // convert to meters

            distance = Math.pow(distance, 2) + Math.pow(0, 2);

            return Math.sqrt(distance);
        }
    }

    /**
     * Parses the answer from Vasttrafik's trip API and creates a new request with the geometry ref
     */
    private class TripParser implements Response.Listener<JSONObject>, Response.ErrorListener{

        private VasttrafikTripHandler tripCallback;
        private VasttrafikErrorHandler errorCallback;
        private String uri;

        public TripParser(VasttrafikTripHandler tripCallback, VasttrafikErrorHandler errorCallback, String uri){
            this.tripCallback = tripCallback;
            this.errorCallback = errorCallback;
            this.uri = uri;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            TripParser parser = new TripParser(tripCallback, errorCallback, uri);
            JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
            queue.add(request);
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
                                    if (((JSONObject) leg.get(i)).has("GeometryRef")) {
                                        temp = (JSONObject) ((JSONObject) leg.get(i)).get("GeometryRef");
                                        String uri = (String) temp.get("ref");
                                        GeoParser parser = new GeoParser(tripCallback, errorCallback, uri, i==0, ((JSONObject) leg.get(i)).get("type").equals("WALK"));
                                        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
                                        queue.add(request);
                                    }
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

    /**
     * Parses the answer from Vasttrafik's Location.name API and calls the callback with a list of strings
     */
    private class AutocompleteParser implements Response.Listener<JSONObject>, Response.ErrorListener{

        private VasttrafikAutocompleteHandler autoCallback;
        private VasttrafikErrorHandler errorCallback;
        private List<VasttrafikLocation> locations;
        private String uri;

        public AutocompleteParser(VasttrafikAutocompleteHandler autoCallback, VasttrafikErrorHandler errorCallback, String uri){
            this.autoCallback = autoCallback;
            this.errorCallback = errorCallback;
            this.locations = new ArrayList<>();
            this.uri = uri;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            AutocompleteParser parser = new AutocompleteParser(autoCallback, errorCallback, uri);
            JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
            queue.add(request);
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
            autoCallback.vasttrafikRequestDone(locations.toArray(new VasttrafikLocation[locations.size()]));
        }

        private void addLocations(JSONObject locationList, String has) throws JSONException {
            JSONArray locations;
            JSONObject temp;
            if (locationList.has(has) && locationList.get(has) instanceof JSONArray) {
                locations = (JSONArray)locationList.get(has);
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
