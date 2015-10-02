package com.teamteamname.gotogothenburg.api.mock;

import com.teamteamname.gotogothenburg.api.ElectricityHandler;
import com.teamteamname.gotogothenburg.GPSCoord;

import lombok.Getter;

/**
 * Created by Olof on 30/09/2015.
 */
public class MockElectricityHandler implements ElectricityHandler {

    @Getter private GPSCoord gpsResponse;
    @Getter private String nextStopResponse;
    @Getter private double ambientTempResponse;
    @Getter private double cabinTempResponse;
    @Getter private boolean isPressedResponse;
    @Getter private int nbrOfUsersResponse;
    @Getter private String errorResponse;

    @Override
    public void electricityGPSResponse(GPSCoord coord) {
        gpsResponse = coord;
    }

    @Override
    public void electricityNextStopResponse(String nextStop) {
        nextStopResponse = nextStop;
    }

    @Override
    public void electricityAmbientTemperatureResponse(double temp) {
        ambientTempResponse = temp;
    }

    @Override
    public void electricityCabinTemperature(double temp) {
        cabinTempResponse = temp;
    }

    @Override
    public void electricityStopPressedResponse(boolean isPressed) {
        isPressedResponse = isPressed;
    }

    @Override
    public void electricityWifiUsersResponse(int nbrOfUsers) {
        nbrOfUsersResponse = nbrOfUsers;
    }

    @Override
    public void electricityRequestError(String error) {
        errorResponse = error;
    }
}
