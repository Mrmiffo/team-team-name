package com.teamteamname.gotogothenburg.sound;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.teamteamname.gotogothenburg.GPSLocation;

/**
 * Created by kakan on 2015-09-22.
 */
public abstract class AudioNode implements Audible, UnpluggedHandsfreeDialog.IDialogListener {

    //AudioNode does not extend GPSLocation, since it does not have a "is a" relation.
    private GPSLocation location;
    private float radius;
    private Context context;
    private int resID;


    public AudioNode(GPSLocation location, float radius, Context context, int resID) {
        this.location = location.clone();
        this.radius = radius;
        this.context = context;
        this.resID = resID;

        AudioHandler.addAudioNode(this);
    }

    @Override
    public void playSound() {
        //Checks if a headset is plugged in.
        if (AudioManager.ACTION_HEADSET_PLUG.equals("1")) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, resID);
            mediaPlayer.start();
        } else {
            UnpluggedHandsfreeDialog dialog = new UnpluggedHandsfreeDialog();
            dialog.registerListener(this);
            dialog.show(((Activity) context).getFragmentManager(), null);
        }
    }

    @Override
    public void okPressed(boolean value) {

        //playSound();
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
