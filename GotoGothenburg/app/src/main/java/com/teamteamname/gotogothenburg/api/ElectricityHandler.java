package com.teamteamname.gotogothenburg.api;


import com.teamteamname.gotogothenburg.GPSCoord;

/**
 * Created by Olof on 22/09/2015.
 */
public interface ElectricityHandler {
    public void electricityGPSResponse(GPSCoord coord);

    public void electricityNextStopResponse(String nextStop);

    public void electricityAmbientTemperatureResponse(double temp);

    public void electricityCabinTemperature(double temp);

    public void electricityStopPressedResponse(boolean isPressed);

    public void electricityWifiUsersResponse(int nbrOfUsers);

    public void electricityRequestError(String error);
}
