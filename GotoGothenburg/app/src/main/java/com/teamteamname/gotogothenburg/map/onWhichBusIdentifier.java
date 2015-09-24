package com.teamteamname.gotogothenburg.map;

import com.teamteamname.gotogothenburg.API.IAPIHandler;

/**
 * Created by Anton on 2015-09-24.
 */
class onWhichBusIdentifier {
    private IAPIHandler api;
    private static final String ELECTRICITY_WIFI_SSID = "ElectriCity";

    protected onWhichBusIdentifier(IAPIHandler apiHandler){
        api = apiHandler;
    }
    /**
     * A method that first checks the MAC address of the Wifi the user is connected to and compares
     * it to the available buses.
     * @return the buss the user is on, or null if none was found.
     */
    protected Bus identifyBus(){

        //Checks if the device is connected to the Electricity wifi, if not
        if (!api.connectedToWifi(ELECTRICITY_WIFI_SSID) && api.connectToOpenWifi(ELECTRICITY_WIFI_SSID)) {
            String routerMAC = api.getWiFiRouterMAC();
            return Bus.getBusByMac(routerMAC);
        } else return null;



    }
}
