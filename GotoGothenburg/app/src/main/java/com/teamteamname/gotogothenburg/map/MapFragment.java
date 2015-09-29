package com.teamteamname.gotogothenburg.map;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * The fragment used to display the map in the application.
 * Created by Anton on 2015-09-21.
 */
public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    // Request code to use when launching the resolution activity
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    public static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private GoogleMap map;
    private GoogleApiClient locationsAPI;
    private Marker myPosition;

    public static MapFragment newInstance(){
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Let google maps know we want the handle the map and connect to locations api
        getMapAsync(this);
        locationsAPI = new GoogleApiClient.Builder(getActivity())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        if(!mResolvingError) {
            locationsAPI.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (locationsAPI.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(locationsAPI, LocationRequest.getLocationRequest(), this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationsAPI.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(locationsAPI, this);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        locationsAPI.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(locationsAPI);
        LocationServices.FusedLocationApi.requestLocationUpdates(locationsAPI, LocationRequest.getLocationRequest(), this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("Connection suspended", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(mResolvingError) {
            return;
        }

        if(connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                Log.e("Intent Exception", e.getMessage());
                locationsAPI.connect();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /**
     * Call when dialog has been dismissed. Tells MapFragment that error is no longer being resolved.
     */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /**
     * Call to report result from error resolving. Most likely call from onActivityResult,
     *
     * @param resultCode    The result code
     * @param data  Intent with data-
     */
    public void resolutionResult (int resultCode, Intent data) {
        mResolvingError = false;
        if (resultCode == Activity.RESULT_OK) {
            if (!locationsAPI.isConnecting() && !locationsAPI.isConnected()) {
                locationsAPI.connect();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        markCurrentPosition(location);
    }

    // Place marker on devices' current position
    private void markCurrentPosition(Location location) {
        if(myPosition != null) {
            myPosition.remove();
        }
        myPosition = map.addMarker( new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("I am here"));
    }
}
