package com.teamteamname.gotogothenburg.map;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikTripHandler;
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
    // Marker for where the user clicks on the map
    Marker userSelectionMarker;
    private static final float BUTTON_PRESSED_ALPHA = 1f;
    private static final float BUTTON_UNPRESSED_ALPHA = 0.5f;

    private List<Marker> currentTripMarkers = new ArrayList<>();
    private VasttrafikChange[] currentTripInfo;
    private List<Polyline> currentPolyline = new ArrayList<>();

    public static MapFragment newInstance(){
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
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
        setStartGuideButtonAlpha(view);

        return parentView;
    }

    private void setStartGuideButtonAlpha(View view) {
        if (GuideHandler.getInstance().isRunning()){
            view.findViewById(R.id.startGuideButton).setAlpha(BUTTON_PRESSED_ALPHA);
        } else {
            view.findViewById(R.id.startGuideButton).setAlpha(BUTTON_UNPRESSED_ALPHA);
        }
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
     * Draws all the given polylines on the map
     * @param polylines the polylines which are to be drawn
     */
    public void drawPolyLine(PolylineOptions... polylines){
        if(currentPolyline != null) {
            removeCurrentPolyline();
        }

        for(PolylineOptions p : polylines) {
            p.width(5);
            currentPolyline.add(map.addPolyline(p));
            p.visible(true);
        }
    }

    private void removeCurrentPolyline(){
        for(Polyline p : currentPolyline){
            p.remove();
        }
    }


    /**
     * Places markers containing the given trip information on the map
     * @param newTrip The trip information
     */
    public void updateCurrentTrip(VasttrafikChange... newTrip){
        if(currentTripMarkers != null) {
            clearTripMarkers();
        }

        currentTripInfo = newTrip;

        for(VasttrafikChange vc : currentTripInfo){
            MarkerOptions marker = new MarkerOptions();
            if(vc.getLine().equalsIgnoreCase("Walk")) {
                marker.title("Walk from: " + vc.getStopName());
            } else {
                marker.title("Line: " + vc.getLine());
                marker.snippet("From: " + vc.getStopName() + ", track " + vc.getTrack());
            }
            marker.position(vc.getPosition());
            currentTripMarkers.add(placeMarker(marker));
        }
    }

    private void clearTripMarkers(){
        for(Marker m : currentTripMarkers){
            m.remove();
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
                v.setAlpha(BUTTON_UNPRESSED_ALPHA);
            } else {
                for (Destination dest : RecommendedDestinations.getInstance().getRecommendedDestinations()) {
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(dest.getLatitude(), dest.getLongitude()));
                    marker.title(dest.getName());
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    marker.snippet("directions");
                    markers.add(placeMarker(marker));
                }
                v.setAlpha(BUTTON_PRESSED_ALPHA);
            }
            isDisplaying = !isDisplaying;
        }
    };

    private View.OnClickListener startGuideButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (GuideHandler.getInstance().isRunning()) {
                GuideHandler.getInstance().stopGuide();
            } else {
                GuideHandler.getInstance().startGuide();
            }
            setStartGuideButtonAlpha(v);
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
                v.setAlpha(BUTTON_UNPRESSED_ALPHA);
            } else {
                for (Destination dest : SavedDestinations.getInstance().getSavedDestinations()) {
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(dest.getLatitude(), dest.getLongitude()));
                    marker.title(dest.getName());
                    marker.snippet("Directions");
                    markers.add(placeMarker(marker));
                }

                v.setAlpha(BUTTON_PRESSED_ALPHA);
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
                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                String address = "";
                if (addressList.size() > 0) {
                    address = addressList.get(0).getAddressLine(0);
                }
                userSelectionMarker = placeMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_drop_black_48dp))
                        .title("".equals(address) ? latLng.toString(): address)
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
                SavedDestinations.getInstance().addDestination(new Destination(marker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude));
                marker.remove();
                Toast.makeText(getActivity(), "Destination added", Toast.LENGTH_SHORT).show();
            } else if(!"Directions".equals(marker.getSnippet())) {
                return;
            } else {
                Location myLocation = LocationServicesAPI.getInstance().getLastKnownLocation();
                if (myLocation != null) {
                    VasttrafikAPI.getInstance().getTrip(
                            (VasttrafikTripHandler) getActivity(),
                            (VasttrafikErrorHandler) getActivity(),
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
