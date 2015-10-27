package com.teamteamname.gotogothenburg.api;

import com.teamteamname.gotogothenburg.api.electricity.handlers.TempHandler;

/**
 * Created by Olof on 27/10/2015.
 */
public interface IGetAmbientTemp {
    /**
     * Requests the temperature outside of a specific bus. The response is sent back to a requester using the
     * callback parameter in the form of a double.
     *
     * Sensor updates each 10 sec.
     *
     * @param bus
     * Specifies the bus.
     * @param callback
     * A interface for recieving the response.
     */
    void getAmbientTemperature(Bus bus, TempHandler callback);
}
