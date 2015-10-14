package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikLocation;

/**
 * Created by Mattias Ahlstedt on 2015-10-09.
 */
public interface VasttrafikAutocompleteHandler {
    /**
     * Called with the list of autocomplete suggestions which are to be displayed
     * @param autocomplete  The list of suggestions
     */
    public void vasttrafikRequestDone(VasttrafikLocation... autocomplete);
}