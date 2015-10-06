package com.teamteamname.gotogothenburg.api;

import com.teamteamname.gotogothenburg.GPSCoord;

/**
 * Created by Olof on 06/10/2015.
 */
public interface ElectricityGPSHandler extends ElectricityErrorHandler {
    public void electricityGPSResponse(GPSCoord coord);
}
