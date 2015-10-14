package com.teamteamname.gotogothenburg.api.vasttrafik;

import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikAutocompleteHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikTripHandler;

/**
 * Created by Mattias Ahlstedt on 2015-09-25.
 */
public interface IVasttrafikAPI {

    /**
     * Sends a list of coordinates to the callback
     * The coordinates are a route from originLocation to destLocation
     * @param tripCallback the callback supplied with response
     * @param errorCallback the callback supplied with an eventual error
     * @param originLocation the location from which the coordinates start
     * @param destLocation the destination where the coordinates end
     */
    void getCoordinates(VasttrafikTripHandler tripCallback, VasttrafikErrorHandler errorCallback, VasttrafikLocation originLocation, VasttrafikLocation destLocation);

    /**
     * Sends a list fo autocomplete suggestions to the callback.
     * The suggestions are based on the string "input" which is supplied.
     * @param autoCallback  The interface which is to be supplied with the result
     * @param errorCallback The interface which is to be supplied with an eventual error
     * @param input     The input which is the base for the autocomplete suggestions
     */
    void getAutocomplete(VasttrafikAutocompleteHandler autoCallback, VasttrafikErrorHandler errorCallback, String input);

    void setWalkSpeed(int walkSpeed);
    void setMaxWalkDist(int maxWalkDist);
    void setAddChangeTime(int addChangeTime);
    void setMaxChanges(int maxChanges);
    void setWheelchair(boolean wheelchair);
    void setStroller(boolean stroller);
    void setLowFloor(boolean lowFloor);
    void setRampLift(boolean rampLiftNeeded);
}
