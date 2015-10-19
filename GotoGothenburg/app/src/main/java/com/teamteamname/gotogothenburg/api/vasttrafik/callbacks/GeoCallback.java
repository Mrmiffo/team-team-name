package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-10-16.
 */
public interface GeoCallback {
    /**
     * Callback for returning a list of Polylines
     * @param polylines the polylines
     */
    public void polylineRequestDone(List<PolylineOptions> polylines);

    /**
     * Callback for returning trip information
     * @param marker the trip information
     */
    public void markerRequestDone(VasttrafikChange marker);
}
