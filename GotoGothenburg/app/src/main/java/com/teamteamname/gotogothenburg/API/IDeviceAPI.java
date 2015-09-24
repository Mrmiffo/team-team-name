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
    void playSound(File sound);
    boolean iSHandsfreePluggedIn();
    void handsfreeNotPluggedInPopUp();
}
