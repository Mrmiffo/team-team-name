package com.teamteamname.gotogothenburg.api.electricity.parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ApiRequestError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.WifiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class WifiUsersParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    WifiHandler callback;

    public WifiUsersParser(WifiHandler callback){ this.callback = callback; }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("WifiUsersParser", error.toString());
        ApiRequestError elecError = new ApiRequestError();
        elecError.setResponseHeaders(error.networkResponse.headers);
        elecError.setResponseStatusCode(error.networkResponse.statusCode);
        elecError.setMessage(error.getMessage());
        elecError.setNetworkTimeMs(error.getNetworkTimeMs());
        callback.electricityRequestError(elecError);
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.i("Response", response.toString());

        JSONObject wifiUsers = getLatestJSONValue("Total_Online_Users_Value",response);
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
