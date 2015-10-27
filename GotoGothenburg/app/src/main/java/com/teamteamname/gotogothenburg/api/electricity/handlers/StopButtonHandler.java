package com.teamteamname.gotogothenburg.api.electricity.handlers;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface StopButtonHandler extends ErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param isPressed
     * Tells whether the stop button is pressed or not on a bus.
     */
    void electricityStopPressedResponse(boolean isPressed);
}
