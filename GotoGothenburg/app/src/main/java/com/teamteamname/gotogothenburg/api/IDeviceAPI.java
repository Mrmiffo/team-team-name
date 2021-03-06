package com.teamteamname.gotogothenburg.api;

/**
 * The interface DeviceAPI is used to abstract the model of the program from the device it's
 * implemented on. Each operating system and sometimes operating system version or device type may
 * have access to different functionalites and this interface is intended to make sure that the
 * application can handle these in the best possible way by setting a framework for the responses.
 * Created by Anton on 2015-09-23.
 */
public interface IDeviceAPI {

    /**
     * Plays a given sound through the default sound output port.
     * @param callback the interface to call when the sound finished playing.
     * @param soundId the fileId for the sound.
     */
    void playSound(ISoundDoneCallback callback,int soundId);

    /**
     * Checks whether handsfree are plugged into the standard sound I/O port.
     * @return true if handsfree are plugged in, false otherwise.
     */
    boolean isHandsfreePluggedIn();

    /**
     *
     * @return the MAC adress of the router which the device is connected to.
     */
    String getWiFiRouterMAC();

    /**
     *
     * @return the SSID of the Wifi the device is currently connected to.
     */
    String getConnectedWifiSSID();

    /**
     * Returns if the device is currently connected to the specified wifi.
     * @param ssid
     * @return
     */
    boolean connectedToWifi(String ssid);


}
