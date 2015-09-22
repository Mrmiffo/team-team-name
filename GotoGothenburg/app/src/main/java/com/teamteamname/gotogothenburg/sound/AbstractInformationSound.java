package com.teamteamname.gotogothenburg.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.teamteamname.gotogothenburg.GPSLocation;

import java.io.Serializable;

/**
 * Created by kakan on 2015-09-22.
 */
public abstract class AbstractInformationSound implements Audible, Serializable {

    private static final long serialVersionUID = -3849515683521293056L;
    private GPSLocation location;
    private float radius;
    private int resID;

    public AbstractInformationSound(GPSLocation location, float radius, int resID) {
        this.location = location.clone();
        this.radius = radius;
        this.resID = resID;
    }

    @Override
    public void playSound(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, resID);
        mediaPlayer.start();
    }

    /**
     * Evaluates whether or not a given coordinate is enclosed in the area.
     * @param coordinate the coordinate to evaluate.
     * @return true if the coordinate is in the area, false otherwise.
     */
    public boolean isInArea(GPSLocation coordinate) {
        // (x - x_center)² + (y - y_center)² < radius²
        return Math.pow(coordinate.getLatitude()-location.getLatitude(), 2) + Math.pow(coordinate.getLongitude()-location.getLongitude(), 2) < Math.pow(radius, 2);
    }
}
