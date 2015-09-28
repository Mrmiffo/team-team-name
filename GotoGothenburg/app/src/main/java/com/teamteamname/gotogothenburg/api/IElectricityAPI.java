package com.teamteamname.gotogothenburg.api;

import com.teamteamname.gotogothenburg.map.Bus;

/**
 * Created by Olof on 25/09/2015.
 */
public interface IElectricityAPI {

    public void getBusLocation(Bus bus, ElectricityHandler callback);

    public void getNextStop(Bus bus, ElectricityHandler callback);

    public void getAmbientTemperature(Bus bus, ElectricityHandler callback);

    public void getCabinTemperature(Bus bus, ElectricityHandler callback);

    public void getStopPressed(Bus bus, ElectricityHandler callback);

    public void getNbrOfWifiUsers(Bus bus, ElectricityHandler callback);

}
