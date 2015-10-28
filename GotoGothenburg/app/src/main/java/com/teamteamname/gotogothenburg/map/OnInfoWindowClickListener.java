package com.teamteamname.gotogothenburg.map;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teamteamname.gotogothenburg.api.Api;
import com.teamteamname.gotogothenburg.api.ErrorHandler;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.TripHandler;
import com.teamteamname.gotogothenburg.destination.Destination;
import com.teamteamname.gotogothenburg.destination.SavedDestinations;

/**
 * Listener for deciding what should happen when the user clicks on a markers info window
 */
class OnInfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener {
    final private IMap map;
    final private TripHandler tripHandler;
    final private ErrorHandler errorHandler;
    final private Context context;

    /**
     * Creates a new OnInfoWindowClick Listener
     *
     * @param map Map on which info window is shown
     * @param tripHandler TripHandler to call should directions be requested
     * @param errorHandler ErrorHandler should directions request go wrong
     * @param context Context for displaying toasts
     */
    public OnInfoWindowClickListener(IMap map, TripHandler tripHandler, ErrorHandler errorHandler, Context context) {
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
        } else if("Directions".equals(marker.getSnippet())) {
            final Location myLocation = LocationServicesAPI.getInstance().getLastKnownLocation();
            if (myLocation != null) {
                final LatLng originCoord = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                final LatLng destCoord = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                Api.getTrip(
                        tripHandler, errorHandler, "Me", originCoord, marker.getTitle(), destCoord);
            }
        }
    }
}
