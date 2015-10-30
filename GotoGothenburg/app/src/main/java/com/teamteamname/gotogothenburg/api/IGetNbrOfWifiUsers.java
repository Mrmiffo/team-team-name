package com.teamteamname.gotogothenburg.api;

/**
 * Created by Olof on 27/10/2015.
 */
public interface IGetNbrOfWifiUsers {
    /**
     * This method is implemented but isn't used in the current applications version. It still exists in the code for use in further development.
     *
     * Requests the number of devices connected to the Wifi of a specific bus. The users does not need to be
     * authenticated (does not have to have accepted the Terms of Use). The response is sent back to the
     * requester using the callback parameter in the form of a integer.
     *
     * Sensor updates each 12 sec.
     *
     * @param bus
     * Specifies the bus.
     * @param callback
     * A interface for recieving the response.
     */
    void  getNbrOfWifiUsers(Bus bus, WifiHandler callback);
}
