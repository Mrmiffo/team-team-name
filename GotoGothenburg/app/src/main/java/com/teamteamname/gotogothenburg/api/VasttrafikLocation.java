package com.teamteamname.gotogothenburg.api;

import com.google.android.gms.maps.model.LatLng;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Mattias Ahlstedt on 2015-10-02.
 */
public class VasttrafikLocation {

    @Getter @Setter private String name;
    @Getter @Setter private LatLng latlng;

    public VasttrafikLocation(String name, double lat, double lng){
        this.name = name;
        this.latlng = new LatLng(lat, lng);
    }
}
