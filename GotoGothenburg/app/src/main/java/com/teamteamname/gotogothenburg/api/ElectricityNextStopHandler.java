package com.teamteamname.gotogothenburg.api;

import com.teamteamname.gotogothenburg.Stops;

/**
 * Created by Olof on 06/10/2015.
 */
public interface ElectricityNextStopHandler extends ElectricityErrorHandler {
    public void electricityNextStopResponse(Stops nextStop);
}
