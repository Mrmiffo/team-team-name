package com.teamteamname.gotogothenburg.api.vasttrafik.parsers;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikLocation;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikAutocompleteHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikErrorHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-10-14.
 */
public class AutocompleteParser implements Response.Listener<JSONObject>, Response.ErrorListener {

    private VasttrafikAutocompleteHandler autoCallback;
    private VasttrafikErrorHandler errorCallback;
    private List<VasttrafikLocation> locations;
    private String uri;
    private RequestQueue queue;

    public AutocompleteParser(VasttrafikAutocompleteHandler autoCallback, VasttrafikErrorHandler errorCallback, String uri, RequestQueue queue) {
        this.autoCallback = autoCallback;
        this.errorCallback = errorCallback;
        this.locations = new ArrayList<>();
        this.uri = uri;
        this.queue = queue;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AutocompleteParser parser = new AutocompleteParser(autoCallback, errorCallback, uri, queue);
        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONObject locationList;
        try {
            if (response.has("LocationList")) {
                locationList = (JSONObject) response.get("LocationList");
                addLocations(locationList, "CoordLocation");
                addLocations(locationList, "StopLocation");
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
        }
        autoCallback.vasttrafikRequestDone(locations.toArray(new VasttrafikLocation[locations.size()]));
    }

    private void addLocations(JSONObject locationList, String has) throws JSONException {
        JSONArray locations;
        JSONObject temp;
        if (locationList.has(has) && locationList.get(has) instanceof JSONArray) {
            locations = (JSONArray) locationList.get(has);
            for (int i = 0; i < 2; i++) {
                temp = locations.getJSONObject(i);
                if (temp.has("name") && temp.has("lon") && temp.has("lat")) {
                    String name = (String) temp.get("name");
                    double lat = Double.valueOf((String) temp.get("lat"));
                    double lng = Double.valueOf((String) temp.get("lon"));
                    VasttrafikLocation location = new VasttrafikLocation(name, lat, lng);
                    this.locations.add(location);
                }
            }
        }
    }
}