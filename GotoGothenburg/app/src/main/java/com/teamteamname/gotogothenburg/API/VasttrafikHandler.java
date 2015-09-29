package com.teamteamname.gotogothenburg.API;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-25.
 */
public interface VasttrafikHandler {
    public void vasttrafikRequestDone(List<String> autocomplete);
    public void vasttrafikRequestError(String e);
}
