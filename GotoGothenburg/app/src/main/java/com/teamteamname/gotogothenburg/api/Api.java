package com.teamteamname.gotogothenburg.api;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Olof on 28/10/2015.
 */
public class Api {

    private static IApiFactory factory;

    public static void init(Activity activity){
        factory = new ApiFactory(activity);
        // initialized boolean för att undvika nullpointer vid senare factory anrop?
        // I sådana fall borde även metoderna return boolean som säger om de genomfördes eller ej, beroende på om den är initialized.
        // Kanske inte behövs... Vette fan. Säger ju inte så jävla mycket... kanske borde skicka ett exception istället. mm troligtvis.
    }

    public static void getBusLocation(Bus bus, GPSHandler callback){
        IGetBusLocation requester = factory.createIGetBusLocation();
        requester.getBusLocation(bus,callback);
    }

    public static void getNextStop(Bus bus, NextStopHandler callback){
        IGetNextStop requester = factory.createIGetNextStop();
        requester.getNextStop(bus, callback);
    }

    public static void getAmbientTemp(Bus bus, TempHandler callback){
        IGetAmbientTemp requester = factory.createIGetAmbientTemp();
        requester.getAmbientTemperature(bus, callback);
    }

    public static void getCabinTemp(Bus bus, TempHandler callback){
        IGetCabinTemp requester = factory.createIGetCabinTemp();
        requester.getCabinTemperature(bus, callback);
    }

    public static void getStopPressed(Bus bus, StopButtonHandler callback){
        IGetStopPressed requester = factory.createIGetStopPressed();
        requester.getStopPressed(bus, callback);
    }

    public static void getNbrOfWifiUsers(Bus bus, WifiHandler callback){
        IGetNbrOfWifiUsers requester = factory.createIGetNbrOfWifiUsers();
        requester.getNbrOfWifiUsers(bus, callback);
    }

    public static void getAutocomplete(AutocompleteHandler autoCallback, ErrorHandler errorCallback, String input){
        IAutocomplete requester = factory.createIAutocomplete();
        requester.getAutocomplete(autoCallback, errorCallback, input);
    }

    public static void getTrip(TripHandler tripCallback, ErrorHandler errorCallback, String originName, LatLng originCoords, String destName, LatLng destCoords){
        ITrip requester = factory.createITrip();
        requester.getTrip(tripCallback,errorCallback,originName,originCoords,destName,destCoords);
    }
}
