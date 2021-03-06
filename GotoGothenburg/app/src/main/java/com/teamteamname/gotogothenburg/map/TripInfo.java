package com.teamteamname.gotogothenburg.map;

import com.google.android.gms.maps.model.LatLng;

import lombok.Getter;

/**
 * Used for markers in map to gather all necessary information.
 * Created by Mattias Ahlstedt on 2015-10-27.
 */
public class TripInfo {
    @Getter final private String line;
    @Getter final private String stopName;
    @Getter final private String track;
    @Getter final private LatLng position;

    public TripInfo(String line, String stopName, String track, LatLng position){
        this.line = line;
        this.stopName = stopName;
        this.track = track;
        this.position = position;
    }
}
