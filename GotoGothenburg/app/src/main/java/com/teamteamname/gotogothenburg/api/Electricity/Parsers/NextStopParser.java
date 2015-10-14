package com.teamteamname.gotogothenburg.api.Electricity.Parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.Electricity.Handlers.ElectricityNextStopHandler;
import com.teamteamname.gotogothenburg.route.Stops;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class NextStopParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    ElectricityNextStopHandler callback;

    public NextStopParser(ElectricityNextStopHandler callback){
        this.callback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("NextStopParser", error.toString());
        callback.electricityRequestError(error.toString());
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.i("Response",response.toString());

        JSONObject nextStop = this.getLatestJSONValue("Bus_Stop_Name_Value",response);
        boolean stopExists = false;

        try {
            for(Stops stop:Stops.values()){
                //Checks caps to match
                if(nextStop.getString("value").toUpperCase().equals(stop.toString())){
                    callback.electricityNextStopResponse(stop);
                    stopExists = true;
                }
            }
            if(!stopExists){
                throw new IllegalArgumentException("No stop" + nextStop.getString("value") + "exists");
            }
        }catch(JSONException e){
            Log.e("JSONException","Error getting values from JSONObject-response");
        }catch(NullPointerException e){
            Log.e("NullPointer","Error reading JSONObjects");
        }
    }
}
