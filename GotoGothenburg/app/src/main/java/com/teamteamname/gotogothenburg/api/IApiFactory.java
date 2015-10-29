package com.teamteamname.gotogothenburg.api;

/**
 * A interface defining which API-interfaces possible to ask for. Returns interfaces defining possible interactions with the APIs.
 * Created by Olof on 28/10/2015.
 */
public interface IApiFactory {
    IGetBusLocation createIGetBusLocation();

    IGetNextStop createIGetNextStop();

    IGetAmbientTemp createIGetAmbientTemp();

    IGetCabinTemp createIGetCabinTemp();

    IGetStopPressed createIGetStopPressed();

    IGetNbrOfWifiUsers createIGetNbrOfWifiUsers();

    IAutocomplete createIAutocomplete();

    ITrip createITrip();

    ILocationServices createILocationServices();

    IElectriCityWiFiSystemIDAPI createIWiFiSystemSystemIDAPI();

    IDeviceAPI createDeviceAPI();

}
