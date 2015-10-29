package com.teamteamname.gotogothenburg.api;

/**
 * Created by Olof on 27/10/2015.
 */
public interface IGetAmbientTemp {
    /**
     * This method is implemented but isn't used in the current applications version. It still exists in the code for use in further development.
     *
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
