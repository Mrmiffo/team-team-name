package com.teamteamname.gotogothenburg.map;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by patrick on 15/10/2015.
 */
public class HotelPoints implements IMapMarkerData{

    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<MarkerOptions> markerOptions = new ArrayList<>();

    /**
     * Creates a new HotelPoints class
     */
    public HotelPoints(){
        markerOptions.add(new MarkerOptions()
                .title("Hotel Novotel Göteborg")
                .position(new LatLng(57.690417, 11.908392))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        markerOptions.add(new MarkerOptions()
                .title("Radisson Blu Riverside Hotel Gothenburg")
                .position(new LatLng(57.707353, 11.940783))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        markerOptions.add(new MarkerOptions()
                .title("Quality Hotel Panorama")
                .position(new LatLng(57.689104, 11.988182))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        markerOptions.add(new MarkerOptions()
                .title("Elite Park Avenue")
                .position(new LatLng(57.698186, 11.978569))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        markerOptions.add(new MarkerOptions()
                .title("Hotell Onyxen")
                .position(new LatLng(57.699287, 11.985607))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        markerOptions.add(new MarkerOptions()
                .title("Avalon Hotel")
                .position(new LatLng(57.704028, 11.968183))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        markerOptions.add(new MarkerOptions()
                .title("Hotel Royal")
                .position(new LatLng(57.706384, 11.972218))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        markerOptions.add(new MarkerOptions()
                .title("Scandic Europa")
                .position(new LatLng(57.708092, 11.970233))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        markerOptions.add(new MarkerOptions()
                .title("First Hotel G")
                .position(new LatLng(57.709158, 11.972518))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        markerOptions.add(new MarkerOptions()
                .title("Good Morning Göteborg City")
                .position(new LatLng(57.716196, 11.972904))
                .snippet("Directions")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
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
