package com.teamteamname.gotogothenburg.api;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-25.
 */
public interface VasttrafikHandler {

    /**
     * Called with the list of autocomplete suggestions which are to be displayed
     * @param autocomplete  The list of suggestions
     */
    void vasttrafikRequestDone(List<String> autocomplete);

    /**
     * Called if any unsuspected error occurs
     * @param e The error message
     */
    void vasttrafikRequestError(String e);
}
