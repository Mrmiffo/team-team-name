package com.teamteamname.gotogothenburg.guide;

import com.teamteamname.gotogothenburg.API.APIHandler;
import com.teamteamname.gotogothenburg.GPSCoord;
import com.teamteamname.gotogothenburg.GPSListener;
import com.teamteamname.gotogothenburg.Lines;
import com.teamteamname.gotogothenburg.Stops;
import com.teamteamname.gotogothenburg.map.Bus;
import com.teamteamname.gotogothenburg.sound.AudioNode;
import com.teamteamname.gotogothenburg.sound.AudioNodeList;

import java.util.ArrayList;
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
    private List<AudioNode> playedNodes;
    private List<AudioNode> playQueue;
    private Timer getNextStop;
    private Stops lastStop;

    public Guide (Bus bus) {
        this.bus = bus;
        playedNodes = new ArrayList<>();
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
            playQueue.addAll(AudioNodeList.getListofNodes(lastStop));
            playQueue();
        }
    }

    private synchronized void playQueue() {
        if (playQueue.size() < 1) {
            AudioNode toBePlayed = playQueue.get(0);
            if (!playedNodes.contains(toBePlayed)) {
                playedNodes.add(toBePlayed);
                //be api spela upp
                //api borde bara kunna spela upp en sak åt gången.
            }
            playQueue.remove(toBePlayed);
        }
    }































/*
    private APIHandler apiHandler;
    private List<AudioNode> guidePoints = new ArrayList<>();
    private Integer indexOfUpcomingAudioNode;


    public Guide(APIHandler apiHandler, Bus bus, Lines line) {
        this.apiHandler = apiHandler;
        guidePoints.addAll(AudioNodeList.getListOfNodes(line));
    }








    @Override
    public void updatedLocation(GPSCoord newLocation) {
        updateUpcomingAudioNode(newLocation);
        playSoundIfInArea(newLocation);
    }

    private void updateUpcomingAudioNode(GPSCoord coord) {
        AudioNode closest = null;
        if (indexOfUpcomingAudioNode == null) { //the first time the check is done, all nodes must be checked.
            //Use the first element as pivot.
            closest = guidePoints.get(0);

            for (AudioNode node : guidePoints) {
                closest = getClosest(coord, closest, node);
            }

        } else { //if the check has been done before, only the nodes on both sides need to be checked.

            //Only zero or one other can be closer to the current one.
            if (indexOfUpcomingAudioNode > 0) {
                closest = getClosest(coord, guidePoints.get(indexOfUpcomingAudioNode), guidePoints.get(indexOfUpcomingAudioNode - 1));
            }
            if (indexOfUpcomingAudioNode < guidePoints.size()) {
                closest = getClosest(coord, guidePoints.get(indexOfUpcomingAudioNode), guidePoints.get(indexOfUpcomingAudioNode + 1));
            }
        }
        indexOfUpcomingAudioNode = guidePoints.indexOf(closest);
    }

    private AudioNode getClosest(GPSCoord pointOfReference, AudioNode a, AudioNode b) {
        float lat = pointOfReference.getLatitude();
        float lon = pointOfReference.getLongitude();
        //Uses Pythagoras' theorem and compares which node is closer.
        //sqrt((x - x_a)² + (y - y_a)²) < sqrt((x - x_b)² + (y - y_b)²)
        if (Math.sqrt(lat - Math.pow(a.getLocation().getLatitude(), 2) + Math.pow(lon - a.getLocation().getLongitude(), 2)) <
                Math.sqrt(Math.pow(lat - b.getLocation().getLatitude(), 2) + Math.pow(lon - b.getLocation().getLongitude(), 2))) {
            return a;
        } else { // if both are equally close, either could be returned.
            return b;
        }
    }

    private void playSoundIfInArea(GPSCoord coord) {
        AudioNode closestNode = guidePoints.get(indexOfUpcomingAudioNode);
        if (closestNode.isInArea(coord)) {
            closestNode.playSound(apiHandler);
        }
    }

    */
}
