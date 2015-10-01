package com.teamteamname.gotogothenburg.API;

import android.test.ActivityInstrumentationTestCase2;

import com.teamteamname.gotogothenburg.activity.MainActivity;


/**
 * Created by Anton on 2015-09-25.
 */
public class AndroidDeviceAPITest extends ActivityInstrumentationTestCase2<MainActivity> {



    private MainActivity mActivity;
    private AndroidDeviceAPI androidDeviceAPI;

    public AndroidDeviceAPITest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp(){
        mActivity = getActivity();
        AndroidDeviceAPI.getInstance().initialize(mActivity);
        androidDeviceAPI = AndroidDeviceAPI.getInstance();
    }

    public void testPreconditions(){
        assertNotNull("Activity is null", mActivity);
        assertNotNull("DeviceAPI is null", androidDeviceAPI);
    }

    // As we can't anticipate if the test will run on simulator (which doesn't have wifi) or on a
    // phone (on which we can't know the wifi it's connected to) this will only be tested with unit
    // test and has been tested manually.
    public void testGetWiFiRouterMAC() throws Exception {
        assertNull("WiFimac is: " + androidDeviceAPI.getWiFiRouterMAC(), androidDeviceAPI.getWiFiRouterMAC());

    }

    public void testGetConnectedWifiSSID() throws Exception {
       //assertNull("WifiSSID is: " + androidDeviceAPI.getConnectedWifiSSID(), androidDeviceAPI.getConnectedWifiSSID());
    }

    public void testConnectToOpenWifi() throws Exception {

    }

    public void testConnectedToWifi() throws Exception {

    }
}