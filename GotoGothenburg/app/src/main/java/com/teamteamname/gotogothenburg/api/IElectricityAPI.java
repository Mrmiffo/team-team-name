package com.teamteamname.gotogothenburg.api;

import com.teamteamname.gotogothenburg.map.Bus;

/**
 * Created by Olof on 25/09/2015.
 */
public interface IElectricityAPI {

    // Returns the location of the given bus.
    // Sensor updates each 5 sec.
    public void getBusLocation(Bus bus, ElectricityHandler callback);

    // Returns the next stop for the given bus.
    // Sensor updates each 10 sec.
    public void getNextStop(Bus bus, ElectricityHandler callback);

    // Returns the temperature outside of the given bus.
    // Sensor updates each 10 sec.
    public void getAmbientTemperature(Bus bus, ElectricityHandler callback);

    // Returns the temperature inside the DRIVER'S cabin of the given bus.
    // Sensor updates each 2,5 min.
    public void getCabinTemperature(Bus bus, ElectricityHandler callback);

    // Returns whether the stop button is pressed for the given bus.
    // Sensor updates each time someone presses the stop button.
    public void getStopPressed(Bus bus, ElectricityHandler callback);

    // Returns the total number of users connected to the Wifi on the given bus.
    // Sensor updates each 12 sec.
    public void  getNbrOfWifiUsers(Bus bus, ElectricityHandler callback);

}
