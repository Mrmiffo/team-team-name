package com.teamteamname.gotogothenburg.api;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * LocationServicesAPI exposes functionality for accessing the devices' location and registering listeners for
 * updates on when the location changes.
 *
 * Created by patrick on 06/10/2015.
 */
public class LocationServicesAPI implements ILocationServices {

    // The activity which is responsible for this api
    private final Activity activity;
    // Request code to use when launching the resolution activity
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    public static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private static boolean mResolvingError;
    // Reference to the api
    private GoogleApiClient api;
    // List of listeners to register when api is connected
    final private List<LocationListener> locationListeners = new ArrayList<>();

    public LocationServicesAPI(Activity activity) {
        this.activity = activity;
        connect();
    }

    /**
     * Unless the api is currently in a state of resolving an error then this method connects
     * the api
     */
    @Override
    public void connect() {
        if(api == null) {
            api = new GoogleApiClient.Builder(activity)
                    .addOnConnectionFailedListener(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if(!mResolvingError) {
            api.connect();
        }
    }

    /**
     * Disconnects the api
     */
    public void disconnect() {
        api.disconnect();
    }

    /**
     * Adds a LocationLister to the location updates subscription list
     *
     * @param locationListener A location listener to register for updates
     */
    public void registerLocationUpdateListener(LocationListener locationListener) {
        if(api.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(api, LocationRequest.getLocationRequest(), locationListener);
        } else {
            locationListeners.add(locationListener);
        }
    }

    /**
     * Removes provided LocationListener from listening to location updates.
     *
     * @param locationListener A location Listener to remove
     */
    public void removeLocationUpdateListener(LocationListener locationListener){
        if(api.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(api, locationListener);
        }
    }

    /**
     * Returns the last known location of the device
     *
     * @return A location object which represents the devices' current location
     */
    public Location getLastKnownLocation(){
        return LocationServices.FusedLocationApi.getLastLocation(api);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(!locationListeners.isEmpty()) {
            for(final LocationListener l : new ArrayList<>(locationListeners)) {
                registerLocationUpdateListener(l);
                locationListeners.remove(l);
            }
        }
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
                connectionResult.startResolutionForResult(activity, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                Log.e("Intent Exception", e.getMessage());
                api.connect();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        final ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(activity.getFragmentManager(), "errordialog");
    }

    /**
     * Call when dialog has been dismissed. Tells MapScreen that error is no longer being resolved.
     */
    private static void onDialogDismissed() {
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
                api.connect();
        }
    }

    /**
     * Created by patrick on 28/09/2015.
     * The LocationRequest used for location updates.
     * Since it is used for marking the devices location on a map, a high priority and a low interval is used.
     */
    private static class LocationRequest {
        private LocationRequest(){}
        // Consider setFastestInterval if updates happen to regularly
        /**
         * Gets the project specific LocationRequest.
         * Priority is High accuracy and interval is 5s.
         *
         * @return the LocationRequest
         */
        public static com.google.android.gms.location.LocationRequest getLocationRequest() {
            return com.google.android.gms.location.LocationRequest.create()
                    .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5000);
        }
    }

    /**
     * Created by patrick on 29/09/2015.
     * Dialog which displays the errors from LocationServices to the user.
     */
    public static class ErrorDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            LocationServicesAPI.onDialogDismissed();
        }
    }
}
