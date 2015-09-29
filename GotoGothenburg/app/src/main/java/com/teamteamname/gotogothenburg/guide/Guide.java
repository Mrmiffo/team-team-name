package com.teamteamname.gotogothenburg.guide;

import com.teamteamname.gotogothenburg.PointOfInterest;
import com.teamteamname.gotogothenburg.Stops;
import com.teamteamname.gotogothenburg.map.Bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class representing a guided tour as a list of AudioNodes.
 * Created by kakan on 2015-09-25.
 */
public class Guide {

    //Lista spelade ljud
    //Fråga api efter nästa hållplats --> få tag i alla noder associerade med hållplatsen --> spela upp de som inte är i listan --> lägg till i listan.

    private Bus bus;
    private List<PointOfInterest> visitedPOIs;
    private List<PointOfInterest> playQueue;
    private Timer getNextStop;
    private Stops lastStop;

    public Guide (Bus bus) {
        this.bus = bus;
        visitedPOIs = new ArrayList<>();
        playQueue = new ArrayList<>();
        getNextStop = new Timer();
        getNextStop.schedule(new TimerTask() {
            @Override
            public void run() {
                checkNextStop();

            }
        }, 0, 5000);
    }

    private void checkNextStop() {
        if (!lastStop.equals(null)) {//Fråga ElectriCity efter nextStop() för bussen och adapter))
            lastStop = null; //tilldela med nextStop() och adapter
            playQueue.clear();
            playQueue.addAll(Arrays.asList(lastStop.getPOIs()));
            playQueue();
        }
    }

    private synchronized void playQueue() {
        if (playQueue.size() < 1) {
            PointOfInterest toBePlayed = playQueue.get(0);
            if (!visitedPOIs.contains(toBePlayed)) {
                visitedPOIs.add(toBePlayed);
                //be api spela upp     toBePlayed
                //api borde bara kunna spela upp en sak åt gången.
            }
            playQueue.remove(toBePlayed);
        }
    }
}
