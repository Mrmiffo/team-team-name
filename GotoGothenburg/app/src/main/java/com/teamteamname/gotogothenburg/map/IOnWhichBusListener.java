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
    void notConnectedToElectriCityWifiError();
}
