package com.teamteamname.gotogothenburg.api.electricity.handlers;

/**
 * A interface for recieving error messages from Electricity's REST API.
 */
public interface ElectricityErrorHandler {
    /**
     * Method for recieving a error.
     * @param error
     */
    void electricityRequestError(ElectricityError error);
}
