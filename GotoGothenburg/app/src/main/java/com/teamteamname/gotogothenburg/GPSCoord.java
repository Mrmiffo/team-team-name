package com.teamteamname.gotogothenburg;

/**
 * Created by kakan on 2015-09-22.
 * A class defining a physical coordinate.
 */
public class GPSCoord {

    private final float latitude;
    private final float longitude;

    public GPSCoord() {
        this.latitude = 0;
        this.longitude = 0;
    }

    public GPSCoord(final float latitude, final float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }
}
