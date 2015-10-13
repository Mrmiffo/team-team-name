package com.teamteamname.gotogothenburg.api;

/**
 * The callback interface which must be implemented for a class to access IDeviceAPI's playSound()-method.
 * Created by kakan on 2015-10-05.
 */
public interface ISoundDoneCallback {
    /**
     * What action to take when the sound finished playing.
     */
    void soundFinishedPlaying();

    /**
     * What action to take if the sound could not be played.
     */
    void soundCouldNotBePlayed();
}
