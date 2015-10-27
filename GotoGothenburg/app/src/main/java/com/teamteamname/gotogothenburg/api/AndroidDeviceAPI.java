package com.teamteamname.gotogothenburg.api;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The AndriodDeviceAPI is intended for any android device with a WiFi signal.
 * Created by Anton on 2015-09-23.
 */
public final class AndroidDeviceAPI implements IDeviceAPI {
    private static AndroidDeviceAPI instance;
    final private WifiInfo wifiInfo;
    final private Context context;

    /**
     * Constructor require a context in order to connect the API to the device.
     */
    private AndroidDeviceAPI(Context context){
        this.context = context;
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

    public static AndroidDeviceAPI getInstance() {
        return instance;
    }

    /**
     * A method to init the singleton instance of AndroidDeviceAPI. Require and android context
     * to setup the instance. All methods will throw nullpointer exception unless this method has
     * been run.
     * @param context
     */
    public static void init(Context context) {
        synchronized (AndroidDeviceAPI.class) {
            if (instance == null) {
                instance = new AndroidDeviceAPI(context);
            }
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
    public void playSound(final ISoundDoneCallback callback, int soundId) {
        if (soundId == 0) {
            callback.soundCouldNotBePlayed();
        } else {
            final MediaPlayer mediaPlayer = MediaPlayer.create(context, soundId);
            mediaPlayer.start();
            final Timer mediaStopper = new Timer();
            mediaStopper.schedule(new TimerTask() {
                @Override
                public void run() {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    callback.soundFinishedPlaying();
                }
            }, mediaPlayer.getDuration());
        }
    }

    @Override
    public boolean isHandsfreePluggedIn() {
        final AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        //Check both wired and wireless handsfree
        //Only other parts of the deprecated method is deprecated.
        return audioManager.isWiredHeadsetOn() || audioManager.isBluetoothA2dpOn(); 
    }

    @Override
    public boolean connectedToWifi(String ssid) {
        if (ssid == null){
            return false;
        }
        return ssid.equals(getConnectedWifiSSID());
    }
}
