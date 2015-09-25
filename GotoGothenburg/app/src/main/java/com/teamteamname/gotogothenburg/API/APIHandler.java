package com.teamteamname.gotogothenburg.API;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Olof on 21/09/2015.
 */
public class APIHandler implements IAPIHandler {

    private IDeviceAPI deviceAPI;
    private IGooglePlacesAPI placesAPI;

    public APIHandler(IDeviceAPI deviceAPI, IGooglePlacesAPI placesAPI){
        this.deviceAPI = deviceAPI;
        this.placesAPI = placesAPI;
    }

    @Override
    public String getWiFiRouterMAC() {
        return null;
    }

    @Override
    public List<String> autoComplete(String s) {
        return null;
    }

    @Override
    public void playSound(File sound) {
        deviceAPI.playSound(sound);
    }

    @Override
    public boolean iSHandsfreePluggedIn() {
        return deviceAPI.iSHandsfreePluggedIn();
    }
}


