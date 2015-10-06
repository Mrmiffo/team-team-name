package com.teamteamname.gotogothenburg.api;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The AndriodDeviceAPI is intended for any android device with a WiFi signal.
 * Created by Anton on 2015-09-23.
 */
public class AndroidDeviceAPI implements IDeviceAPI {
    private static AndroidDeviceAPI instance;
    private WifiInfo wifiInfo;
    private Context context;

    /**
     * Constructor require a context in order to connect the API to the device.
     */
    private AndroidDeviceAPI(Context context){
        this.context = context;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

    public static synchronized AndroidDeviceAPI getInstance(){
        return instance;
    }

    /**
     * A method to initialize the singleton instance of AndroidDeviceAPI. Require and android context
     * to setup the instance. All methods will throw nullpointer exception unless this method has
     * been run.
     * @param context
     */
    public static void initialize(Context context) {
        if (instance == null) {
            instance = new AndroidDeviceAPI(context);
        }
    }

    /**
     * Method used to get the MAC adress of the router. This is used to identify which bus the user is on.
     * @return null if the com.teamteamname.gotogothenburg.API has been created incorrectly or if the device is not connected to a Wifi.
     */
    @Override
    public String getWiFiRouterMAC(){
        return wifiInfo.getBSSID();

    }

    @Override
    public String getConnectedWifiSSID() {
        String toReturn = wifiInfo.getSSID();
        //getSSID method returns the SSID in the format of "<SSID>" with "" included.
        // If an ssid is returned this cleaner will remove the " before and after the SSID.
        if (toReturn != null && toReturn.length() > 1){
            toReturn = toReturn.substring(1,toReturn.length()-1);
        }
        return toReturn;
    }

    @Override
    public void playSound(File sound) {
        int resourceID = context.getResources().getIdentifier(sound.getName(), null, null);
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, resourceID);
        mediaPlayer.start();
        Timer mediaStopper = new Timer();
        mediaStopper.schedule(new TimerTask() {
            @Override
            public void run() {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }, mediaPlayer.getDuration());

    }

    @Override
    public boolean iSHandsfreePluggedIn() {
        return AudioManager.ACTION_HEADSET_PLUG.equals("1");
    }

    @Override
    public boolean connectedToWifi(String ssid) {
        if (ssid == null){
            return false;
        }
        Log.e("ConnectedToWifi", "Received SSID: " + ssid);
        Log.e("ConnectedToWifi", "Device connected to SSID:" + getConnectedWifiSSID());
        return ssid.equals(getConnectedWifiSSID());
    }

}
