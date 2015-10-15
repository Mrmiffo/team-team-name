package com.teamteamname.gotogothenburg.guide;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;

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
public class GuideHandler implements IOnWhichBusListener {

    private AbstractGuide guide;
    private boolean hasStartedGuide;
    private Context context;
    private boolean hasShownWifiError;
    private static final int SHOW_ERROR_AGAIN_AFTER = 60;

    public GuideHandler(Context context){
        this.context = context;

    }

    public void startGuide(){
        OnWhichBusIdentifier.getInstance().registerListener(this);
        OnWhichBusIdentifier.getInstance().start();
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
        //--TEST CODE--
        if (!hasStartedGuide) {
            Log.e("GuideHandler test", "Starting test run");
            whichBussCallBack(Bus.getBusBySysId("2501069301"));
        }
        //-TEST CODE END--
        /*
        if (hasStartedGuide) {
            stopGuide();
            //TODO Implement reconnect to wifi to restart guide.
        } else {
            displayWifiError();
        }
        */
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

    public void stopGuide() {
        guide.stopGuide();
        //TODO Kill route.
        hasStartedGuide = !hasStartedGuide;
        OnWhichBusIdentifier.getInstance().stop();
        OnWhichBusIdentifier.getInstance().removeListener(this);
    }

    @Override
    public void unableToIdentifyBusError() {

    }
}
