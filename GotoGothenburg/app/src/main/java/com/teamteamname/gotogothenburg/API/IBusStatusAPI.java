package com.teamteamname.gotogothenburg.API;

/**
 * An API which will read information from the wifi it's connected to and identify the wifi router details.
 * Created by Anton on 2015-10-01.
 */
public interface IBusStatusAPI {
    void getConnectedBusSystemID(IBusStatusHandler handler);
}
