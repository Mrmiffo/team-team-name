package com.teamteamname.gotogothenburg.map;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamteamname.gotogothenburg.PointOfInterest;
import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.api.ISoundDoneCallback;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.guide.Guide;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.List;


/**
 * The fragment used to display the map in the application.
 * Created by Anton on 2015-09-21.
 */

public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback, LocationListener, IOnWhichBusListener, ISoundDoneCallback {
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
        map.getUiSettings().setMapToolbarEnabled(false);
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

    /**
     * Adds the MarkerOptions Marker to the map and returns the referene to the added object
     *
     * @param marker Marker to add
     * @return Reference to created marker
     */
    public Marker placeMarker(MarkerOptions marker){
        return map.addMarker(marker);
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

    /**
     * Used by other classes to start a guide for the user
     */
    public void guideUser() {
        identifyBus();
    }

    /**
     * Starts listening for which bus the user is on
     */
    private void identifyBus() {
        final OnWhichBusIdentifier identifier = OnWhichBusIdentifier.getInstance();
        identifier.registerListener(this);
        identifier.start();
    }

    private void doGuide(Bus bus) {
        final Guide guide = new Guide(bus);
        final PointOfInterest nextPOI = guide.getNextPOI();

        if (nextPOI != null) {
            AndroidDeviceAPI.getInstance().playSound(this, nextPOI.getSoundGuide());

            final GuideDialog guideDialog = GuideDialog.createInstance(nextPOI);
            guideDialog.show(getFragmentManager(), "guide");
        }
    }

    @Override
    public void whichBussCallBack(Bus busUserIsOn) {
        final OnWhichBusIdentifier identifier = OnWhichBusIdentifier.getInstance();
        identifier.removeListener(this);
        identifier.stop();
        doGuide(busUserIsOn);
    }

    @Override
    public void notConnectedToElectriCityWifiError() {

    }

    @Override
    public void unableToIdentifyBusError() {

    }

    @Override
    public void soundFinishedPlaying() {
        identifyBus();
    }

    /**
     * Draws a polyline on the map with the color rgb
     * @param r color
     * @param g color
     * @param b color
     * @param coords List of coordinates to connect with a line
     */
    public void drawPolyLine(int r, int g, int b, LatLng... coords){
        PolylineOptions polyline = new PolylineOptions();
        polyline.color(Color.argb(255, r, g, b));
        for(LatLng latlng : coords){
            polyline.add(latlng);
        }
        map.addPolyline(polyline);
        polyline.visible(true);
    }
}
