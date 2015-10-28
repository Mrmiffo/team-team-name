package com.teamteamname.gotogothenburg.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamteamname.gotogothenburg.R;

import java.io.IOException;
import java.util.List;

/**
 * Listener for displaying a marker where the user clicks on the map
 */
class OnMapLongClickListener implements GoogleMap.OnMapLongClickListener {

    final private IMap map;
    final private Context context;

    /**
     * Creates a new OnMapLongClick Listener
     *
     * @param map Map on which marker will be drawn
     * @param context Context for which toast will be displayed when no internet connection is found
     */
    public OnMapLongClickListener(IMap map, Context context) {
        this.map = map;
        this.context = context;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        final Geocoder geocoder = new Geocoder(context);
        if (map.getUserMarker() != null) {
            map.getUserMarker().remove();
        }

        try {
            final List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = null;
            if (!addressList.isEmpty()) {
                address = addressList.get(0).getAddressLine(0);
            }
            map.setUserMarker(map.placeMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_drop_black_48dp))
                    .title("".equals(address) ? latLng.toString() : address)
                    .snippet("Create Destination"))
            );
        } catch (IOException e) {
            Toast.makeText(context, "No network connection", Toast.LENGTH_SHORT).show();
        }
    }
}
