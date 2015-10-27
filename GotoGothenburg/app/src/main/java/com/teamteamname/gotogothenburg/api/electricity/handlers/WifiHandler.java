package com.teamteamname.gotogothenburg.api.electricity.handlers;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface WifiHandler extends ErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param nbrOfUsers
     * The number of (non-authenticated) users connetcted to the Wifi of a bus.
     */
    void electricityWifiUsersResponse(int nbrOfUsers);
}
