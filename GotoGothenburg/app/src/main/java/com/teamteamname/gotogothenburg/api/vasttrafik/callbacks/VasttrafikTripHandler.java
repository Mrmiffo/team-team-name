package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;

/**
 * Created by Mattias Ahlstedt on 2015-10-09.
 */
public interface VasttrafikTripHandler {
    /**
     * Called with the list of coords for a route
     * @param polyline The list of coords
     */
    public void vasttrafikRequestDone(boolean newPolyline, LatLng... polyline);

    public void vasttrafikRequestDone(boolean newPolyline, VasttrafikChange... tripInfo);
}
