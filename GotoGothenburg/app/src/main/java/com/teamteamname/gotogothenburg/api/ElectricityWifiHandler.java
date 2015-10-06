package com.teamteamname.gotogothenburg.api;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface ElectricityWifiHandler extends ElectricityErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param nbrOfUsers
     * The number of (non-authenticated) users connetcted to the Wifi of a bus.
     */
    public void electricityWifiUsersResponse(int nbrOfUsers);
}
