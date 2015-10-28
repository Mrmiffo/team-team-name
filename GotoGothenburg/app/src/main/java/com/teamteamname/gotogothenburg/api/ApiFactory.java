package com.teamteamname.gotogothenburg.api;

import android.app.Activity;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.teamteamname.gotogothenburg.api.electricity.ElectricityAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.IAutocomplete;
import com.teamteamname.gotogothenburg.api.vasttrafik.ITrip;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikAPI;

/**
 * Created by Olof on 28/10/2015.
 */
public class ApiFactory implements IApiFactory {

    private ElectricityAPI electricityAPI;
    private VasttrafikAPI vasttrafikAPI;

    public ApiFactory(Activity activity){
        // Initialize the APIs
        final Cache cache = new DiskBasedCache(activity.getCacheDir(),1024*1024);
        final Network network = new BasicNetwork(new HurlStack());
        final RequestQueue queue = new RequestQueue(cache,network);

        electricityAPI = new ElectricityAPI(activity.getApplicationContext(),queue);
        vasttrafikAPI = new VasttrafikAPI(activity.getApplicationContext(),queue);
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
}
