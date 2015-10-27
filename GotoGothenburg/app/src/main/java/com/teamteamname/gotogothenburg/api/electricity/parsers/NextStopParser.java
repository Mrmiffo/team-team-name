package com.teamteamname.gotogothenburg.api.electricity.parsers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.teamteamname.gotogothenburg.api.ApiRequestError;
import com.teamteamname.gotogothenburg.api.NextStopHandler;
import com.teamteamname.gotogothenburg.api.Stops;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olof on 14/10/2015.
 */
public class NextStopParser  implements Response.Listener<JSONArray>, Response.ErrorListener{
    private NextStopHandler callback;
    private StopsAdapter stopsAdapter;

    public NextStopParser(NextStopHandler callback){
        this.callback = callback;
        this.stopsAdapter = new StopsAdapter();
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
        stopsAdapter.identifyStop(response);
    }

    private class StopsAdapter extends ElectricityParser {
        private Map<String, Stops> stopNames;
        public StopsAdapter(){
            //Create a list of stops where the names are modified to match the API output.
            stopNames = new HashMap<>();
            for (Stops stop: Stops.values()){
                String nameToAdd;
                if (Stops.KUNGSPORTSPLATSEN == stop){
                    nameToAdd = "KUNGSPORTSPL";
                } else if (Stops.SVEN_HULTINS_PLATS == stop){
                    nameToAdd = "SVEN_HULTINGS_PL";
                }

                else {
                    nameToAdd = stop.name();
                }
                //Replace _ with a space to match the API input.
                nameToAdd = nameToAdd.replace('_',' ');
                stopNames.put(nameToAdd, stop);
            };
        }
        public void identifyStop(JSONArray response){
            if(response.length()>0) {
                JSONObject nextStop = this.getLatestJSONValue("Bus_Stop_Name_Value", response);
                try {
                    //Sanitize the string to add å,ä,ö instead of UTF code.
                    String sanetizedString = sanitizeString(nextStop.getString("value"));
                    //Modify the string to remove the stop track.
                    sanetizedString = sanetizedString.substring(0, sanetizedString.length() - 1).replace(".","")
                            //Make the API response to uppercase to match the names.
                            .toUpperCase();
                    for (Map.Entry stop : stopNames.entrySet()) {
                        //Checks caps to match
                        if (sanetizedString.equals(stop.getKey())) {
                            callback.electricityNextStopResponse((Stops)stop.getValue());
                            break;
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
}
