package com.teamteamname.gotogothenburg.destination;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which contains all the destinations that the user has saved. This class implements
 * serializable in order to save all the destinations into.
 * Created by Anton on 2015-09-30.
 */
class SavedDestinations {
    private List<Destination> savedDestinations;
    private IDestinationSaver saver;
    private List<ISavedDestinationListener> listeners;
    private static SavedDestinations instance;

    private SavedDestinations(IDestinationSaver saver){
        this.saver = saver;
        savedDestinations = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    static SavedDestinations getInstance(){
        return instance;
    }

    public static synchronized void init(IDestinationSaver saver){
        if (instance == null){
            instance = new SavedDestinations(saver);

        }
    }

    public void loadDestinations(List<Destination> destinationsToLoad){
        if (destinationsToLoad != null && instance != null){
            savedDestinations = new ArrayList<>();
            savedDestinations.addAll(destinationsToLoad);
            notifyListeners();
        }
    }

    public List<Destination> getSavedDestinations(){
        List<Destination> toReturn = new ArrayList<>();
        toReturn.addAll(savedDestinations);
        return toReturn;
    }

    public void addDestination(Destination toAdd){
        savedDestinations.add(toAdd);
        saver.save(toAdd);
        sort();
        notifyListeners();
    }

    public void removeDestination(Destination toRemove) {
        if (savedDestinations.contains(toRemove)) {
            savedDestinations.remove(toRemove);
            saver.removeDestination(toRemove);
        }
        notifyListeners();
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

    private void sort() {
        ArrayList<Destination> newSorting = new ArrayList<>();
        newSorting.addAll(getVisited(false));
        newSorting.addAll(getVisited(true));
        savedDestinations = newSorting;
        notifyListeners();
    }

    private void notifyListeners(){
        for (ISavedDestinationListener listener: listeners){
            listener.update();
        }
    }

    public void registerListener(ISavedDestinationListener listener){
        listeners.add(listener);
    }

    public void removeListener(ISavedDestinationListener listener){
        if (listeners.contains(listener)){
            listeners.add(listener);
        }

    }
}
