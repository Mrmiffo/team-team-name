package com.teamteamname.gotogothenburg.api;

/**
 * An interface implemented by the class which will receive the response from the IBusStatus API.
 * Created by Anton on 2015-10-01.
 */
public interface IElectriCityWiFiSystemIDAPIHandler {
    /**
     * A method to get the systemID of the wifi router on the bus which the user is connected to.
     * Only works on ElectriCity.
     * @param returnValue the SystemID of the wifi on the bus which the user is connected to.
     *                    Returns null if an error occur, such as an invalid wifi.
     */
    void getConnectedBusSystemIDCallback(String returnValue);

    /**
     * Return an error to the handler that occured when executing the HTTP requst and parsing.
     * Malformed, Parser and SAXExceotion error should be reported to the developers. IOExceptions
     * should inform users that system was unable to read from the bus wifi and they should try again.
     * @param e instance of MalformedURLException, ParserConfigurationExc, SAXException or IOException.
     */
    void getConnectedBusError(Exception e);
}
