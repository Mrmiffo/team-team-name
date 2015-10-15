package com.teamteamname.gotogothenburg.map;

import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.activity.MainActivity;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikLocation;
import com.teamteamname.gotogothenburg.destination.Destination;
import com.teamteamname.gotogothenburg.destination.RecommendedDestinations;
import com.teamteamname.gotogothenburg.destination.SavedDestinations;
import com.teamteamname.gotogothenburg.guide.GuideHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The fragment used to display the map in the application.
 * Created by Anton on 2015-09-21.
 */
public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback {

    // Reference to the map displayed in application
    private GoogleMap map;
    // The current or lastly drawn polyline
    private List<Polyline> polyline = new ArrayList<>();
    // Marker for where the user clicks on the map
    Marker userSelectionMarker;

    public static MapFragment newInstance(){
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        getMapAsync(this);

        LocationServicesAPI.getInstance().registerLocationUpdateListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    zoomToLocation(location, 15);
                    LocationServicesAPI.getInstance().removeLocationUpdateListener(this);
                }
            }
        });

        ViewGroup parentView = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, null);
        parentView.addView(view);

        view.findViewById(R.id.centerCameraButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomToLocation(LocationServicesAPI.getInstance().getLastKnownLocation(), 15);
            }
        });
        view.findViewById(R.id.resalePointsButton).setOnClickListener(new FABListener(new ResalePoints(getActivity()), this));
        view.findViewById(R.id.showHotels).setOnClickListener(new FABListener(new HotelPoints(),this));
        view.findViewById(R.id.showResturants).setOnClickListener(new FABListener(new ResturantPoints(),this));
        view.findViewById(R.id.showDestButton).setOnClickListener(destinationPointsListener);
        view.findViewById(R.id.showRecDest).setOnClickListener(showRecommendedDestinations);
        view.findViewById(R.id.startGuideButton).setOnClickListener(startGuideButtonListener);

        return parentView;
    }

    /*
    Initiate the map when it is available
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setBuildingsEnabled(true);
        map.setInfoWindowAdapter(customInfoWindowAdapter);
        map.setOnMapLongClickListener(onMapLongClickListener);
        map.setOnInfoWindowClickListener(onInfoWindowClickListener);
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

    private View.OnClickListener showRecommendedDestinations = new View.OnClickListener(){
        private List<Marker> markers = new ArrayList<>();
        private boolean isDisplaying;

        @Override
        public void onClick(View v){
            if (isDisplaying){
                for (Marker marker : markers) {
                    marker.remove();
                }
                v.setAlpha(0.5f);
            } else {
                for (Destination dest : RecommendedDestinations.getInstance().getRecommendedDestinations()) {
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(dest.getLatitude(), dest.getLongitude()));
                    marker.title(dest.getName());
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    marker.snippet("directions");
                    markers.add(placeMarker(marker));
                }
                v.setAlpha(1f);
            }
            isDisplaying = !isDisplaying;
        }
    };

    private View.OnClickListener startGuideButtonListener = new View.OnClickListener() {
        GuideHandler guideStarter;
        boolean hasBeenPressed;
        @Override
        public void onClick(View v) {
            if (hasBeenPressed) {
                guideStarter.stopGuide();
                v.setAlpha(0.5f);
            } else {
                if (guideStarter == null){
                    guideStarter = new GuideHandler(getActivity());
                }
                guideStarter.startGuide();
                v.setAlpha(1f);
            }
            hasBeenPressed = !hasBeenPressed;
        }
    };
    /**
     * Listener for displaying destinations on the map
     */
    private View.OnClickListener destinationPointsListener = new View.OnClickListener() {
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
                    marker.snippet("Directions");
                    markers.add(placeMarker(marker));
                }

                v.setAlpha(1f);
            }
            isDisplaying = !isDisplaying;

        }
    };

    /**
     * Listener for displaying a marker where the user clicks on the map
     */
    private GoogleMap.OnMapLongClickListener onMapLongClickListener = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            Geocoder geocoder = new Geocoder(getActivity());
            if (userSelectionMarker != null) {
                userSelectionMarker.remove();
            }
            try {
                userSelectionMarker = placeMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_drop_black_48dp))
                        .title(geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0).getAddressLine(0))
                        .snippet("Create Destination"));
            } catch (IOException e) {
                Toast.makeText(getActivity(),"No network connection",Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Listener for deciding what should happen when the user clicks on a markers info window
     */
    private GoogleMap.OnInfoWindowClickListener onInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            if (marker.equals(userSelectionMarker)) {
                SavedDestinations.getInstance().addDestination(new Destination(marker.getTitle(),marker.getPosition().latitude,marker.getPosition().longitude));
                marker.remove();
                Toast.makeText(getActivity(), "Destination added", Toast.LENGTH_SHORT).show();
            } else {
                Location myLocation = LocationServicesAPI.getInstance().getLastKnownLocation();
                if (myLocation != null) {
                    VasttrafikAPI.getInstance().getCoordinates(
                            (MainActivity) getActivity(),
                            (MainActivity) getActivity(),
                            new VasttrafikLocation("origin", myLocation.getLatitude(), myLocation.getLongitude()),
                            new VasttrafikLocation(marker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude));
                } else {
                    Toast.makeText(getActivity(),"Device Location not found",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * Custom InfoWindowAdapter for displaying custom marker InfoWindows
     * Uses snippet text for deciding what type of InfoWindow to display
     */
    private GoogleMap.InfoWindowAdapter customInfoWindowAdapter = new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {return null;}

        @Override
        public View getInfoContents(Marker marker) {
            if ("Create Destination".equalsIgnoreCase(marker.getSnippet())) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_add_destinations_marker_info_window_layout ,null);
                ((Button)v.findViewById(R.id.info_window_button)).setText(marker.getTitle());
                return v;
            } else if ("Directions".equalsIgnoreCase(marker.getSnippet())) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_directions_marker_info_window_layout, null);
                ((Button)v.findViewById(R.id.info_window_button)).setText(marker.getTitle());
                return v;
            } else {
                return null;
            }
        }
    };
}
