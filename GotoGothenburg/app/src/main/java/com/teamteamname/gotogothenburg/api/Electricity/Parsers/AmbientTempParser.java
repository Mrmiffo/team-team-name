package com.teamteamname.gotogothenburg.api.electricity.parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityTempHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class AmbientTempParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    ElectricityTempHandler callback;

    public AmbientTempParser(ElectricityTempHandler callback){ this.callback = callback; }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("AmbientTempParser", error.toString());
        callback.electricityRequestError(error.toString());
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.i("Response",response.toString());

        JSONObject temperature = getLatestJSONValue("Ambient_Temperature_Value",response);

        try {
            callback.electricityAmbientTemperatureResponse(temperature.getDouble("value"));
        }catch(JSONException e){
            Log.e("JSONException","Error getting values from JSONObject-response");
        }catch(NullPointerException e){
            Log.e("NullPointer","Error reading JSONObjects");
        }
    }
}
