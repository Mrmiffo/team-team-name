package com.teamteamname.gotogothenburg.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.teamteamname.gotogothenburg.R;

/**
 * Custom InfoWindowAdapter for displaying custom marker InfoWindows
 * Uses snippet text for deciding what type of InfoWindow to display
 */
class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {return null;}

    @Override
    public View getInfoContents(Marker marker) {
        if ("Create Destination".equalsIgnoreCase(marker.getSnippet())) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_add_destinations_marker_info_window_layout ,null);
            ((Button)v.findViewById(R.id.info_window_button)).setText(marker.getTitle());
            return v;
        } else if ("Directions".equalsIgnoreCase(marker.getSnippet())) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_directions_marker_info_window_layout, null);
            ((Button)v.findViewById(R.id.info_window_button)).setText(marker.getTitle());
            return v;
        } else {
            return null;
        }
    }
}
