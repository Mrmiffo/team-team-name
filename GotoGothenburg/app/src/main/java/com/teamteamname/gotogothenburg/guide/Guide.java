package com.teamteamname.gotogothenburg.guide;

import com.teamteamname.gotogothenburg.api.IDeviceAPI;
import com.teamteamname.gotogothenburg.api.ISoundDoneCallback;
import com.teamteamname.gotogothenburg.route.PointOfInterest;
import com.teamteamname.gotogothenburg.route.Route;

/**
 * A class defining what a guide provides, regardless of what device doing the guiding.
 * Created by kakan on 2015-10-13.
 */
public abstract class Guide implements ISoundDoneCallback {
    protected IDeviceAPI api;
    protected Route route;
    protected PointOfInterest pointOfInterest;
    protected boolean isGuiding;

    public Guide(Route route) {
        this.route = route;
    }
    /**
     * Initiates the guiding.
     */
    public abstract void startGuide();

    /**
     * Starts guiding for the upcoming Point of Interest.
     */
    protected abstract void guideNextPointOfInterest();

    /**
     * Terminates the guiding.
     */
    public abstract void stopGuide();
}
