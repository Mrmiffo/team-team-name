package com.teamteamname.gotogothenburg.api;

import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityNextStopHandler;

/**
 * Created by Olof on 27/10/2015.
 */
public interface IGetNextStop {
    /**
     * Requests the next stop for a specific bus. The response is sent back to a requester using the
     * callback parameter in the form of a Stops enum.
     *
     * Sensor updates each 10 sec.
     *
     * @param bus
     * Specifies the bus.
     * @param callback
     * A interface for recieving the response.
     */
    void getNextStop(Bus bus, ElectricityNextStopHandler callback);
}
