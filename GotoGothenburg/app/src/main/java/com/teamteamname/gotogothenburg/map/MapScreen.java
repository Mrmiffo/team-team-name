package com.teamteamname.gotogothenburg.map;

import android.location.Location;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.ErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.TripHandler;
import com.teamteamname.gotogothenburg.guide.GuideHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * The fragment used to display the map in the application.
 * Created by Anton on 2015-09-21.
 */
public class MapScreen extends com.google.android.gms.maps.MapFragment implements IMap, OnMapReadyCallback {

    // Reference to the map displayed in application
    private GoogleMap map;
    // Reference to the user selected location on the map in form of a Marker
    private Marker userMarker;

    private List<Marker> currentTripMarkers = new ArrayList<>();
    private TripInfo[] currentTripInfo;
    private List<Polyline> currentPolyline = new ArrayList<>();

    public static MapScreen newInstance(){
        return new MapScreen();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getMapAsync(this);

        // Make the map zoom in on the user
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
        view.findViewById(R.id.showResturants).setOnClickListener(new FABListener(new RestaurantPoints(),this));
        view.findViewById(R.id.showDestButton).setOnClickListener(new DestinationsPointListener(this));
        view.findViewById(R.id.showRecDest).setOnClickListener(new ShowRecommendedDestinationsListener(this));
        view.findViewById(R.id.startGuideButton).setOnClickListener(startGuideButtonListener);
        setStartGuideButtonAlpha(view);

        return parentView;
    }

    private void setStartGuideButtonAlpha(View view) {
        TypedValue value = new TypedValue();
        if (GuideHandler.getInstance().isRunning()){
            getResources().getValue(R.dimen.BUTTON_PRESSED_ALPHA,value, true);
        } else {
            getResources().getValue(R.dimen.BUTTON_UNPRESSED_ALPHA, value, true);
        }
        view.findViewById(R.id.startGuideButton).setAlpha(value.getFloat());
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
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
        map.setOnMapLongClickListener(new OnMapLongClickListener(this, getActivity()));
        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener(this, (TripHandler) getActivity(), (ErrorHandler) getActivity(), getActivity()));
    }

    private void zoomToLocation(Location location, float zoom){
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
        }
    }

    @Override
    public Marker placeMarker(MarkerOptions marker){
        return map.addMarker(marker);
    }

    @Override
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
     * @param lines
     * @param stopNames
     * @param tracks
     * @param positions
     */
    public void updateCurrentTrip(String[] lines, String[] stopNames, String[] tracks, LatLng[] positions){
        TripInfo[] newTrip = new TripInfo[positions.length];
        for(int i = 0; i < positions.length; i++){
            newTrip[i] = new TripInfo(lines[i], stopNames[i], tracks[i], positions[i]);
        }

        if(currentTripMarkers != null) {
            clearTripMarkers();
        }

        currentTripInfo = newTrip;

        for(TripInfo trip : currentTripInfo){
            MarkerOptions marker = new MarkerOptions();
            if(trip.getLine().equalsIgnoreCase("Walk")) {
                marker.title("Walk from: " + trip.getStopName());
            } else {
                marker.title("Line: " + trip.getLine());
                marker.snippet("From: " + trip.getStopName() + ", track " + trip.getTrack());
            }

            marker.position(trip.getPosition());
            currentTripMarkers.add(placeMarker(marker));
        }
    }

    @Override
    public Marker getUserMarker() {
        return userMarker;
    }

    @Override
    public void setUserMarker(Marker marker) {
        this.userMarker = marker;
    }

    private void clearTripMarkers(){
        for(Marker m : currentTripMarkers){
            m.remove();
        }
    }

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
}
