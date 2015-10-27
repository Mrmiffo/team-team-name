package com.teamteamname.gotogothenburg.map;

import android.util.TypedValue;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.destination.Destination;
import com.teamteamname.gotogothenburg.destination.SavedDestinations;

import java.util.ArrayList;

/**
 * Listener for displaying destinations on the map
 */
class DestinationsPointListener implements View.OnClickListener {

    private IMap map;
    private boolean isDisplaying = false;
    private ArrayList<Marker> markers = new ArrayList<>();

    public DestinationsPointListener(IMap map) {
        this.map = map;
    }

    @Override
    public void onClick(View v) {
        TypedValue value = new TypedValue();
        if (isDisplaying) {
            for (Marker marker : markers) {
                marker.remove();
            }
            v.getResources().getValue(R.dimen.BUTTON_UNPRESSED_ALPHA, value, true);
        } else {
            for (Destination dest : SavedDestinations.getInstance().getSavedDestinations()) {
                MarkerOptions marker = new MarkerOptions();
                marker.position(new LatLng(dest.getLatitude(), dest.getLongitude()));
                marker.title(dest.getName());
                marker.snippet("Directions");
                markers.add(map.placeMarker(marker));
            }
            v.getResources().getValue(R.dimen.BUTTON_PRESSED_ALPHA, value, true);
        }
        v.setAlpha(value.getFloat());
        isDisplaying = !isDisplaying;
    }
}
