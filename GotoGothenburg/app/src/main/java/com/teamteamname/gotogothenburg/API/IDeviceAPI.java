package com.teamteamname.gotogothenburg.API;

import java.io.File;

/**
 * The interface DeviceAPI is used to abstract the model of the program from the device it's
 * implemented on. Each operating system and sometimes operating system version or device type may
 * have access to different functionalites and this interface is intended to make sure that the
 * application can handle these in the best possible way by setting a framework for the responses.
 * Created by Anton on 2015-09-23.
 */
public interface IDeviceAPI {

    String getWiFiRouterMAC();

    /**
     * Plays a given sound through the default sound output port.
     * @param sound the file containing the sound.
     */
    void playSound(File sound);

    /**
     * Checks whether handsfree are plugged into the standard sound I/O port.
     * @return true if handsfree are plugged in, false otherwise.
     */
    boolean iSHandsfreePluggedIn();
}
