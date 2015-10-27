package com.teamteamname.gotogothenburg.api.electricity.handlers;

/**
 * A interface for recieving error messages from Electricity's REST API.
 */
public interface ErrorHandler {
    /**
     * Method for recieving a error.
     * @param error
     */
    void electricityRequestError(ApiRequestError error);
}
