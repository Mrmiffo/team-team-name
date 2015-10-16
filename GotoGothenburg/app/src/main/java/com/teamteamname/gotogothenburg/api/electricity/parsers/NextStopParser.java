package com.teamteamname.gotogothenburg.api.electricity.parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityNextStopHandler;
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
        ElectricityError elecError = new ElectricityError();
        elecError.setResponseHeaders(error.networkResponse.headers);
        elecError.setResponseStatusCode(error.networkResponse.statusCode);
        elecError.setMessage(error.getMessage());
        elecError.setNetworkTimeMs(error.getNetworkTimeMs());
        callback.electricityRequestError(elecError);
    }

    @Override
    public void onResponse(JSONArray response) {
        JSONObject nextStop = this.getLatestJSONValue("Bus_Stop_Name_Value",response);
        boolean stopExists = false;

        try {
            String value = nextStop.getString("value");
            String stopName = value.substring(0,value.length()-1).toUpperCase();
            stopName = stopName.replace(" ", "_");
            for(Stops stop:Stops.values()){
                //Checks caps to match
                if (stopName.equals(stop.toString())){
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
