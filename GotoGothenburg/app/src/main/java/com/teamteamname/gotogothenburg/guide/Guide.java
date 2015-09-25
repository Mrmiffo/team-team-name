package com.teamteamname.gotogothenburg.guide;

import com.teamteamname.gotogothenburg.API.APIHandler;
import com.teamteamname.gotogothenburg.GPSCoord;
import com.teamteamname.gotogothenburg.GPSListener;
import com.teamteamname.gotogothenburg.Lines;
import com.teamteamname.gotogothenburg.Stops;
import com.teamteamname.gotogothenburg.sound.AudioNode;
import com.teamteamname.gotogothenburg.sound.AudioNodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a guided tour as a list of AudioNodes.
 * Created by kakan on 2015-09-25.
 */
public class Guide implements GPSListener {

    private APIHandler apiHandler;
    private List<AudioNode> guidePoints = new ArrayList<>();
    private Integer indexOfUpcomingAudioNode;


    public Guide(APIHandler apiHandler, Lines line) {
        this.apiHandler = apiHandler;
        switch (line) {
            case BUS_55:
                guidePoints.add(AudioNodeList.getNode(Stops.Sven_Hultings_Gata));
                guidePoints.add(AudioNodeList.getNode(Stops.Chalmersplatsen));
                guidePoints.add(AudioNodeList.getNode(Stops.Kapellplatsen));
                guidePoints.add(AudioNodeList.getNode(Stops.Götaplatsen));
                guidePoints.add(AudioNodeList.getNode(Stops.Valand));
                guidePoints.add(AudioNodeList.getNode(Stops.Kungsportsplatsen));
                guidePoints.add(AudioNodeList.getNode(Stops.Brunnsparken));
                guidePoints.add(AudioNodeList.getNode(Stops.Lilla_Bommen));
                guidePoints.add(AudioNodeList.getNode(Stops.Frihamnsporten));
                guidePoints.add(AudioNodeList.getNode(Stops.Pumpgatan));
                guidePoints.add(AudioNodeList.getNode(Stops.Regnbågsgatan));
                guidePoints.add(AudioNodeList.getNode(Stops.Lindholmen));
                guidePoints.add(AudioNodeList.getNode(Stops.Teknikgatan));
                guidePoints.add(AudioNodeList.getNode(Stops.Lindholmsplatsen));
                break;
        }
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
}
