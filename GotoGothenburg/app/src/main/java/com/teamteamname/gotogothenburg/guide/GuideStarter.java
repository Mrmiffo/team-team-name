package com.teamteamname.gotogothenburg.guide;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;

import com.teamteamname.gotogothenburg.map.Bus;
import com.teamteamname.gotogothenburg.map.ConnectToWiFiErrorDialog;
import com.teamteamname.gotogothenburg.map.IOnWhichBusListener;
import com.teamteamname.gotogothenburg.map.OnWhichBusIdentifier;
import com.teamteamname.gotogothenburg.route.Route;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used to start a guide.
 * Created by Anton on 2015-10-15.
 */
public class GuideStarter implements IOnWhichBusListener {

    private AbstractGuide guide;
    private boolean hasStartedGuide;
    private Context context;
    private boolean hasShownWifiError;
    private static final int SHOW_ERROR_AGAIN_AFTER = 60;

    public GuideStarter(Context context){
        this.context = context;

    }

    @Override
    public void whichBussCallBack(Bus busUserIsOn) {
        if (!hasStartedGuide){
            Route routeToStart = new Route(busUserIsOn);
            guide = new AndroidGuide(context,routeToStart);
            guide.startGuide();
            hasStartedGuide = true;
        }
    }

    @Override
    public void notConnectedToElectriCityWifiError() {
        //TODO Remove test code
        //Replace code snippets for using specified bus buss without wifi.
        /*
        if (!hasStartedGuide) {
            Log.e("GuideStarter test", "Starting test run");
            whichBussCallBack(Bus.getBusBySysId("2501069301"));
        }
        */
        if (hasStartedGuide) {
            stopGuide();
            //TODO Implement reconnect to wifi to restart guide.
        } else {
            displayWifiError();
        }
    }

    //Method used to display a wifi error. This error will only be display once every 10
    private void displayWifiError() {
        if (!hasShownWifiError) {
            DialogFragment error = ConnectToWiFiErrorDialog.createInstance(context);
            error.show(((Activity) context).getFragmentManager(), "notConnectedToWifi");
            hasShownWifiError = true;
            Timer resetError = new Timer();
            resetError.schedule(new TimerTask() {
                @Override
                public void run() {
                    hasShownWifiError = false;
                }
            }, 1000 * SHOW_ERROR_AGAIN_AFTER);
        }
    }

    private void stopGuide() {
        guide.stopGuide();
        hasStartedGuide = !hasStartedGuide;
        OnWhichBusIdentifier.getInstance().stop();
        OnWhichBusIdentifier.getInstance().removeListener(this);
    }

    @Override
    public void unableToIdentifyBusError() {

    }
}
