package com.teamteamname.gotogothenburg.api;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mattias Ahlstedt on 2015-10-09.
 */
public interface VasttrafikTripHandler {
    /**
     * Called with the list of coords for a route
     * @param polyline The list of coords
     */
    public void vasttrafikRequestDone(boolean newPolyline, int r, int g, int b, LatLng... polyline);
}
