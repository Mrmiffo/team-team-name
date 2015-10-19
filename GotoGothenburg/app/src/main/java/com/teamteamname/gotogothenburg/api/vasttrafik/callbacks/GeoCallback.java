package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-10-16.
 */
public interface GeoCallback {
    public void polylineRequestDone(List<PolylineOptions> polyline);
    public void markerRequestDone(VasttrafikChange marker);
}
