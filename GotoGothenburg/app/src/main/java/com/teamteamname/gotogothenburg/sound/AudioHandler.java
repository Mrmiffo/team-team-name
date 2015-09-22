package com.teamteamname.gotogothenburg.sound;

import android.content.Context;

import com.teamteamname.gotogothenburg.GPSListener;
import com.teamteamname.gotogothenburg.GPSLocation;

import java.util.LinkedList;

/**
 * Created by kakan on 2015-09-22.
 */
public class AudioHandler implements GPSListener{
    private static LinkedList<AudioNode> audioNodes = new LinkedList<>();

    public static void addAudioNode(AudioNode node) {
        audioNodes.add(node);
    }

    @Override
    public void updatedLocation(GPSLocation newLocation) {
        for (AudioNode node:audioNodes) {
            if (node.isInArea(newLocation)) {
                node.playSound(/*context*/);
            }
        }
    }
}
