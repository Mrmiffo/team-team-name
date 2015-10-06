package com.teamteamname.gotogothenburg.api;

/**
 * A interface for recieving the callback response from Electricity's REST API.
 */
public interface ElectricityTempHandler extends ElectricityErrorHandler {
    /**
     * Callback method for the Electricity API.
     * @param temp
     * Temperature outside of a bus.
     */
    public void electricityAmbientTemperatureResponse(double temp);

    /**
     * Callback method for the Electricity API.
     * @param temp
     * Temperature inside the driver's cabin of a bus.
     */
    public void electricityCabinTemperature(double temp);
}
