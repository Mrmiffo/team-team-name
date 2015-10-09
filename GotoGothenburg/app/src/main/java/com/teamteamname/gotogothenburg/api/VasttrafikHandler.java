package com.teamteamname.gotogothenburg.api;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-25.
 */
public interface VasttrafikHandler {

    /**
     * Called with the list of autocomplete suggestions which are to be displayed
     * @param autocomplete  The list of suggestions
     */
    public void vasttrafikRequestDone(VasttrafikLocation... autocomplete);

    /**
     * Called with the list of coords for a route
     * @param polyline The list of coords
     */
    public void vasttrafikRequestDone(int r, int g, int b, LatLng... polyline);

    /**
     * Called if any unsuspected error occurs
     * @param e The error message
     */
    void vasttrafikRequestError(String e);
}
