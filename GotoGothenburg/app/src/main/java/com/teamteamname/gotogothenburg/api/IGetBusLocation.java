package com.teamteamname.gotogothenburg.api;

/**
 * Created by Olof on 27/10/2015.
 */
public interface IGetBusLocation {

    /**
     * This method is implemented but isn't used in the current applications version. It still exists in the code for use in further development.
     *
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
    void getBusLocation(Bus bus, GPSHandler callback);
}
