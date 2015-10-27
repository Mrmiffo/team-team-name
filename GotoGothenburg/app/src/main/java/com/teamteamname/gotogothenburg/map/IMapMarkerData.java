package com.teamteamname.gotogothenburg.map;

/**
 * Created by patrick on 15/10/2015.
 * Interface used by FAB listener when user wants to draw markers on the map
 */
public interface IMapMarkerData {

    /**
     * Adds the markers to the provided map
     *
     * @param map Map to add markers to
     */
    void addMarkers(IMap map);

    /**
     * Removes the markers from the map they are drawn on
     */
    void removeMarkers();
}
