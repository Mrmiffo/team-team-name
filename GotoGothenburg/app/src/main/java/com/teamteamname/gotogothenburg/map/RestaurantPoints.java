package com.teamteamname.gotogothenburg.map;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by patrick on 15/10/2015.
 */
public class RestaurantPoints implements IMapMarkerData{

    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<MarkerOptions> markerOptions = new ArrayList<>();

    /**
     * Creates a new RestaurantPoints class and loads the markerOptions
     */
    public RestaurantPoints() {
        markerOptions.add(new MarkerOptions()
                .title("Thörnströms kök")
                .position(new LatLng(57.694099, 11.977736))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        markerOptions.add(new MarkerOptions()
                .title("Koka")
                .position(new LatLng(57.698505, 11.965496))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        markerOptions.add(new MarkerOptions()
                .title("Incontro")
                .position(new LatLng(57.697347, 11.989038))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        markerOptions.add(new MarkerOptions()
                .title("Resturang 28+")
                .position(new LatLng(57.698115, 11.97402))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        markerOptions.add(new MarkerOptions()
                .title("Sjöbaren")
                .position(new LatLng(57.69839, 11.958572))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        markerOptions.add(new MarkerOptions()
                .title("Sjömagasinet")
                .position(new LatLng(57.691605, 11.909258))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        markerOptions.add(new MarkerOptions()
                .title("Resturang Nevera")
                .position(new LatLng(57.73404, 11.955871))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        markerOptions.add(new MarkerOptions()
                .title("Bon")
                .position(new LatLng(57.698008, 11.976943))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        markerOptions.add(new MarkerOptions()
                .title("Lilla tavernan")
                .position(new LatLng(57.692102, 11.950892))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
    }

    @Override
    public void addMarkers(IMap map) {
        for (MarkerOptions marker : markerOptions) {
            markers.add((map.placeMarker(marker)));
        }
    }

    @Override
    public void removeMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }
}
