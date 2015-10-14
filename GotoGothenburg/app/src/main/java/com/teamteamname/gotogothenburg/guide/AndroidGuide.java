package com.teamteamname.gotogothenburg.guide;

import android.app.Activity;
import android.content.Context;

import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.route.Route;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A class handling guiding as an Android device.
 * Created by kakan on 2015-10-13.
 */
public class AndroidGuide extends Guide {

    private Context context;
    private GuideDialog guideDialog;

    public AndroidGuide(Context context, Route route) {
        super(route);
        api = AndroidDeviceAPI.getInstance();
        this.context = context;
    }

    @Override
    public void startGuide() {
        isGuiding = true;
        guideNextPointOfInterest();
    }

    @Override
    protected void guideNextPointOfInterest() {
    //Please note that the user will not be able to call this method in any way.
    //It is automatically called when the sound file has finished playing, or after 10 seconds if it could not be played.
        if (isGuiding) {
            pointOfInterest = route.getNextPOI();

            if (pointOfInterest != null) {
                if (api.isHandsfreePluggedIn()) {
                    api.playSound(this, pointOfInterest.getSoundGuide());
                } else {
                    soundCouldNotBePlayed();
                }

                guideDialog = GuideDialog.newInstance(pointOfInterest);
                guideDialog.show(((Activity) context).getFragmentManager(), "guide");
            }
        }
    }

    @Override
    public void stopGuide() {
        isGuiding = false;
    }

    @Override
    public void soundFinishedPlaying() {
        guideDialog.dismiss();
        guideNextPointOfInterest();
    }

    @Override
    public void soundCouldNotBePlayed() {
        Timer delay = new Timer();
        int DELAY_TIME = 10000;
        delay.schedule(new TimerTask() {
            @Override
            public void run() {
                soundFinishedPlaying();
            }
        }, DELAY_TIME);
    }
}
