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

/**
 * LocationServicesAPI exposes functionality for accessing the devices' location and registering listeners for
 * updates on when the location changes.
 *
 * Created by patrick on 06/10/2015.
 */
public class LocationServicesAPI implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    // The singleton instance
    private static LocationServicesAPI instance;
    // The activity which is responsible for this api
    private Activity activity;
    // Request code to use when launching the resolution activity
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    public static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    // Reference to the api
    private GoogleApiClient api;

    /**
     * Initialize the api but doesn't connect it.
     * @param activity
     */
    public static void init(Activity activity) {
        instance = new LocationServicesAPI(activity);
    }

    /**
     * Gives a handle to the singleton api.
     * May return null should the api not be instantiated.
     *
     * @return the api instance
     */
    public static LocationServicesAPI getInstance() {
        return instance;
    }

    private LocationServicesAPI(Activity activity) {
        this.activity = activity;
        connect();
    }

    private void connect() {
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
    public void onConnected(Bundle bundle) {}

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
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(activity.getFragmentManager(), "errordialog");
    }

    /**
     * Call when dialog has been dismissed. Tells MapFragment that error is no longer being resolved.
     */
    private void onDialogDismissed() {
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
            if (!api.isConnecting() && !api.isConnected()) {
                api.connect();
            }
        }
    }

    /**
     * Created by patrick on 28/09/2015.
     * The LocationRequest used for location updates.
     * Since it is used for marking the devices location on a map, a high priority and a low interval is used.
     */
    private static class LocationRequest {
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
        public ErrorDialogFragment() {}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            LocationServicesAPI.getInstance().onDialogDismissed();
        }
    }
}
