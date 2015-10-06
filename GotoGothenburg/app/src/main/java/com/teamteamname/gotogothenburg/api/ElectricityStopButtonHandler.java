package com.teamteamname.gotogothenburg.api;

/**
 * Created by Olof on 06/10/2015.
 */
public interface ElectricityStopButtonHandler extends ElectricityErrorHandler {
    public void electricityStopPressedResponse(boolean isPressed);
}
