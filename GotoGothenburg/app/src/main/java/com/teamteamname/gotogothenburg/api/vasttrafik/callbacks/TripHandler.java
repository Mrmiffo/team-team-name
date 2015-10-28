package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Mattias Ahlstedt on 2015-10-09.
 */
public interface TripHandler {

    /**
     * @param lines
     * @param stopNames
     * @param tracks
     * @param positions
     * @param polyline
     */
    void requestDone(String[] lines, String[] stopNames, String[] tracks, LatLng[] positions, PolylineOptions[] polyline);
}
