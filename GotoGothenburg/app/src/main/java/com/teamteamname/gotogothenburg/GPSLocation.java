package com.teamteamname.gotogothenburg;

/**
 * Created by kakan on 2015-09-22.
 * A class defining a co-ordinate.
 */
public class GPSLocation implements Cloneable{

    private float latitude;
    private float longitude;

    public GPSLocation(final float latitude, final float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    @Override
    public GPSLocation clone() {
        try {
            return (GPSLocation) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
