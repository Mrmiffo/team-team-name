package com.teamteamname.gotogothenburg.api.vasttrafik;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.AutocompleteHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.ErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.TripHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.parsers.AutocompleteParser;
import com.teamteamname.gotogothenburg.api.vasttrafik.parsers.TripParser;

/**
 * Created by Mattias Ahlstedt on 2015-09-28.
 */
public class VasttrafikAPI  implements ITrip, IAutocomplete {

    private String baseURL;
    private String apiKey;

    private String autocomplete = "location.name";
    private String trip = "trip";
    private String format = "format=json";

    /*
    // These aren't needed untill the outcommented methods in the interface are implemented
    @Setter private int walkSpeed;
    @Setter private int maxWalkDist;
    @Setter private int addChangeTime;
    @Setter private int maxChanges;
    @Setter private boolean wheelchair;
    @Setter private boolean stroller;
    @Setter private boolean lowFloor;
    @Setter private boolean rampLift;
    */

    private RequestQueue queue;

    public VasttrafikAPI(Context context, RequestQueue queue){
        this.queue = queue;
        this.baseURL = context.getResources().getString(R.string.vasttrafik_base_url);
        this.apiKey = context.getResources().getString(R.string.vasttrafik_api_key);
    }

    /**
     * Setups the first part of the url which is always needed
     * @param service the service which is to be called
     * @return a stringbuilder containing the base of the url
     */
    private StringBuilder setupRequest(String service){
        StringBuilder sb = new StringBuilder();
        sb.append(baseURL);
        sb.append(service + "?");
        sb.append("authKey=" + apiKey + "&");
        sb.append(format + "&");
        return sb;
    }

    @Override
    public void getTrip(TripHandler tripCallback, ErrorHandler errorCallback, String originName, LatLng originCoords, String destName, LatLng destCoords) {
        StringBuilder sb = setupRequest(trip);
        sb.append("needGeo=1&");
        sb.append("originCoordLat=" + originCoords.latitude + "&");
        sb.append("originCoordLong=" + originCoords.longitude + "&");
        sb.append("originCoordName=" + sanitize(originName) + "&");
        sb.append("destCoordLat=" + destCoords.latitude + "&");
        sb.append("destCoordLong=" + destCoords.longitude + "&");
        sb.append("destCoordName=" + sanitize(destName));
        String uri = sb.toString();

        TripParser parser = new TripParser(tripCallback, errorCallback, uri, queue);
        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
        queue.add(request);
    }

    @Override
    public void getAutocomplete(AutocompleteHandler autoCallback, ErrorHandler errorCallback, String input) {

        String sanitizedInput = sanitize(input);

        StringBuilder sb = setupRequest(autocomplete);
        sb.append("input=" + sanitizedInput);
        String uri = sb.toString();

        AutocompleteParser parser = new AutocompleteParser(autoCallback, errorCallback, uri, queue);
        JsonObjectRequest request = new JsonObjectRequest(uri, null, parser, parser);
        queue.add(request);
    }

    /**
     * replaces any occurences of "ö", "å", "ä" with "o" respectively "a"
     * removes any none word characters
     * @param input the string which is to be sanitized
     * @return the sanitized string
     */
    private String sanitize(String input){
        input = input.replace('ö', 'o');
        input = input.replace('å', 'a');
        input = input.replace('ä', 'a');
        input = input.replaceAll("\\W", "");
        return input;
    }
}
