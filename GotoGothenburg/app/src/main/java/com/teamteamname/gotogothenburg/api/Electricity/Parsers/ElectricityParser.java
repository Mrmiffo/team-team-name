package com.teamteamname.gotogothenburg.api.Electricity.Parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by Olof on 14/10/2015.
 */
public class ElectricityParser {
    // Returns the JSONObject with latest value (according to the timestamps) for a specific resource out of a array which could contain other resources.
    public JSONObject getLatestJSONValue(String resourceName, JSONArray array){

        PriorityQueue<JSONObject> objectsWithResourceName = new PriorityQueue<JSONObject>(2,new Comparator<JSONObject>(){

            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                Log.i("ParseLatest", lhs.toString());
                Log.i("ParseLatest",rhs.toString());
                try {
                    return rhs.getInt("timestamp")-lhs.getInt("timestamp");
                }catch (JSONException e){
                    Log.e("ParsingError","No timestamp in Object");
                }
                return 0;
            }
        });
        try{
            for(int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                if(object.getString("resourceSpec").equals(resourceName)){
                    objectsWithResourceName.add(object);
                }

            }

            return objectsWithResourceName.poll();

        }catch(JSONException e){
            Log.e("ParsingError", "Not able to find resource in array.");
        }

        //Should only return if there was an parsing error.
        return new JSONObject();
    }
}
