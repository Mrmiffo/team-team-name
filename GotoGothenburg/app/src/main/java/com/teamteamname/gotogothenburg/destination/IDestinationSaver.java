package com.teamteamname.gotogothenburg.destination;

import java.util.List;

/**
 * Created by Anton on 2015-10-07.
 */
public interface IDestinationSaver {
    /**
     * This method will drop all previously saved destinations and replace them with the provided saved destinations.
     * @param destinationsToSave the destinations to save to the database
     */
    void saveAll(SavedDestinations destinationsToSave);

    /**
     * Will save the destinations to the database. Should be called everytime the Saved destinations is updated.
     * @param destinationToSave
     */
    void save(Destination destinationToSave);

    /**
     * Retreives all the destinations from the database and puts them in a new SavedDestinations object.
     * @return SavedDestinations object containing all the destinations in the database.
     */
    List<Destination> loadAll();

    /**
     * Removes the specified destination from the database.
     * @param destinationToRemove
     */
    void removeDestination(Destination destinationToRemove);

    /**
     * Removes all saved destinations in the database.
     */
    void removeAllDestinations();
}
