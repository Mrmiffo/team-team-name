package com.teamteamname.gotogothenburg.api;

/**
 * Created by Olof on 27/10/2015.
 */
public interface IGetCabinTemp {
    /**
     * Requests the temperature inside the DRIVER'S cabin of a specific bus. The response is sent back to the
     * requester using the callback parameter in the form of a double.
     *
     * Sensor updates each 2,5 min.
     *
     * @param bus
     * Specifies the bus.
     * @param callback
     * A interface for recieving the response.
     */
    void getCabinTemperature(Bus bus, TempHandler callback);
}
