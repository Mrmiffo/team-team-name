package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-25.
 */
public interface VasttrafikErrorHandler {
    /**
     * Called if any unsuspected error occurs
     * @param e The error message
     */
    void vasttrafikRequestError(String e);
}
