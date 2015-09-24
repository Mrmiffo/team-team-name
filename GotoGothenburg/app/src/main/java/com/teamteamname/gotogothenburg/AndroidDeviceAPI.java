package com.teamteamname.gotogothenburg;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * The AndriodDeviceAPI is intended for any android phone with a GPS signal.
 * Created by Anton on 2015-09-23.
 */
public class AndroidDeviceAPI implements IDeviceAPI {
    private WifiInfo wifiInfo;

    /**
     * Constructor require a context in order to connect the API to the device.
     * @param context the main activity context
     */
    public AndroidDeviceAPI(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

    /**
     * Method used to get the MAC adress of the router. This is used to identify which bus the user is on.
     * @return null if the API has been created incorrectly or if the device is not connected to a Wifi.
     */
    @Override
    public String getWiFiRouterMAC(){
        if (wifiInfo == null){
            return null;
        } else {
            return wifiInfo.getBSSID();
        }
    }
}
