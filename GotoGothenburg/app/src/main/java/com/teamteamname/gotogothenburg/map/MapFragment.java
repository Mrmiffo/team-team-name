package com.teamteamname.gotogothenburg.map;

import android.app.Fragment;
import android.graphics.Color;
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
public class MapFragment extends Fragment implements OnMapReadyCallback {


    private MapView mapView;
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
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        view.findViewById(R.id.centerCameraButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomToLocation(LocationServicesAPI.getInstance().getLastKnownLocation(),15);
            }
        });

        mapView = ((MapView)view.findViewById(R.id.mapView));
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        
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
                    for(Marker marker: markers){
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
      }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        mapView.onDestroy();
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
    }

    /**
     * Draws a polyline on the map with the color rgb
     * @param r color
     * @param g color
     * @param b color
     * @param coords List of coordinates to connect with a line
     */
    public void drawPolyLine(boolean cleanMap, int r, int g, int b, LatLng... coords){
        if(polyline != null && cleanMap) {
            removePolyline();
        }

        PolylineOptions polyline = new PolylineOptions();
        polyline.color(Color.argb(255, r, g, b));
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
