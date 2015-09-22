package com.teamteamname.gotogothenburg.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import com.teamteamname.gotogothenburg.GPSLocation;
import com.teamteamname.gotogothenburg.R;

/**
 * Created by kakan on 2015-09-22.
 */
public abstract class AbstractInformationSound implements Audible {

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
}
