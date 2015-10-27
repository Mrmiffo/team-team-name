package com.teamteamname.gotogothenburg.api;

import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityGPSHandler;

/**
 * Created by Olof on 27/10/2015.
 */
public interface IGetBusLocation {

    /**
     * Requests the GPS location of a specific bus. The response is sent back to a requester using the
     * callback parameter in the form of a GPSCoord.
     *
     * Sensor updates each 5 sec.
     *
     * @param bus
     * Specifies the bus.
     * @param callback
     * A interface for recieving the response.
     */
    void getBusLocation(Bus bus, ElectricityGPSHandler callback);
}
