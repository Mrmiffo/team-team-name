package com.teamteamname.gotogothenburg.api.vasttrafik.callbacks;

/**
 * Created by Mattias Ahlstedt on 2015-09-25.
 */
public interface ErrorHandler {
    /**
     * Called if any unmanageble errors occur
     * @param e The error message
     */
    void RequestError(String e);
}
