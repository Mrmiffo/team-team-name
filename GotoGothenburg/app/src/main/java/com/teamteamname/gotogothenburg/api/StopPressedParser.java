package com.teamteamname.gotogothenburg.api;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class StopPressedParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    private StopButtonHandler callback;

    public StopPressedParser(StopButtonHandler callback){ this.callback = callback; }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("StopPressedParser", error.toString());
        ApiRequestError elecError;
        if(error.networkResponse == null) {
            elecError = null;
        } else {
            elecError = new ApiRequestError(error.getMessage(),
                    error.networkResponse.headers,
                    error.networkResponse.statusCode,
                    error.getNetworkTimeMs());
        }
        callback.requestError(elecError);
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.i("Response",response.toString());

        final JSONObject stopPressed = getLatestJSONValue("Stop_Pressed_Value",response);

        try {
            callback.electricityStopPressedResponse(stopPressed.getBoolean("value"));
        }catch(JSONException e){
            Log.e("JSONException","Error getting values from JSONObject-response");
        }catch(NullPointerException e){
            Log.e("NullPointer","Error reading JSONObjects");
        }
    }
}