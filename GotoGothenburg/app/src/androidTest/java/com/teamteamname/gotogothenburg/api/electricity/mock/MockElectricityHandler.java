package com.teamteamname.gotogothenburg.api.electricity.mock;

import com.teamteamname.gotogothenburg.api.GPSCoord;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ApiRequestError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.GPSHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.NextStopHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.StopButtonHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.TempHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.WifiHandler;
import com.teamteamname.gotogothenburg.api.Stops;

import lombok.Getter;

/**
 * A mockclass of respoonsehandlers for ElectricityAPI. Catches responses. Used to test whether the response is correct.
 */
public class MockElectricityHandler implements WifiHandler, StopButtonHandler, TempHandler, NextStopHandler, GPSHandler {

    @Getter private GPSCoord gpsResponse;
    @Getter private Stops nextStopResponse;
    @Getter private double ambientTempResponse;
    @Getter private double cabinTempResponse;
    @Getter private boolean isPressedResponse;
    @Getter private int nbrOfUsersResponse;
    @Getter private ApiRequestError errorResponse;

    @Override
    public void electricityGPSResponse(GPSCoord coord) {
        gpsResponse = coord;
    }

    @Override
    public void electricityNextStopResponse(Stops nextStop) {
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
    public void electricityRequestError(ApiRequestError error) {
        errorResponse = error;
    }
}
