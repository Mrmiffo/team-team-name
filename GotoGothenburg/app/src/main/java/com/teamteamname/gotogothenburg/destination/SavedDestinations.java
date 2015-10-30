package com.teamteamname.gotogothenburg.destination;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton class which contains all the destinations that the user has saved. This class use an
 * IDestinationSaver in order to save the data. Register a listener to this class in order to get
 * notified when destinations are added or removed.
 * Created by Anton on 2015-09-30.
 */
public final class SavedDestinations {
    private List<Destination> savedDestinations;
    final private IDestinationSaver saver;
    final private List<ISavedDestinationListener> listeners;
    private static SavedDestinations instance;

    //Private constructor used by init method.
    private SavedDestinations(IDestinationSaver saver){
        this.saver = saver;
        savedDestinations = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    /**Package private method used to get an instance
     *
     * @return the instance of the singleton, or null if the init class has not been run.
     */
    public static SavedDestinations getInstance(){
        return instance;
    }

    /**
     * Used to init the singleton.
     * @param saver the IDestinationSaver to be used to permanently store data. If this object is null
     *              no data will be retained after restarting the application.
     */
    public static synchronized void init(IDestinationSaver saver){
        if (instance == null){
            instance = new SavedDestinations(saver);

        }
    }

    /**
     * Method called in order to load the SavedDestinations with destinations from the database upon
     * restart. This method will replace all the destinations in SavedDestinations with the provided
     * list of destinations. It will also make sure that the database have the same instances. Will
     * notify all listeners once.
     * WARNING: THIS WILL PERMANENTLY REMOVE ALL EXISTING DESTINATIONS AND REPLACE WITH PROVIDED LIST.
     * IF CALLED WITH AN EMPTY LIST ALL DESTINATIONS WILL REMOVED BOTH FROM MEMORY AND DATABASE!
     * @param destinationsToLoad
     */
    public void loadDestinations(List<Destination> destinationsToLoad){
        if (destinationsToLoad != null && instance != null){
            savedDestinations = new ArrayList<>();
            savedDestinations.addAll(destinationsToLoad);
            saver.saveAll(getSavedDestinations());
            //notifyListeners();
        }
    }

    /**
     * Get a list of all the saved destinations.
     * @return A new list with all the saved destinations. Will return an empty list if no
     * destinations were found.
     */
    public List<Destination> getSavedDestinations(){
        final List<Destination> toReturn = new ArrayList<>();
        toReturn.addAll(savedDestinations);
        return toReturn;
    }

    /**
     * Adds another destination to the list of Saved destinations. Will notify all listeners that a
     * changes has been made.
     * @param toAdd the destination to add.
     */
    public void addDestination(Destination toAdd){
        savedDestinations.add(toAdd);
        saver.save(toAdd);
        notifyListeners();
    }

    /**
     * Remove the selected destination if it exists in the list of saved destinations. Will not do
     * anything if the provided destination does not exist in the list. If a destination is removed
     * all listeners will be notified.
     * @param toRemove the destinations to remove.
     */
    public void removeDestination(Destination toRemove) {
        if (savedDestinations.contains(toRemove)) {
            savedDestinations.remove(toRemove);
            saver.removeDestination(toRemove);
            notifyListeners();
        }

    }

    /**
     * Returns a list of all destinations that has the corresponding isVisited value.
     * @param isVisited if the destinations to return should be visited or not.
     * @return a new list of all the destinations found in the database. Will return an empty list if
     * no destinations were found.
     */
    public List<Destination> getVisited(boolean isVisited){
        final List<Destination> toReturn = new ArrayList<>();
        for (final Destination dest: savedDestinations){
            if (dest.isVisited() == isVisited){
                toReturn.add(dest);
            }
        }
        return toReturn;
    }

    //Local method to report to all the listeners that a change has been made.
    public void notifyListeners(){
        for (final ISavedDestinationListener listener: listeners){
            listener.update();
        }
    }

    /**
     * Method used to register an object as a listener to the SavedDestinations object. The callback
     * method of the ISavedDestinationListener will be called when the list of destinations is modified.
     *
     * @param listener the listener object that should be called when changes are made.
     */
    public void registerListener(ISavedDestinationListener listener){
        listeners.add(listener);
    }

    /**
     * Method used to remove a listener. The removed listener will no longer receive callbacks when
     * the SavedDestinations is modified.
     * @param listener
     */
    public void removeListener(ISavedDestinationListener listener){
        if (listeners.contains(listener)){
            listeners.add(listener);
        }
    }
}
