package com.teamteamname.gotogothenburg.api;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Olof on 14/10/2015.
 */

public abstract class ElectricityParser implements Response.Listener<JSONArray>, Response.ErrorListener{
    private final Map<CharSequence,CharSequence> encodings = new HashMap<CharSequence,CharSequence>(6);

    public ElectricityParser(){
        encodings.put("Å","%C3%85");
        encodings.put("Ä","%C3%84");
        encodings.put("Ö","%C3%96");
        encodings.put("å","%C3%A5");
        encodings.put("ä","%C3%A4");
        encodings.put("ö","%C3%B6");
    }
    /** Returns the JSONObject with latest value (according to the timestamps) for a specific resource out of a array which could contain other resources.
     * @param resourceName
     * @param array
     * @return
     */
    public JSONObject getLatestJSONValue(String resourceName, JSONArray array){

        try{
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

    /**
     * Replaces encoded uses of Å,Ä,Ö,å,ä,ö with the corresponding character.
     * @param string
     * @return
     * A string without encoded characters
     */
    public String sanitizeString(String string){
        return string.replace(encodings.get("Å"),"Å").replace(encodings.get("Ä"),"Ä").replace(encodings.get("Ö"),"Ö").replace(encodings.get("å"),"å").replace(encodings.get("ä"),"ä").replace(encodings.get("ö"),"ö");
    }
}
