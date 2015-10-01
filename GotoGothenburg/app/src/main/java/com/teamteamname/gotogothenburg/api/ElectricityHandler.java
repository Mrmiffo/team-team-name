package com.teamteamname.gotogothenburg.api;


import com.teamteamname.gotogothenburg.GPSCoord;

/**
 * Created by Olof on 22/09/2015.
 */
public interface ElectricityHandler {
    public void electricityRequestDone(GPSCoord coord);

    public void electricityRequestError(String error);
}
