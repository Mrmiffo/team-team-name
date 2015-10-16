package com.teamteamname.gotogothenburg.api.vasttrafik;

import com.google.android.gms.maps.model.LatLng;

import lombok.Getter;

/**
 * Created by Mattias Ahlstedt on 2015-10-14.
 */
public class VasttrafikChange {
    @Getter private String line;
    @Getter private String stopName;
    @Getter private String track;
    @Getter private LatLng position;

    public VasttrafikChange(String line, String stopName, String track, LatLng position){
        this.line = line;
        this.stopName = stopName;
        this.track = track;
        this.position = position;
    }
}
