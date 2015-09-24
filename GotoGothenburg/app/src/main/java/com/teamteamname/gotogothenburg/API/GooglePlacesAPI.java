package com.teamteamname.gotogothenburg.API;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-24.
 */
public class GooglePlacesAPI implements IGooglePlacesAPI, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient apiClient;
    private Context context;

    public GooglePlacesAPI(Context context){
        this.context = context;
    }

    @Override
    public List<String> autoComplete(String s) {
        apiClient = new GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
