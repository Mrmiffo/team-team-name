package com.teamteamname.gotogothenburg;

/**
 * An interface for all observers interested in change of GPS location.
 * Created by kakan on 2015-09-22.
 */
public interface GPSListener {
    void updatedLocation(GPSCoord newLocation);
}
