package com.teamteamname.gotogothenburg.api;

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
