package com.teamteamname.gotogothenburg.map;


import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.api.ElectriCityWiFiSystemIDAPI;
import com.teamteamname.gotogothenburg.api.IElectriCityWiFiSystemIDAPIHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class that identifies which ElectriCity bus which the user is connected to.
 * Created by Anton on 2015-09-24.
 */


public class OnWhichBusIdentifier{
    private static OnWhichBusIdentifier instance;
    private static final String ELECTRICITY_WIFI_SSID = "ElectriCity";
    private List<IOnWhichBusListener> listeners;

    private Timer queryTimer;
    private static final int QUERY_TIMER_DELAY = 5;
    private boolean queryTimerRunning;

    private OnWhichBusIdentifier(){
        listeners = new ArrayList<>();
        queryTimer = new Timer();
    }

    /**
     * Initialize method of the singleton class used to
     */
    public static void initialize(){
        if (instance == null){
            instance = new OnWhichBusIdentifier();

        }
    }
    public static OnWhichBusIdentifier getInstance(){
        return instance;
    }

    public void registerListener(IOnWhichBusListener listener){
        listeners.add(listener);
    }

    public void removeListener(IOnWhichBusListener listener){
        listeners.remove(listener);
    }

    /**
     * Call this method to start scanning for which bus the device is on.
     * Once started the OnWhichBusIdentifier will start broadcasting which bus
     * the user is on to all listeners.
     * If it's already running nothing will happen.
     */
    public void start(){
        if (!queryTimerRunning){
            queryTimer.scheduleAtFixedRate(new QueryExecutor(), 0, 1000 * QUERY_TIMER_DELAY);
            queryTimerRunning = true;
        }
    }

    /**
     * Call this method to stop scanning for which bus the device is on.
     * Once called the OnWhichBusIdentifier will stop checking which bus the user is on.
     * If it's not running, nothing will happen.
     */
    public void stop(){
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
    public boolean isRunning(){
        return queryTimerRunning;
    }


    /**
     * QueryExecutor is a local helper class for the OnWhichBussIdentifier which is used to
     * run the query for which bus the user is on in a seperate thread.
     */
    private class QueryExecutor extends TimerTask  implements IElectriCityWiFiSystemIDAPIHandler {
        @Override
        public void run() {
            startQuery();
        }

        private void startQuery() {
            //First check if the user is on an Electricity wifi
            if (AndroidDeviceAPI.getInstance().connectedToWifi(ELECTRICITY_WIFI_SSID)) {
                //If so, query the Wifi for the system id. Call back will come through the
                // getConnectedBusSystemIDCallback method, or the getConnectedBusError method.
                ElectriCityWiFiSystemIDAPI.getInstance().getConnectedBusSystemID(this);
            } else {
                for (IOnWhichBusListener listener: listeners){
                    listener.notConnectedToElectriCityWifiError();
                }
            }
        }

        @Override
        public void getConnectedBusSystemIDCallback(String returnValue) {
            if (isRunning() && returnValue != null){
                Bus identifiedBus = Bus.getBusBySysId(returnValue);
                if (identifiedBus != null){
                    for (IOnWhichBusListener listener: listeners){
                        listener.whichBussCallBack(identifiedBus);
                    }
                }

            }
        }

        @Override
        public void getConnectedBusError(Exception e) {

        }
    }
}

