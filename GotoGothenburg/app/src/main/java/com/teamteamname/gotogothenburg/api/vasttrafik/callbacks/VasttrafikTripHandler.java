package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;

/**
 * Created by Mattias Ahlstedt on 2015-10-09.
 */
public interface VasttrafikTripHandler {
    public void vasttrafikRequestDone(VasttrafikChange[] tripInfo, PolylineOptions[] polyline);
}
