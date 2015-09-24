package com.teamteamname.gotogothenburg.API;


import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Olof on 22/09/2015.
 */
public interface GPSCoordReceiver {

    public void sendGPSCoord(LatLng gpsCoord);
}
