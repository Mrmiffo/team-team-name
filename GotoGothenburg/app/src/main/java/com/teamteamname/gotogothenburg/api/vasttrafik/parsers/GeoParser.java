package com.teamteamname.gotogothenburg.api.vasttrafik.parsers;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.GeoCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-10-14.
 */
public class GeoParser implements Response.Listener<JSONObject>, Response.ErrorListener {

    private GeoCallback callback;
    private RequestQueue queue;

    private String url;
    private VasttrafikChange trip;
    private boolean walk;

    public GeoParser(GeoCallback callback, RequestQueue queue,
                     String url, VasttrafikChange trip, boolean walk) {

        this.callback = callback;
        this.queue = queue;

        this.url = url;
        this.trip = trip;
        this.walk = walk;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        GeoParser parser = new GeoParser(callback, queue, url, trip, walk);
        JsonObjectRequest request = new JsonObjectRequest(url, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray point = getPoint(response);
            if (point != null) {
                polylineDone(createPolyline(point));
                markerDone(trip, point);
            } else {
                onErrorResponse(new VolleyError());
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
        }
    }

    private void markerDone(VasttrafikChange vc, JSONArray ja) throws JSONException {
        LatLng temp = getCoordFromJSON(ja);
        if(temp != null) {
            callback.markerRequestDone(new VasttrafikChange(vc.getLine(), vc.getStopName(), vc.getTrack(), temp));
        } else {
            onErrorResponse(new VolleyError());
        }
    }

    private LatLng getCoordFromJSON(JSONArray ja) throws JSONException {
        if(((JSONObject)ja.get(0)).has("lat") && ((JSONObject)ja.get(0)).has("lon")){
            double lat = Double.parseDouble((String) ((JSONObject)ja.get(0)).get("lat"));
            double lng = Double.parseDouble((String) ((JSONObject)ja.get(0)).get("lon"));
            return new LatLng(lat, lng);
        }
        return null;
    }

    private JSONArray getPoint(JSONObject input) throws JSONException{
        if (input.has("Geometry")) {
            JSONObject geometry = (JSONObject) input.get("Geometry");
            if (geometry.has("Points")) {
                JSONObject points = (JSONObject) geometry.get("Points");
                if (points.has("Point")) {
                    return (JSONArray) points.get("Point");
                }
            }
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

    private void polylineDone(List<LatLng> polyline) {
        List<PolylineOptions> polylines;
        if(walk) {
            polylines = returnDashed(polyline);
        } else {
            polylines = new ArrayList<>();
            polylines.add((new PolylineOptions()).add(polyline.toArray(new LatLng[polyline.size()])));
        }
        callback.polylineRequestDone(polylines);
    }

    private List<PolylineOptions> returnDashed(List<LatLng> polyline) {
        List<LatLng> temp  = new ArrayList<>();
        List<PolylineOptions> polylines = new ArrayList<>();

        polyline = enhance(polyline);

        for (int i = 0; i < polyline.size(); i++) {
            temp.add(polyline.get(i));
            if (i % 2 == 0) {
                polylines.add((new PolylineOptions()).add(temp.toArray(new LatLng[temp.size()])));
                temp = new ArrayList<>();
            }
        }

        return polylines;
    }

    /*
    fills in and removes coordinates from the given list until there's a coordinate every 10-20 meters
     */
    private List<LatLng> enhance(List<LatLng> polyline) {
        for (int i = 0; i < polyline.size(); i += 2) {
            if (i >= polyline.size() - 1) {
                break;
            }
            if (distance(polyline.get(i).latitude, polyline.get(i + 1).latitude, polyline.get(i).longitude, polyline.get(i + 1).longitude) > 21) {
                polyline.add(i + 1, new LatLng((polyline.get(i).latitude + polyline.get(i + 1).latitude) / 2, (polyline.get(i).longitude + polyline.get(i + 1).longitude) / 2));
                polyline = enhance(polyline);
            }
            if (distance(polyline.get(i).latitude, polyline.get(i + 1).latitude, polyline.get(i).longitude, polyline.get(i + 1).longitude) < 10) {
                polyline.remove(i + 1);
                polyline = enhance(polyline);
            }
        }
        return polyline;
    }

    /*
    returns the distance in meters between two given coordinates
     */
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
