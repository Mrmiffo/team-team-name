package com.teamteamname.gotogothenburg.map;

import android.util.TypedValue;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.destination.Destination;
import com.teamteamname.gotogothenburg.destination.RecommendedDestinations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 27/10/2015.
 * Listener for displaying recommended destinations on a map
 * Possible extension/development: Consider using dynamic data from internet API or similar.
 */
class ShowRecommendedDestinationsListener implements View.OnClickListener {

    private IMap map;
    private List<Marker> markers = new ArrayList<>();
    private boolean isDisplaying;

    /**
     * Creates a new ShowRecommendedDestinationsListener
     *
     * @param map Map which recommended destinations will be drawn on
     */
    public ShowRecommendedDestinationsListener(IMap map) {
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
            for (Destination dest : RecommendedDestinations.getInstance().getRecommendedDestinations()) {
                MarkerOptions marker = new MarkerOptions();
                marker.position(new LatLng(dest.getLatitude(), dest.getLongitude()));
                marker.title(dest.getName());
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                marker.snippet("directions");
                markers.add(map.placeMarker(marker));
            }
            v.getResources().getValue(R.dimen.BUTTON_PRESSED_ALPHA, value, true);
        }
        v.setAlpha(value.getFloat());
        isDisplaying = !isDisplaying;
    }
}
