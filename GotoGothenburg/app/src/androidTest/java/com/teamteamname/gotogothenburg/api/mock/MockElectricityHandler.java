package com.teamteamname.gotogothenburg.api.mock;

import com.teamteamname.gotogothenburg.GPSCoord;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityGPSHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityNextStopHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityStopButtonHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityTempHandler;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityWifiHandler;
import com.teamteamname.gotogothenburg.route.Stops;

import lombok.Getter;

/**
 * A mockclass of respoonsehandlers for ElectricityAPI. Catches responses. Used to test whether the response is correct.
 */
public class MockElectricityHandler implements ElectricityWifiHandler, ElectricityStopButtonHandler, ElectricityTempHandler, ElectricityNextStopHandler, ElectricityGPSHandler {

    @Getter private GPSCoord gpsResponse;
    @Getter private Stops nextStopResponse;
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
    public void electricityRequestError(String error) {
        errorResponse = error;
    }
}
