package com.teamteamname.gotogothenburg.api;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface GPSHandler extends ErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param coord
     * The GPS location of a bus.
     */
    void electricityGPSResponse(GPSCoord coord);
}
