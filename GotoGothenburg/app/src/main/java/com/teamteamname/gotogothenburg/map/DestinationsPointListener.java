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
import java.util.List;

/**
 * Listener for displaying destinations on the map
 */
class DestinationsPointListener implements View.OnClickListener {

    final private IMap map;
    private boolean isDisplaying;
    final private List<Marker> markers = new ArrayList<>();

    public DestinationsPointListener(IMap map) {
        this.map = map;
    }

    @Override
    public void onClick(View v) {
        final TypedValue value = new TypedValue();
        if (isDisplaying) {
            for (final Marker marker : markers) {
                marker.remove();
            }
            v.getResources().getValue(R.dimen.BUTTON_UNPRESSED_ALPHA, value, true);
        } else {
            for (final Destination dest : SavedDestinations.getInstance().getSavedDestinations()) {
                final MarkerOptions marker = new MarkerOptions();
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
