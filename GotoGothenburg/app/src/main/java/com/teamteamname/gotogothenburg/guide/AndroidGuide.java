package com.teamteamname.gotogothenburg.guide;

import android.app.Activity;
import android.content.Context;

import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.map.Bus;
import com.teamteamname.gotogothenburg.map.GuideDialog;

/**
 * A class handling guiding as an Android device.
 * Created by kakan on 2015-10-13.
 */
public class AndroidGuide extends Guide {

    private Context context;
    private GuideDialog guideDialog;

    public AndroidGuide(Context context, Bus bus) {
        super(bus);
        api = AndroidDeviceAPI.getInstance();
        this.context = context;
    }

    @Override
    public void startGuide() {
        isGuiding = true;
        guideNextPointOfInterest();
    }

    /*@Override
    protected void identifyBus() {
        final OnWhichBusIdentifier identifier = OnWhichBusIdentifier.getInstance();
        identifier.registerListener(this);
        identifier.start();
    }*/


    @Override
    protected void guideNextPointOfInterest() {
        if (isGuiding) {
            pointOfInterest = route.getNextPOI();

            if (pointOfInterest != null) {
                //TODO Check if handsfree are plugged in. (Also, what to do if they are unplugged afterwards?)
                if (api.isHandsfreePluggedIn()) {
                    api.playSound(this, pointOfInterest.getSoundGuide());
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

    /*@Override
    public void whichBussCallBack(Bus busUserIsOn) {
        final OnWhichBusIdentifier identifier = OnWhichBusIdentifier.getInstance();
        identifier.removeListener(this);
        identifier.stop();
        bus = busUserIsOn;
        doGuide();
    }*/

    /*@Override
    public void notConnectedToElectriCityWifiError() {
        ConnectToWiFiErrorDialog connectError = ConnectToWiFiErrorDialog.createInstance(getActivity());
        connectError.show(getActivity().getFragmentManager(), "notConnectedToElectriCityWifiError");
    }*/

    /*@Override
    public void unableToIdentifyBusError() {

    }*/


    @Override
    public void soundFinishedPlaying() {
        guideDialog.dismiss();
    }

    @Override
    public void soundCouldNotBePlayed() {

    }
}
