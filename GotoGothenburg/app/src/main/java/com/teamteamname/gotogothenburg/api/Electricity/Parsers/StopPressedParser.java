package com.teamteamname.gotogothenburg.api.Electricity.Parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.Electricity.Handlers.ElectricityStopButtonHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class StopPressedParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    ElectricityStopButtonHandler callback;

    public StopPressedParser(ElectricityStopButtonHandler callback){ this.callback = callback; }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("StopPressedParser", error.toString());
        callback.electricityRequestError(error.toString());
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