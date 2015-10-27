package com.teamteamname.gotogothenburg.route;

import com.teamteamname.gotogothenburg.api.electricity.ElectricityAPI;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityError;
import com.teamteamname.gotogothenburg.api.electricity.handlers.ElectricityNextStopHandler;
import com.teamteamname.gotogothenburg.api.Bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class iterating over, firstly, stops and, secondly, points of interest of the given bus.
 * Created by kakan on 2015-09-25.
 */
public class Route implements ElectricityNextStopHandler{

    private final Bus bus;
    private final List<PointOfInterest> visitedPOIs;
    private final List<PointOfInterest> poiQueue;
    private Stops lastStop = Stops.DEFAULT;
    private boolean secondTry;
    private Timer timer;
    private boolean timerRunning;


    /**
     * Creates a route for the given bus.
     * @param bus the bus to iterated over.
     */
    public Route(final Bus bus) {
        this.bus = bus;
        visitedPOIs = new ArrayList<>();
        poiQueue = new ArrayList<>();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                checkNextStop();
            }
        },0, 5000);
        timerRunning = true;
    }

    /**
     * Method used to stop the thread running in the Route class. Method should be run prior to
     * discarding the object to improve performance.
     */
    public void stopRoute(){
        if (timerRunning){
            timer.cancel();
        }

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
            poiQueue.addAll(Arrays.asList(lastStop.getPointOfInterests()));
        }
    }

    /**
     * Iterates over Point of Intresets along the tour and returns the next Point of Interest.
     * @return the next Point of Interest associated with the current stop. Null if no such exists.
     */
    public PointOfInterest getNextPOI() {
        if (poiQueue.size() > 0) {
            final PointOfInterest next = poiQueue.get(0);
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
    public void electricityRequestError(ElectricityError error) {

    }
}
