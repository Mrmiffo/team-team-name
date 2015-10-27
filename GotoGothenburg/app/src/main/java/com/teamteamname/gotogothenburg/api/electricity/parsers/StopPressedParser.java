package com.teamteamname.gotogothenburg.api.electricity.parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ApiRequestError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.StopButtonHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class StopPressedParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    StopButtonHandler callback;

    public StopPressedParser(StopButtonHandler callback){ this.callback = callback; }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("StopPressedParser", error.toString());
        ApiRequestError elecError = new ApiRequestError();
        elecError.setResponseHeaders(error.networkResponse.headers);
        elecError.setResponseStatusCode(error.networkResponse.statusCode);
        elecError.setMessage(error.getMessage());
        elecError.setNetworkTimeMs(error.getNetworkTimeMs());
        callback.electricityRequestError(elecError);
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.i("Response",response.toString());

        JSONObject stopPressed = getLatestJSONValue("Stop_Pressed_Value",response);

        try {
            callback.electricityStopPressedResponse(stopPressed.getBoolean("value"));
        }catch(JSONException e){
            Log.e("JSONException","Error getting values from JSONObject-response");
        }catch(NullPointerException e){
            Log.e("NullPointer","Error reading JSONObjects");
        }
    }
}