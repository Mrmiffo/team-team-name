package com.teamteamname.gotogothenburg.api.vasttrafik.parsers;

import com.google.android.gms.maps.model.LatLng;

import lombok.Getter;

/**
 * Created by Mattias Ahlstedt on 2015-10-27.
 */
public class Change {
    @Getter final private String line;
    @Getter final private String stopName;
    @Getter final private String track;
    @Getter final private LatLng position;

    public Change(String line, String stopName, String track, LatLng position){
        this.line = line;
        this.stopName = stopName;
        this.track = track;
        this.position = position;
    }
}
