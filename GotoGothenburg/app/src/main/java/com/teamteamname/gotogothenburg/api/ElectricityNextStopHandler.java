package com.teamteamname.gotogothenburg.api;

import com.teamteamname.gotogothenburg.Stops;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface ElectricityNextStopHandler extends ElectricityErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param nextStop
     * The next stop for a bus.
     */
    void electricityNextStopResponse(Stops nextStop);
}
