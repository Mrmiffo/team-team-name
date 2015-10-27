package com.teamteamname.gotogothenburg.api.electricity.handlers;

import com.teamteamname.gotogothenburg.api.GPSCoord;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface ElectricityGPSHandler extends ElectricityErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param coord
     * The GPS location of a bus.
     */
    void electricityGPSResponse(GPSCoord coord);
}
