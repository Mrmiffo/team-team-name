package com.teamteamname.gotogothenburg.map;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;


/**
 * The fragment used to display the map in the application.
 * Created by Anton on 2015-09-21.
 */
public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap map;
    private Marker myPosition;
    private boolean onLocationChangedHasRun;

    public static MapFragment newInstance(){
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Let google maps know we want the handle the map and connect to locations api
        getMapAsync(this);
        LocationServicesAPI.getInstance().registerLocationUpdateListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        LocationServicesAPI.getInstance().registerLocationUpdateListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationServicesAPI.getInstance().removeLocationUpdateListener(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        LocationServicesAPI.getInstance().removeLocationUpdateListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LocationServicesAPI.getInstance().removeLocationUpdateListener(this);
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setBuildingsEnabled(true);
    }



    @Override
    public void onLocationChanged(Location location) {
        if(map == null) {
            return;
        }
        markCurrentPosition(location);
        if (!onLocationChangedHasRun) {
            zoomToLocation(location, 15);
            onLocationChangedHasRun = true;
        }
    }

    private void zoomToLocation(Location location, float zoom){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
    }

    // Place marker on devices' current position
    private void markCurrentPosition(Location location) {
        if(myPosition != null) {
            myPosition.remove();
        }
        myPosition = map.addMarker( new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("I am here"));
    }
}
