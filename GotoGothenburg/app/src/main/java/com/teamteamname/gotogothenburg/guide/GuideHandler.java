package com.teamteamname.gotogothenburg.guide;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;

import com.teamteamname.gotogothenburg.api.Bus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This singleton class is used to handle the guides and routes.
 * Created by Anton on 2015-10-15.
 */
public final class GuideHandler implements IOnWhichBusListener {

    private static GuideHandler instance;
    private boolean guideHandlerStarted;
    final private Context context;

    //Helper classes
    private AbstractGuide guide;
    private Route routeToStart;
    private OnWhichBusIdentifier onWhichBusIdentifier;

    //Status values
    private boolean hasShownWifiError;
    private Bus prevBus;
    private boolean hasStartedGuide;

    //Settings
    private static final int SHOW_ERROR_AGAIN_AFTER = 60;

    private GuideHandler(Context context){
        this.context = context;

    }

    public static GuideHandler getInstance(){
        return instance;
    }

    public synchronized static void init(Context context){

        if (instance == null){
            instance = new GuideHandler(context);
        }
    }

    /**
     * A method called to start the guide.
     */
    public void startGuide(){
        if (!guideHandlerStarted) {
            guideHandlerStarted = true;
            onWhichBusIdentifier = new OnWhichBusIdentifier();
            onWhichBusIdentifier.registerListener(this);
            onWhichBusIdentifier.start();
        }
    }

    /**
     * A method called to stop the guide.
     */
    public void stopGuide() {
        stopGuidedRoute();
        hasStartedGuide = false;
        guideHandlerStarted = false;
        hasShownWifiError = false;
    }

    private void stopGuidedRoute() {
        if (guide!=null) {
            guide.stopGuide();
        }
        if (routeToStart != null){
            routeToStart.stopRoute();
        }
        if (onWhichBusIdentifier != null) {
            onWhichBusIdentifier.stop();
            onWhichBusIdentifier.removeListener(this);
        }
    }

    /**
     * A methods to check if the guide is currently running.
     * @return true if the guide is running. Return false if the guide is not running.
     */
    public boolean isRunning(){
        return guideHandlerStarted;
    }

    @Override
    public void whichBussCallBack(Bus busUserIsOn) {
        if (!hasStartedGuide && guideHandlerStarted){
            startGuidedRoute(busUserIsOn);
            prevBus = busUserIsOn;
        } else if (hasStartedGuide && guideHandlerStarted && (prevBus == null || prevBus.equals(busUserIsOn))){
            //Restart the guide with the new bus
            stopGuidedRoute();
            startGuidedRoute(busUserIsOn);
            prevBus = busUserIsOn;
        }
    }

    private void startGuidedRoute(Bus busUserIsOn) {
        routeToStart = new Route(busUserIsOn);
        guide = new AndroidGuide(context,routeToStart);
        guide.startGuide();
        hasStartedGuide = true;
    }

    @Override
    public void notConnectedToElectriCityWifiError() {
        //TODO Remove test code.
        //Replace code snippets for using specified bus buss without wifi.
        //--TEST CODE--
        /*
        if (!hasStartedGuide) {
            Log.e("GuideHandler test", "Starting test run");
            whichBussCallBack(Bus.getBusBySysId("2501069301"));
        }
        //-TEST CODE END--
        */
        if (hasStartedGuide) {
            stopGuide();
            //TODO Implement reconnect to wifi to restart guide.
            // Not implemented in prototype version due to lack of time.
        }
        displayWifiError();
    }

    //Method used to display a wifi error. This error will only be display once every 10
    private void displayWifiError() {
        if (!hasShownWifiError) {
            final DialogFragment error = ConnectToWiFiErrorDialog.createInstance(context);
            error.show(((Activity) context).getFragmentManager(), "notConnectedToWifi");
            hasShownWifiError = true;
            final Timer resetError = new Timer();
            resetError.schedule(new TimerTask() {
                @Override
                public void run() {
                    hasShownWifiError = false;
                }
            }, 1000 * SHOW_ERROR_AGAIN_AFTER);
        }
    }

    @Override
    public void unableToIdentifyBusError() {

    }
}
