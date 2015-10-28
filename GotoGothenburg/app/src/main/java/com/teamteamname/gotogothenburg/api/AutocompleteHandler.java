package com.teamteamname.gotogothenburg.api;

import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mattias Ahlstedt on 2015-10-09.
 */
public interface AutocompleteHandler {

    /**
     *
     * @param suggestions
     */
    void requestDone(Pair<String, LatLng>... suggestions);
}
