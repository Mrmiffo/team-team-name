package com.teamteamname.gotogothenburg.api;

/**
 * Created by Olof on 28/10/2015.
 */
public interface IApiFactory {
    public IGetBusLocation createIGetBusLocation();

    public IGetNextStop createIGetNextStop();

    public IGetAmbientTemp createIGetAmbientTemp();

    public IGetCabinTemp createIGetCabinTemp();

    public IGetStopPressed createIGetStopPressed();

    public IGetNbrOfWifiUsers createIGetNbrOfWifiUsers();

    public IAutocomplete createIAutocomplete();

    public ITrip createITrip();
}
