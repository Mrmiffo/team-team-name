package com.teamteamname.gotogothenburg.map;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 15/10/2015.
 */
public class RestaurantPoints implements IMapMarkerData{

    final private List<Marker> markers = new ArrayList<>();
    final private List<MarkerOptions> markerOptions = new ArrayList<>();

    /**
     * Creates a new RestaurantPoints class and loads the markerOptions
     */
    public RestaurantPoints() {
        markerOptions.add(addExtra(new MarkerOptions()
                .title("Thörnströms kök")
                .position(new LatLng(57.694099, 11.977736))));

        markerOptions.add(addExtra(new MarkerOptions()
                .title("Koka")
                .position(new LatLng(57.698505, 11.965496))));

        markerOptions.add(addExtra(new MarkerOptions()
                .title("Incontro")
                .position(new LatLng(57.697347, 11.989038))));

        markerOptions.add(addExtra(new MarkerOptions()
                .title("Resturang 28+")
                .position(new LatLng(57.698115, 11.97402))));

        markerOptions.add(addExtra(new MarkerOptions()
                .title("Sjöbaren")
                .position(new LatLng(57.69839, 11.958572))));

        markerOptions.add(addExtra(new MarkerOptions()
                .title("Sjömagasinet")
                .position(new LatLng(57.691605, 11.909258))));

        markerOptions.add(addExtra(new MarkerOptions()
                .title("Resturang Nevera")
                .position(new LatLng(57.73404, 11.955871))));

        markerOptions.add(addExtra(new MarkerOptions()
                        .title("Bon")
                        .position(new LatLng(57.698008, 11.976943))));

        markerOptions.add(addExtra(new MarkerOptions()
                    .title("Lilla tavernan")
                    .position(new LatLng(57.692102, 11.950892))));

    }

    private MarkerOptions addExtra(MarkerOptions m) {
        return m.snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
    }

    @Override
    public void addMarkers(IMap map) {
        for (final MarkerOptions marker : markerOptions) {
            markers.add(map.placeMarker(marker));
        }
    }

    @Override
    public void removeMarkers() {
        for (final Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }
}
