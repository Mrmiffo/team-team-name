package com.teamteamname.gotogothenburg.api;

import android.content.Intent;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

/**
 * Created by Olof on 28/10/2015.
 */
public interface ILocationServices extends GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    void connect();

    void disconnect();

    void registerLocationUpdateListener(LocationListener locationListener);

    void removeLocationUpdateListener(LocationListener locationListener);

    Location getLastKnownLocation();

    void resolutionResult (int resultCode, Intent data);

}
