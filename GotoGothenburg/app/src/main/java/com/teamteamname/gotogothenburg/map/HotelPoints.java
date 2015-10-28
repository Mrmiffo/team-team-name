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
public class HotelPoints implements IMapMarkerData{

    final private List<Marker> markers = new ArrayList<>();
    final private List<MarkerOptions> markerOptions = new ArrayList<>();

    /**
     * Creates a new HotelPoints class
     */
    public HotelPoints(){
        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("Hotel Novotel Göteborg")
                .position(new LatLng(57.690417, 11.908392))));


        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("Radisson Blu Riverside Hotel Gothenburg")
                .position(new LatLng(57.707353, 11.940783))));


        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("Quality Hotel Panorama")
                .position(new LatLng(57.689104, 11.988182))));

        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("Elite Park Avenue")
                .position(new LatLng(57.698186, 11.978569))));

        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("Hotell Onyxen")
                .position(new LatLng(57.699287, 11.985607))));

        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("Avalon Hotel")
                .position(new LatLng(57.704028, 11.968183))));

        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("Hotel Royal")
                .position(new LatLng(57.706384, 11.972218))));

        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("Scandic Europa")
                .position(new LatLng(57.708092, 11.970233))));

        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("First Hotel G")
                .position(new LatLng(57.709158, 11.972518))));

        markerOptions.add(addExtraInfo(new MarkerOptions()
                .title("Good Morning Göteborg City")
                .position(new LatLng(57.716196, 11.972904))));

    }

    private MarkerOptions addExtraInfo(MarkerOptions m) {
        return m.snippet("Directions").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
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
