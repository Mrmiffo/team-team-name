package com.teamteamname.gotogothenburg.api.electricity.handlers;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface ElectricityTempHandler extends ElectricityErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param temp
     * Temperature outside of a bus.
     */
    void electricityAmbientTemperatureResponse(double temp);

    /**
     * Callback method for the Electricity API.
     * @param temp
     * Temperature inside the driver's cabin of a bus.
     */
    void electricityCabinTemperature(double temp);
}
