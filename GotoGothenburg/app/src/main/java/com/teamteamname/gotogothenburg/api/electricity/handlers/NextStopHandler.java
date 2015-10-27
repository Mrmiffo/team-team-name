package com.teamteamname.gotogothenburg.api.electricity.handlers;

import com.teamteamname.gotogothenburg.api.Stops;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface NextStopHandler extends ErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param nextStop
     * The next stop for a bus.
     */
    void electricityNextStopResponse(Stops nextStop);
}
