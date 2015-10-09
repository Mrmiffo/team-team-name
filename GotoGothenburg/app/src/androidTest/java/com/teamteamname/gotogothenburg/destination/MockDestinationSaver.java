package com.teamteamname.gotogothenburg.destination;

import java.util.List;

/**
 * A mock class of the destination saver used by the SavedDestinations in order to not save anything to the database in these test.
 * Created by Anton on 2015-10-09.
 */
public class MockDestinationSaver implements IDestinationSaver {

    @Override
    public void saveAll(List<Destination> destinationsToSave) {

    }

    @Override
    public void save(Destination destinationToSave) {

    }

    @Override
    public List<Destination> loadAll() {
        return null;
    }

    @Override
    public void removeDestination(Destination destinationToRemove) {

    }

    @Override
    public void removeAllDestinations() {

    }
}
