package com.teamteamname.gotogothenburg.map;

import android.util.Log;

import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.api.BusStatusAPI;
import com.teamteamname.gotogothenburg.api.IBusStatusHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class that identifies which ElectriCity bus which the user is connected to.
 * Created by Anton on 2015-09-24.
 */


public class OnWhichBusIdentifier implements IBusStatusHandler {
    private static OnWhichBusIdentifier instance;
    private static final String ELECTRICITY_WIFI_SSID = "ElectriCity";
    private List<IOnWhichBusListener> listeners;
    private boolean isRunning;

    private OnWhichBusIdentifier(){
        listeners = new ArrayList<>();
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
        //if (!isRunning()){
            isRunning = true;
            Timer test = new Timer();
            TimerTask testTask = new TimerTask() {
                @Override
                public void run() {
                    startQuery();
                }
            };
            test.schedule(testTask, 1);


        //}
    }

    /**
     * Call this method to stop scanning for which bus the device is on.
     * Once called the OnWhichBusIdentifier will stop checking which bus the user is on.
     * If it's not running, nothing will happen.
     */
    public void stop(){
        if (isRunning()){
            isRunning = false;
        }
    }

    /**
     * A method used to identify if the OnWhichBusIdentifier is running.
     * @return
     */
    public boolean isRunning(){
        return isRunning;
    }

    private void startQuery(){
        if (AndroidDeviceAPI.getInstance().connectedToWifi(ELECTRICITY_WIFI_SSID)){
            BusStatusAPI.getInstance().getConnectedBusSystemID(this);
        } else {
            Log.e("startQuery: ", "Not connected to Electricity wifi. Connected to:" + AndroidDeviceAPI.getInstance().getConnectedWifiSSID());
        }


    }

    @Override
    public void getConnectedBusSystemIDCallback(String returnValue) {
        if (returnValue != null) {
            Log.e("SystemIDCallback: ", "<"+returnValue+">");
        }

       // if (isRunning()){
            Bus identifiedBus = Bus.getBusBySysId(returnValue);

            if (identifiedBus != null){
                for (IOnWhichBusListener listener: listeners){
                    listener.whichBussCallBack(identifiedBus);
                }
            }

      //  }

    }
}

