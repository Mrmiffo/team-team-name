package com.teamteamname.gotogothenburg.sound;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.teamteamname.gotogothenburg.GPSLocation;

import java.io.File;

import API.APIHandler;

/**
 * Created by kakan on 2015-09-22.
 */
public abstract class AudioNode implements Audible, UnpluggedHandsfreeDialog.IDialogListener {

    //AudioNode does not extend GPSLocation, since it does not have a "is a" relation.
    private GPSLocation location;
    private float radius;
    private File sound;


    public AudioNode(GPSLocation location, float radius, File sound) {
        this.location = location.clone();
        this.radius = radius;
        this.sound = sound;

        AudioHandler.addAudioNode(this);
    }

    @Override
    public void playSound() {
        //Checks if a headset is plugged in.
        if (AudioManager.ACTION_HEADSET_PLUG.equals("1")) {
            APIHandler apiHandler = APIHandler.getInstance();
            apiHandler.playSound(sound);
        } else {
            /*UnpluggedHandsfreeDialog dialog = new UnpluggedHandsfreeDialog();
            dialog.registerListener(this);
            dialog.show(((Activity) context).getFragmentManager(), null);*/
        }
    }

    @Override
    public void okPressed(boolean value) {
        playSound();
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
