package com.teamteamname.gotogothenburg.api.electricity.parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.GPSCoord;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityGPSHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class GPSCoordParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    private ElectricityGPSHandler callback;

    public GPSCoordParser(ElectricityGPSHandler callback){
        this.callback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("GPSCoordParser", error.toString());
        callback.electricityRequestError(error.toString());
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.i("Response",response.toString());

        JSONObject lat = getLatestJSONValue("Latitude2_Value",response);
        JSONObject lng = getLatestJSONValue("Longitude2_Value",response);

        try {
            callback.electricityGPSResponse(new GPSCoord((float) lat.getDouble("value"), (float) lng.getDouble("value")));
        }catch(JSONException e){
            Log.e("JSONException","Error getting values from JSONObject-response");
        }catch(NullPointerException e){
            Log.e("NullPointer","Error reading JSONObjects");
        }
    }
}
