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

    public boolean equals(Object other){
        if(other == null){
            return false;
        }else if(other == this){
            return true;
        }else if(!other.getClass().equals(this.getClass())){
            return false;
        }
        float epsilon = 0.00000001f;
        GPSCoord otherGPSCoord = (GPSCoord)other;
        if(Math.abs(otherGPSCoord.getLatitude()-this.getLatitude())<epsilon && otherGPSCoord.getLongitude()-this.getLongitude()<epsilon){
            return true;
        }
        return false;
    }

    public int hashCode(){
        int hash = 1;
        hash = hash*17 + Math.round(latitude);
        hash = hash*31 + Math.round(longitude);
        return hash;
    }
}
