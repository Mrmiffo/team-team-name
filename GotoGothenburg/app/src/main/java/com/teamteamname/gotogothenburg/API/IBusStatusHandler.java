package com.teamteamname.gotogothenburg.API;

/**
 * An interface implemented by the class which will receive the response from the IBusStatus API.
 * Created by Anton on 2015-10-01.
 */
public interface IBusStatusHandler {
    /**
     * A method to get the systemID of the wifi router on the bus which the user is connected to.
     * Only works on ElectriCity.
     * @param returnValue the SystemID of the wifi on the bus which the user is connected to.
     *                    Returns null if an error occur, such as an invalid wifi.
     */
    void getConnectedBusSystemIDCallback(String returnValue);
}
