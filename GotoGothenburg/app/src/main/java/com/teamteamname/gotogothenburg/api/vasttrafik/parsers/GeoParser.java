package com.teamteamname.gotogothenburg.api.vasttrafik.parsers;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
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
public class GeoParser implements Response.Listener<JSONObject>, Response.ErrorListener {

    private VasttrafikTripHandler tripCallback;
    private VasttrafikErrorHandler errorCallback;
    private String uri;
    private boolean newPolyline;
    private boolean walk;
    private RequestQueue queue;

    public GeoParser(VasttrafikTripHandler tripCallback, VasttrafikErrorHandler errorCallback, String uri, boolean newPolyline, boolean walk, RequestQueue queue) {
        this.tripCallback = tripCallback;
        this.errorCallback = errorCallback;
        this.newPolyline = newPolyline;
        this.walk = walk;
        this.uri = uri;
        this.queue = queue;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        GeoParser parser = new GeoParser(tripCallback, errorCallback, uri, newPolyline, walk, queue);
        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            List<LatLng> polyline = getPolylineFromJSON(response);
            if (polyline == null) {
                errorCallback.vasttrafikRequestError("null response");
            } else {
                polylineDone(polyline);
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
        }
    }

    private List<LatLng> getPolylineFromJSON(JSONObject jo) throws JSONException {
        if (jo.has("Geometry")) {
            JSONObject geometry = (JSONObject) jo.get("Geometry");
            return ifHasPoints(geometry);
        }
        return null;
    }

    private List<LatLng> ifHasPoints(JSONObject jo) throws JSONException {
        if (jo.has("Points")) {
            JSONObject points = (JSONObject) jo.get("Points");
            return ifHasPoint(points);
        }
        return null;
    }

    private List<LatLng> ifHasPoint(JSONObject jo) throws JSONException {
        if (jo.has("Point")) {
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

    private void polylineDone(List<LatLng> polyline) {
        if (walk) {
            returnDashed(polyline);
        } else {
            LatLng[] polylineArray = polyline.toArray(new LatLng[polyline.size()]);
            tripCallback.vasttrafikRequestDone(newPolyline, polylineArray);
        }
    }

    private LatLng[] returnDashed(List<LatLng> polyline) {
        List<LatLng> temp = new ArrayList<>();
        polyline = enhance(polyline);
        for (int i = 0; i < polyline.size(); i++) {
            temp.add(polyline.get(i));
            if (i % 2 == 0) {
                tripCallback.vasttrafikRequestDone(newPolyline, temp.toArray(new LatLng[temp.size()]));
                temp = new ArrayList<>();
            }
        }
        return polyline.toArray(new LatLng[polyline.size()]);
    }

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
