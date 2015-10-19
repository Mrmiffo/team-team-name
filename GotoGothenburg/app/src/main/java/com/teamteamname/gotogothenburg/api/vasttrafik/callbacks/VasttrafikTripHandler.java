package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;

/**
 * Created by Mattias Ahlstedt on 2015-10-09.
 */
public interface VasttrafikTripHandler {

    /**
     * Callback for returning trip information and the corresponding polylines
     * @param tripInfo the trip information
     * @param polyline the polylines
     */
    public void vasttrafikRequestDone(VasttrafikChange[] tripInfo, PolylineOptions[] polyline);
}
