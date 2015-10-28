package com.teamteamname.gotogothenburg.api;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mattias Ahlstedt on 2015-10-27.
 */
public interface ITrip {
    /**
     * Sends a list of coordinates and information regarding the requested trip to the callback
     * The coordinates are a route from originLocation to destLocation
     * @param tripCallback the callback supplied with response
     * @param errorCallback the callback supplied with an eventual error
     * @param originName
     * @param originCoords
     * @param destName
     * @param destCoords
     */
    void getTrip(TripHandler tripCallback, ErrorHandler errorCallback, String originName, LatLng originCoords, String destName, LatLng destCoords);
}
