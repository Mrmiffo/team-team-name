package com.teamteamname.gotogothenburg.sound;

import com.teamteamname.gotogothenburg.Stops;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for keeping track of all created AudioNodes.
 * Created by kakan on 2015-09-25.
 */
public class AudioNodeList {
    private static List<AudioNode> listOfNodes = new ArrayList<>();

    /**
     * Adds a newly created AudioNode to the list.
     * @param node a newly created AudioNode
     */
    public static void addNode(AudioNode node) {
        listOfNodes.add(node);
    }

    /**
     * Returns the AudioNode associated with given stop.
     * @param stop the stop to which the node is associated.
     * @return the AudioNode associated with the parameter, null if no such AudioNode exists
     */
    public static AudioNode getNode(Stops stop) {
        for (AudioNode node: listOfNodes) {
            if (stop.equals(node.getStop())) {
                return node;
            }
        }
        return null;
    }
}
