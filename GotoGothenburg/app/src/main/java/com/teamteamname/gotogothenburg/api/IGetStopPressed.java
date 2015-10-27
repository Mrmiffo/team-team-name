package com.teamteamname.gotogothenburg.api;

import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityStopButtonHandler;

/**
 * Created by Olof on 27/10/2015.
 */
public interface IGetStopPressed {
    /**
     * Requests information about whether the stop button is pressed or not on a specific bus. The response is sent
     * back to the requester using the callback parameter in the form of a boolean.
     *
     * Sensor updates event-vise (each time someone presses the button).
     *
     * @param bus
     * Specifies the bus.
     * @param callback
     * A interface for recieving the response.
     */
    void getStopPressed(Bus bus, ElectricityStopButtonHandler callback);
}
