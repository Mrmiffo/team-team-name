package com.teamteamname.gotogothenburg.map;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;

/**
 * Created by patrick on 27/10/2015.
 * Interface which establishes the features supported by the map
 */
public interface IMap {

    /**
     * Adds the MarkerOptions Marker to the map and returns the reference to the added object
     *
     * @param markerOptions Marker to add
     * @return Reference to created marker
     */
    Marker placeMarker(MarkerOptions markerOptions);

    /**
     * Draws all the given polylines on the map
     *
     * @param polylines the polylines which are to be drawn
     */
    void drawPolyLine(PolylineOptions... polylines);

    /**
     * Places markers containing the given trip information on the map
     *
     * @param newTrip The trip information
     */
    void updateCurrentTrip(VasttrafikChange... newTrip);

    /**
     * Gets the current user selected location in the form of a marker
     *
     * @return The marker representing the users selection
     */
    Marker getUserMarker();

    /**
     * Sets the user selected location on the map
     *
     * @param marker Marker which will represent the users selected location on the map
     */
    void setUserMarker(Marker marker);
}
