package com.teamteamname.gotogothenburg.api;

/**
 * Created by Olof on 06/10/2015.
 */
public interface ElectricityTempHandler extends ElectricityErrorHandler {
    public void electricityAmbientTemperatureResponse(double temp);

    public void electricityCabinTemperature(double temp);
}
