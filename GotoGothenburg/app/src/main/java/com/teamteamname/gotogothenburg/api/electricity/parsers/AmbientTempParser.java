package com.teamteamname.gotogothenburg.api.electricity.parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.ApiRequestError;
import com.teamteamname.gotogothenburg.api.TempHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class AmbientTempParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    private TempHandler callback;

    public AmbientTempParser(TempHandler callback){ this.callback = callback; }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("AmbientTempParser", error.toString());
        final ApiRequestError elecError = new ApiRequestError(error.getMessage(),
                error.networkResponse.headers,
                error.networkResponse.statusCode,
                error.getNetworkTimeMs());
        callback.electricityRequestError(elecError);
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.i("Response",response.toString());

        final JSONObject temperature = getLatestJSONValue("Ambient_Temperature_Value",response);

        try {
            callback.electricityAmbientTemperatureResponse(temperature.getDouble("value"));
        }catch(JSONException e){
            Log.e("JSONException","Error getting values from JSONObject-response");
        }catch(NullPointerException e){
            Log.e("NullPointer","Error reading JSONObjects");
        }
    }
}
