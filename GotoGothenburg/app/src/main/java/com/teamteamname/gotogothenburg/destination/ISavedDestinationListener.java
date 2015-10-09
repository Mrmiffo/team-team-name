package com.teamteamname.gotogothenburg.destination;

/**
 * Created by Anton on 2015-10-07.
 */
public interface ISavedDestinationListener {
    /**
     * Once registered as a listener to a SavedDestination this method is called each time the '
     * SavedDestination has been changed.
     */
    void update();
}
