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
public class WifiUsersParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    final private WifiHandler callback;

    public WifiUsersParser(WifiHandler callback){ this.callback = callback; }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("WifiUsersParser", error.toString());
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
        Log.i("Response", response.toString());

        final JSONObject wifiUsers = getLatestJSONValue("Total_Online_Users_Value",response);
        callback.electricityWifiUsersResponse(30);
        try {
            callback.electricityWifiUsersResponse(wifiUsers.getInt("value"));
        }catch(JSONException e){
            Log.e("JSONException","Error getting values from JSONObject-response");
        }catch(NullPointerException e){
            Log.e("NullPointer","Error reading JSONObjects");
        }
    }
}
