package com.teamteamname.gotogothenburg.api.electricity.parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ApiRequestError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.NextStopHandler;
import com.teamteamname.gotogothenburg.api.Stops;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Olof on 14/10/2015.
 */
public class NextStopParser extends ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{

    NextStopHandler callback;

    public NextStopParser(NextStopHandler callback){
        this.callback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ApiRequestError elecError = new ApiRequestError();
        if(error.networkResponse != null) {
            elecError.setResponseHeaders(error.networkResponse.headers);
            elecError.setResponseStatusCode(error.networkResponse.statusCode);
        }
        elecError.setMessage(error.getMessage());
        elecError.setNetworkTimeMs(error.getNetworkTimeMs());
        callback.electricityRequestError(elecError);
    }

    @Override
    public void onResponse(JSONArray response) {
        if(response.length()>0) {
            JSONObject nextStop = this.getLatestJSONValue("Bus_Stop_Name_Value", response);
            boolean stopExists = false;

            try {
                //TODO Make cleaner sanitizer
                String sanetizedString = sanitizeString(nextStop.getString("value"));
                sanetizedString = sanetizedString.substring(0, sanetizedString.length() - 1).replace(' ','_').replace(".","").toUpperCase();
                for (Stops stop : Stops.values()) {
                    //Checks caps to match
                    if (sanetizedString.equals(stop.toString())) {
                        callback.electricityNextStopResponse(stop);
                        stopExists = true;
                    }
                }
            } catch (JSONException e) {
                Log.e("JSONException", "Error getting values from JSONObject-response");
            } catch (NullPointerException e) {
                Log.e("NullPointer", "Error reading JSONObjects");
            }
        }
    }
}
