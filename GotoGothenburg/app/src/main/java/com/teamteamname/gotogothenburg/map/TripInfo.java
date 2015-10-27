package com.teamteamname.gotogothenburg.map;

import com.google.android.gms.maps.model.LatLng;

import lombok.Getter;

/**
 * Created by Mattias Ahlstedt on 2015-10-27.
 */
public class TripInfo {
    @Getter private String line;
    @Getter private String stopName;
    @Getter private String track;
    @Getter private LatLng position;

    public TripInfo(String line, String stopName, String track, LatLng position){
        this.line = line;
        this.stopName = stopName;
        this.track = track;
        this.position = position;
    }
}
