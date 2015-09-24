package com.teamteamname.gotogothenburg.sound;

import com.teamteamname.gotogothenburg.GPSCoord;

import java.io.File;

import API.APIHandler;
import API.IAPIHandler;

/**
 * A class for associating a sound file with a physical location.
 * Created by kakan on 2015-09-22.
 */
public class AudioNode implements Audible {

    //AudioNode does not extend GPSCoord, since it does not have a "is a" relation.
    private GPSCoord location;
    private float radius;
    private File sound;

    public AudioNode(GPSCoord location, float radius, File sound, APIHandler apiHandler) {
        if (location == null) {
            location = new GPSCoord();
        }
        this.location = location; //Note that location is immutable
        this.radius = radius;
        this.sound = sound;
    }

    @Override
    public void playSound(IAPIHandler apiHandler) {
        apiHandler.playSound(sound);
    }


    /**
     * Evaluates whether or not a given coordinate is enclosed in the area.
     * @param coordinate the coordinate to evaluate.
     * @return true if the coordinate is in the area, false otherwise.
     */
    public boolean isInArea(GPSCoord coordinate) {
        // (x - x_center)² + (y - y_center)² < radius²
        return Math.pow(coordinate.getLatitude() - location.getLatitude(), 2) + Math.pow(coordinate.getLongitude() - location.getLongitude(), 2) < Math.pow(radius, 2);
    }
}
