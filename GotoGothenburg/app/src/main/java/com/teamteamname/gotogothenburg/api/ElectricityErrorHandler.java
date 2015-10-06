package com.teamteamname.gotogothenburg.api;

/**
 * A interface for recieving error messages from Electricity's REST API.
 */
public interface ElectricityErrorHandler {
    /**
     * Method for recieving a errormessage.
     * @param error
     */
    public void electricityRequestError(String error);
}
