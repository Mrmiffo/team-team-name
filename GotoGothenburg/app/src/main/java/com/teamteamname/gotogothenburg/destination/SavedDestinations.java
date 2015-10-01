package com.teamteamname.gotogothenburg.destination;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which contains all the destinations that the user has saved. This class implements
 * serializable in order to save all the destinations into.
 * Created by Anton on 2015-09-30.
 */
public class SavedDestinations {
    private List<Destination> savedDestinations;

    public SavedDestinations(){
        savedDestinations = new ArrayList<>();
    }

    public SavedDestinations(List<Destination> savedDestinations){
        this.savedDestinations = new ArrayList<>();
        this.savedDestinations.addAll(savedDestinations);
    }

    public List<Destination> getSavedDestinations(){
        List<Destination> toReturn = new ArrayList<>();
        toReturn.addAll(savedDestinations);
        return toReturn;
    }

    public void addDestination(Destination toAdd){
        savedDestinations.add(toAdd);
    }
    public void removeDestination(Destination toRemove) {
        if (savedDestinations.contains(toRemove)) {
            savedDestinations.remove(toRemove);
        }
    }

    public List<Destination> getVisited(boolean isVisited){
        List<Destination> toReturn = new ArrayList<>();
        for (Destination dest: savedDestinations){
            if (dest.isVisited() == isVisited){
                toReturn.add(dest);
            }
        }
        return toReturn;
    }
}
