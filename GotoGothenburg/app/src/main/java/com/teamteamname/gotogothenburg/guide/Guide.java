package com.teamteamname.gotogothenburg.guide;

import com.teamteamname.gotogothenburg.PointOfInterest;
import com.teamteamname.gotogothenburg.Stops;
import com.teamteamname.gotogothenburg.api.ElectricityAPI;
import com.teamteamname.gotogothenburg.api.ElectricityNextStopHandler;
import com.teamteamname.gotogothenburg.map.Bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class representing a guided tour.
 * Created by kakan on 2015-09-25.
 */
public class Guide implements ElectricityNextStopHandler{

    private Bus bus;
    private List<PointOfInterest> visitedPOIs;
    private List<PointOfInterest> poiQueue;
    private Stops lastStop = Stops.DEFAULT;
    private boolean secondTry;

    /**
     * Creates a guide for the given bus.
     * @param bus the bus to be guided.
     */
    public Guide(Bus bus) {
        this.bus = bus;
        visitedPOIs = new ArrayList<>();
        poiQueue = new ArrayList<>();
    }

    /**
     * Makes an request via the API to get the next stop for the given bus.
     */
    private void checkNextStop() {
        ElectricityAPI.getInstance().getNextStop(bus, this);
    }

    /**
     * Evaluates whether the given stop is equal to the last stop and takes appropriate actions.
     * @param stop the stop to be evaluated.
     */
    private void evaluateStop(Stops stop) {
        if (!lastStop.equals(stop)) {
            lastStop = stop;
            poiQueue.clear();
            poiQueue.addAll(Arrays.asList(lastStop.getPOIs()));
        }
    }

    /**
     * Used to access the next Point of Interest.
     * @return the next Point of Interest associated with the current stop. Null if no such exists.
     */
    public PointOfInterest getNextPOI() {
        checkNextStop();
        if (poiQueue.size() > 1) {
            PointOfInterest next = poiQueue.get(0);
            if (!visitedPOIs.contains(next)) {
                visitedPOIs.add(next);
                poiQueue.remove(0);
                return next;
            }
        }
        return null;
    }

    @Override
    public void electricityNextStopResponse(Stops nextStop) {
        secondTry = false;
        evaluateStop(nextStop);
    }

    @Override
    public void electricityRequestError(String error) {
        if(!secondTry) {
            checkNextStop();
        } else {
            evaluateStop(Stops.DEFAULT);
        }
    }
}
