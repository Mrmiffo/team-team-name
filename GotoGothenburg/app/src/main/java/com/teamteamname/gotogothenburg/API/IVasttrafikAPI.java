package com.teamteamname.gotogothenburg.API;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mattias Ahlstedt on 2015-09-25.
 */
public interface IVasttrafikAPI {
    void getCoordinates(VasttrafikHandler callback, LatLng origin, LatLng dest);
    void getCoordinates(VasttrafikHandler callback, String originStop, String destStop);
    void getAutocomplete(VasttrafikHandler callback, String input);
    void getNearbyStops(VasttrafikHandler callback, LatLng origin, int maxDist);
    void setWalkSpeed(int walkSpeed);
    void setMaxWalkDist(int maxWalkDist);
    void setAddChangeTime(int addChangeTime);
    void setMaxChanges(int maxChanges);
    void setWheelchair(boolean wheelchair);
    void setStroller(boolean stroller);
    void setLowFloor(boolean lowFloor);
    void setRampLift(boolean rampLiftNeeded);
}
