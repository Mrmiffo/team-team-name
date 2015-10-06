package com.teamteamname.gotogothenburg.map;

/**
 * A class that implements this interface may register to
 * listen to the OnWhichBUsIdentifier class and will be called
 * when the OnWhichBusIdentifier registers that the user is on a buss.
 * Created by Anton on 2015-10-02.
 */
public interface IOnWhichBusListener {
    /**
     * This method will be called continually when the OnWhichBusIdentifier
     * finds a bus that the user is on.
     * @param busUserIsOn
     */
    void whichBussCallBack(Bus busUserIsOn);

    /**
     * Will be called if the user is not connected to an electricity network.
     */
    void notConnectedToElectriCityWifiError();

    /**
     * WIll be called if the system was unable to identify a bus from the wifi. This could be due to
     * a read error or that no buss with the system id exists.
     */
    void unableToIdentifyBusError();
}
