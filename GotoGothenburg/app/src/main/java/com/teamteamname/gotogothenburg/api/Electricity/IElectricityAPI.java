package com.teamteamname.gotogothenburg.api.Electricity;

import com.teamteamname.gotogothenburg.api.Electricity.ElectricityGPSHandler;
import com.teamteamname.gotogothenburg.api.Electricity.ElectricityNextStopHandler;
import com.teamteamname.gotogothenburg.api.Electricity.ElectricityStopButtonHandler;
import com.teamteamname.gotogothenburg.api.Electricity.ElectricityTempHandler;
import com.teamteamname.gotogothenburg.api.Electricity.ElectricityWifiHandler;
import com.teamteamname.gotogothenburg.map.Bus;

/**
 * A interface for requesting information from Electricity's REST API.
 *
 */
public interface IElectricityAPI {

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
     void getAmbientTemperature(Bus bus, ElectricityTempHandler callback);

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
     void getCabinTemperature(Bus bus, ElectricityTempHandler callback);

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

    /**
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
     void  getNbrOfWifiUsers(Bus bus, ElectricityWifiHandler callback);

}
