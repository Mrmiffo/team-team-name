package com.teamteamname.gotogothenburg.api;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface ElectricityStopButtonHandler extends ElectricityErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param isPressed
     * Tells whether the stop button is pressed or not on a bus.
     */
    void electricityStopPressedResponse(boolean isPressed);
}
