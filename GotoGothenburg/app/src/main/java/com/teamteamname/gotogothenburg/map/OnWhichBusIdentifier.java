package com.teamteamname.gotogothenburg.map;


import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.api.ElectriCityWiFiSystemIDAPI;
import com.teamteamname.gotogothenburg.api.IElectriCityWiFiSystemIDAPIHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class that identifies which ElectriCity bus which the user is connected to. This class is a
 * singleton and provides functionality for identifying which bus the user is on.
 * This class has been tested through user tests only. JUnit tests has not been created as the
 * extensive use of singleton classas and reliance of specific Wifi networks makes this all but
 * practically impossible without extensive work.
 * How to use:
 * 1. Register a listener to the identifier.
 * 2. Start the identifier.
 * 2.a A timer will start that triggers each 5 seconds sending a response to the respective callback methods depending on the result.
 * 3. Stop the timer when a response is no longer requiered.
 * NOTE: If the listener is not removed it may receive a response when some other part of the
 * application is starting the identifier.
 * Created by Anton on 2015-09-24.
 */
public class OnWhichBusIdentifier{
    private static OnWhichBusIdentifier instance;
    private static final String ELECTRICITY_WIFI_SSID = "ElectriCity";
    private volatile List<IOnWhichBusListener> listeners;

    private Timer queryTimer;
    private static final int QUERY_TIMER_DELAY = 5;
    private volatile boolean queryTimerRunning;

    private OnWhichBusIdentifier(){
        listeners = new ArrayList<>();
    }

    /**
     * Initialize method of the singleton class. This method should be run once only when the
     * application is launched.
     */
    public synchronized static void initialize(){
        if (instance == null){
            instance = new OnWhichBusIdentifier();
        }
    }

    public synchronized static OnWhichBusIdentifier getInstance(){
        return instance;
    }

    /**
     * Register a listener to the onWhichBusIdentifier. Any class added as a listener will have it's
     * callback method run when the identifier is able to identify a bus that the user is on.
     * It the identifier is running but fails to identify any bus, the error callback method in the
     * listener will be called.
     * @param listener the listener to add.
     */
    public void registerListener(IOnWhichBusListener listener){
        listeners.add(listener);
    }

    /**
     * Removes a listener from the identifier. The callback methods of this listener will no longer be called.
     * @param listener to remove.
     */
    public void removeListener(IOnWhichBusListener listener){
        if (listeners.contains(listener)){
            listeners.remove(listener);
        }
    }

    /**
     * Call this method to start scanning for which bus the device is on.
     * Once started the OnWhichBusIdentifier will start broadcasting which bus
     * the user is on to all listeners.
     * If it's already running nothing will happen.
     */
    public synchronized void start(){
        if (!queryTimerRunning){
            //Create a new timer each time the query is started as it's not possible to relaunch a canceld timer.
            queryTimer = new Timer();
            queryTimer.scheduleAtFixedRate(new QueryExecutor(), 0, 1000 * QUERY_TIMER_DELAY);
            queryTimerRunning = true;
        }
    }

    /**
     * Call this method to stop scanning for which bus the device is on.
     * Once called the OnWhichBusIdentifier will stop checking which bus the user is on.
     * If it's not running, nothing will happen.
     */
    public synchronized void stop(){
        if (queryTimerRunning){
            queryTimer.cancel();
            queryTimer.purge();
            queryTimerRunning = false;
        }
    }

    /**
     * A method used to identify if the OnWhichBusIdentifier is running.
     * @return
     */
    public synchronized boolean isRunning(){
        return queryTimerRunning;
    }


    /**
     * QueryExecutor is a local helper class for the OnWhichBussIdentifier which is used to
     * run the query for which bus the user is on in a separate thread.
     */
    private class QueryExecutor extends TimerTask  implements IElectriCityWiFiSystemIDAPIHandler {
        @Override
        public void run() {
            startQuery();
        }

        //Run the query
        private void startQuery() {
            //First check if the user is on an Electricity wifi
            if (AndroidDeviceAPI.getInstance().connectedToWifi(ELECTRICITY_WIFI_SSID)) {
                //If so, query the Wifi for the system id. Call back will come through the
                // connectedBusSystemIDCallback method, or the connectedBusErrorCallback method.
                ElectriCityWiFiSystemIDAPI.getInstance().getConnectedBusSystemID(this);
            } else {
                for (final IOnWhichBusListener listener: listeners){
                    listener.notConnectedToElectriCityWifiError();
                }
            }
        }

        @Override
        public void connectedBusSystemIDCallback(String returnValue) {
            //Check if the response contains data and if the identifier is still running.
            if (isRunning() && returnValue != null){
                //Identify the buss, if no bus was found, report to listeners.
                final Bus identifiedBus = Bus.getBusBySysId(returnValue);
                if (identifiedBus == null){
                    for (final IOnWhichBusListener listener: listeners){
                        listener.unableToIdentifyBusError();
                    }
                } else {
                    for (final IOnWhichBusListener listener: listeners){
                        listener.whichBussCallBack(identifiedBus);
                    }
                }
            }
        }

        @Override
        public void connectedBusErrorCallback(Exception e) {
            if (e instanceof IOException) {
                for (final IOnWhichBusListener listener : listeners) {
                    listener.unableToIdentifyBusError();
                }
            }

        }
    }
}

