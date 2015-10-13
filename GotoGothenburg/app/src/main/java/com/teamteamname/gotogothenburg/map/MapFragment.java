package com.teamteamname.gotogothenburg.map;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamteamname.gotogothenburg.destination.Destination;
import com.teamteamname.gotogothenburg.destination.SavedDestinations;
import com.teamteamname.gotogothenburg.route.PointOfInterest;
import com.teamteamname.gotogothenburg.R;
import com.google.android.gms.maps.model.Polyline;
import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.api.ISoundDoneCallback;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.route.Route;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.information.ResalePoints;

import java.util.ArrayList;
import java.util.List;


/**
 * The fragment used to display the map in the application.
 * Created by Anton on 2015-09-21.
 */
public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private List<Polyline> polyline;
    private Marker myPosition;
    private boolean onLocationChangedHasRun;

    public static MapFragment newInstance(){
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.polyline = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ViewGroup parentView = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, null);
        parentView.addView(view);

        view.findViewById(R.id.centerCameraButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomToLocation(LocationServicesAPI.getInstance().getLastKnownLocation(), 15);
            }
        });

        getMapAsync(this);
        
        view.findViewById(R.id.resalePointsButton).setOnClickListener(new View.OnClickListener() {
            private ResalePoints resalePoints = new ResalePoints(getActivity());
            private boolean isDisplaying = false;

            @Override
            public void onClick(View v) {
                if (isDisplaying) {
                    resalePoints.removeResalePoints();
                    v.setAlpha(0.5f);
                } else {
                    resalePoints.drawResalePoints();
                    v.setAlpha(1f);
                }
                isDisplaying = !isDisplaying;
            }
        });

        view.findViewById(R.id.showDestButton).setOnClickListener(new View.OnClickListener() {
            private boolean isDisplaying = false;
            private ArrayList<Marker> markers = new ArrayList<>();

            @Override
            public void onClick(View v) {
                if (isDisplaying) {
                    for (Marker marker : markers) {
                        marker.remove();
                    }
                    v.setAlpha(0.5f);
                } else {
                    for (Destination dest : SavedDestinations.getInstance().getSavedDestinations()) {
                        MarkerOptions marker = new MarkerOptions();
                        marker.position(new LatLng(dest.getLatitude(), dest.getLongitude()));
                        marker.title(dest.getName());
                        markers.add(placeMarker(marker));
                    }

                    v.setAlpha(1f);
                }
                isDisplaying = !isDisplaying;
            }
        });
        return parentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setBuildingsEnabled(true);
        zoomToLocation(LocationServicesAPI.getInstance().getLastKnownLocation(), 15);
    }

    private void zoomToLocation(Location location, float zoom){
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
        }
    }

    /**
     * Adds the MarkerOptions Marker to the map and returns the reference to the added object
     *
     * @param marker Marker to add
     * @return Reference to created marker
     */
    public Marker placeMarker(MarkerOptions marker){
        return map.addMarker(marker);
    }


    /**
     * Used by other classes to start a route for the user
     */
    public void guideUser() {
        Guide guide = new Guide();
        guide.identifyBus();
    }
    
    private class Guide implements IOnWhichBusListener, ISoundDoneCallback {

        private GuideDialog guideDialog;

        /**
         * Starts listening for which bus the user is on
         */
        private void identifyBus() {
            final OnWhichBusIdentifier identifier = OnWhichBusIdentifier.getInstance();
            identifier.registerListener(this);
            identifier.start();
        }

        private void doGuide(Bus bus) {
            final Route route = new Route(bus);
            final PointOfInterest nextPOI = route.getNextPOI();

            if (nextPOI != null) {
                //TODO Check if handsfree are plugged in. (Also, what to do if they are unplugged afterwards?)
                if (nextPOI.getSoundGuide() != null) {
                    AndroidDeviceAPI.getInstance().playSound(this, nextPOI.getSoundGuide()); //TODO change in API to not play if null
                }

                guideDialog = GuideDialog.newInstance(nextPOI);
                guideDialog.show(getActivity().getFragmentManager(), "guide");
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
            ConnectToWiFiErrorDialog connectError = ConnectToWiFiErrorDialog.createInstance(getActivity());
            connectError.show(getActivity().getFragmentManager(),"notConnectedToElectriCityWifiError");
        }

        @Override
        public void unableToIdentifyBusError() {

        }

        @Override
        public void soundFinishedPlaying() {
            identifyBus();
            guideDialog.dismiss();
        }
    }

    /**
     * Draws a polyline on the map
     * @param coords List of coordinates to connect with a line
     */
    public void drawPolyLine(boolean cleanMap, LatLng... coords){
        if(polyline != null && cleanMap) {
            removePolyline();
        }
        PolylineOptions polyline = new PolylineOptions();
        polyline.width(5);
        for(LatLng latlng : coords){
            polyline.add(latlng);
        }
        this.polyline.add(map.addPolyline(polyline));
        polyline.visible(true);
    }

    private void removePolyline(){
        for(Polyline p : polyline){
            p.remove();
        }
    }
}
