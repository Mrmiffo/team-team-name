package com.teamteamname.gotogothenburg.api;

import android.app.Activity;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by Olof on 28/10/2015.
 */
public class ApiFactory implements IApiFactory {

    private ElectricityAPI electricityAPI;
    private VasttrafikAPI vasttrafikAPI;
    private LocationServicesAPI locationServicesAPI;
    private ElectriCityWiFiSystemIDAPI electriCityWiFiSystemIDAPI;
    private AndroidDeviceAPI androidDeviceAPI;

    public ApiFactory(Activity activity){
        // Initialize the APIs
        final Cache cache = new DiskBasedCache(activity.getCacheDir(),1024*1024);
        final Network network = new BasicNetwork(new HurlStack());
        final RequestQueue queue = new RequestQueue(cache,network);

        electricityAPI = new ElectricityAPI(activity.getApplicationContext(),queue);
        vasttrafikAPI = new VasttrafikAPI(activity.getApplicationContext(),queue);

        locationServicesAPI = new LocationServicesAPI(activity);

        electriCityWiFiSystemIDAPI = new ElectriCityWiFiSystemIDAPI();

        androidDeviceAPI = new AndroidDeviceAPI(activity.getApplicationContext());

        queue.start();
    }

    @Override
    public IGetBusLocation createIGetBusLocation() {
        return electricityAPI;
    }

    @Override
    public IGetNextStop createIGetNextStop() {
        return electricityAPI;
    }

    @Override
    public IGetAmbientTemp createIGetAmbientTemp() {
        return electricityAPI;
    }

    @Override
    public IGetCabinTemp createIGetCabinTemp() {
        return electricityAPI;
    }

    @Override
    public IGetStopPressed createIGetStopPressed() {
        return electricityAPI;
    }

    @Override
    public IGetNbrOfWifiUsers createIGetNbrOfWifiUsers() {
        return electricityAPI;
    }

    @Override
    public IAutocomplete createIAutocomplete() {
        return vasttrafikAPI;
    }

    @Override
    public ITrip createITrip() {
        return vasttrafikAPI;
    }

    @Override
    public ILocationServices createILocationServices() {
        return locationServicesAPI;
    }

    @Override
    public IElectriCityWiFiSystemIDAPI createIWiFiSystemSystemIDAPI() {
        return electriCityWiFiSystemIDAPI;
    }

    @Override
    public IDeviceAPI createDeviceAPI() {
        return androidDeviceAPI;
    }
}
