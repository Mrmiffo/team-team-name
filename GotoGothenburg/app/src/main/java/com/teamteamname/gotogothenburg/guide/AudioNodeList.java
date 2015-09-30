package com.teamteamname.gotogothenburg.guide;

import com.teamteamname.gotogothenburg.Stops;
import com.teamteamname.gotogothenburg.guide.AudioNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * Returns the AudioNodes associated with the given stop.
     * @param stop the stop to which the nodes are associated.
     * @return the AudioNodes associated with the parameter, null if no such AudioNodes exists
     */
    public static List<AudioNode> getListOfNodes(Stops stop) {
        Set<AudioNode> temp = new HashSet<>();
        for (AudioNode node: listOfNodes) {
            if (node.isAtStop(stop)) {
                temp.add(node);
            }
        }
        if (temp.size() > 0) {
            return new ArrayList<>(temp);
        } else {
            return null;
        }
    }

/*    public static List<AudioNode> getListOfNodes (Lines line) {
        Set<AudioNode> temp = new HashSet<>();
        for (Stops stop: line.getLineStops()) {
            temp.addAll(getListOfNodes(stop));
        }
        if (temp.size() > 0) {
            return new ArrayList<>(temp);
        } else {
            return null;
        }
    }*/
}
