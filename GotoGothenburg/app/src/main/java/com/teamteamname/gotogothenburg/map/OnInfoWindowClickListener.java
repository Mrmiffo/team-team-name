package com.teamteamname.gotogothenburg.map;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikLocation;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikTripHandler;
import com.teamteamname.gotogothenburg.destination.Destination;
import com.teamteamname.gotogothenburg.destination.SavedDestinations;

/**
 * Listener for deciding what should happen when the user clicks on a markers info window
 */
class OnInfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener {
    private IMap map;
    private VasttrafikTripHandler tripHandler;
    private VasttrafikErrorHandler errorHandler;
    private Context context;

    /**
     * Creates a new OnInfoWindowClick Listener
     *
     * @param map Map on which info window is shown
     * @param tripHandler TripHandler to call should directions be requested
     * @param errorHandler ErrorHandler should directions request go wrong
     * @param context Context for displaying toasts
     */
    public OnInfoWindowClickListener(IMap map, VasttrafikTripHandler tripHandler, VasttrafikErrorHandler errorHandler, Context context) {
        this.map = map;
        this.tripHandler = tripHandler;
        this.errorHandler = errorHandler;
        this.context = context;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.equals(map.getUserMarker())) {
            SavedDestinations.getInstance().addDestination(new Destination(marker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude));
            marker.remove();
            Toast.makeText(context, "Destination added", Toast.LENGTH_SHORT).show();
        } else if(!"Directions".equals(marker.getSnippet())) {
            return;
        } else {
            Location myLocation = LocationServicesAPI.getInstance().getLastKnownLocation();
            if (myLocation != null) {
                VasttrafikAPI.getInstance().getTrip(
                        tripHandler,
                        errorHandler,
                        new VasttrafikLocation("origin", myLocation.getLatitude(), myLocation.getLongitude()),
                        new VasttrafikLocation(marker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude));
            } else {
                Toast.makeText(context,"Device Location not found",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
