package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-10-16.
 */
public interface GeoCallback {
    /**
     * Callback for returning a list of Polylines
     * @param polylines the polylines
     */
    void polylineRequestDone(List<PolylineOptions> polylines);

    /**
     *
     * @param line
     * @param stopName
     * @param track
     * @param position
     */
    void markerRequestDone(String line, String stopName, String track, LatLng position);
}
