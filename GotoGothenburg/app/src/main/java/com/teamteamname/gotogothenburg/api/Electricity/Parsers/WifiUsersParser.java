package com.teamteamname.gotogothenburg.api.Electricity.Parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.Electricity.Handlers.ElectricityWifiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class WifiUsersParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    ElectricityWifiHandler callback;

    public WifiUsersParser(ElectricityWifiHandler callback){ this.callback = callback; }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("WifiUsersParser", error.toString());
        callback.electricityRequestError(error.toString());
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
