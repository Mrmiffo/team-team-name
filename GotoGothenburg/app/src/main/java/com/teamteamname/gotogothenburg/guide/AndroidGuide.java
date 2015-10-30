package com.teamteamname.gotogothenburg.guide;

import android.app.Activity;
import android.content.Context;

import com.teamteamname.gotogothenburg.api.ApiFactory;
import com.teamteamname.gotogothenburg.utils.AndroidConverter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A class handling guiding as an Android device.
 * Created by kakan on 2015-10-13.
 */
public class AndroidGuide extends AbstractGuide {

    final private Context context;
    private GuideDialog guideDialog;
    private UnpluggedHandsfreeDialog unpluggedDialog;

    public AndroidGuide(Context context, Route route) {
        super(route);
        api = ApiFactory.getInstance().createDeviceAPI();
        this.context = context;
    }

    @Override
    public void startGuide() {
        isGuiding = true;
        guideNextPointOfInterest();
    }

    @Override
    protected void guideNextPointOfInterest() {
    //It is automatically called when the sound file has finished playing, or after 10 seconds if it could not be played.
        if (isGuiding) {
            pointOfInterest = route.getNextPOI();
            if (pointOfInterest == null) {
                soundCouldNotBePlayed();
            } else {
                if (api.isHandsfreePluggedIn()) {
                    api.playSound(this,  AndroidConverter.fileToRawResourceID(context, pointOfInterest.getSoundGuide()));
                } else {
                    unpluggedDialog = new UnpluggedHandsfreeDialog();
                    unpluggedDialog.show(((Activity) context).getFragmentManager(), "unplugged");
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
        if (guideDialog != null) {
            guideDialog.dismiss();
        }
        if (unpluggedDialog != null) {
            unpluggedDialog.dismiss();
        }
        guideNextPointOfInterest();
    }

    @Override
    public void soundCouldNotBePlayed() {
        final Timer delay = new Timer();
        final int DELAY_TIME = 10000;
        delay.schedule(new TimerTask() {
            @Override
            public void run() {
                soundFinishedPlaying();
            }
        }, DELAY_TIME);
    }
}
