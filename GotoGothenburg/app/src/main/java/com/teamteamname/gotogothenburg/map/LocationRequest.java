package com.teamteamname.gotogothenburg.map;

/**
 * Created by patrick on 28/09/2015.
 * The LocationRequest used for location updates.
 * Since it is used for marking the devices location on a map, hight priority and a low interval is used.
 */
public class LocationRequest {
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
