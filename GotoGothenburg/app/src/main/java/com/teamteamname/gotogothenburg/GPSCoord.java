package com.teamteamname.gotogothenburg;

import java.util.Objects;

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

    public boolean equals(Object other){
        if(other == this){
            return true;
        }else if(!other.getClass().equals(this.getClass())){
            return false;
        }
        GPSCoord otherGPSCoord = (GPSCoord)other;
        if(otherGPSCoord.getLatitude()==this.getLatitude() && otherGPSCoord.getLongitude()==this.getLongitude()){
            return true;
        }
        return false;
    }
}
