package com.teamteamname.gotogothenburg.sound;

import android.test.ActivityInstrumentationTestCase2;

import com.teamteamname.gotogothenburg.API.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.activity.MainActivity;

import junit.framework.TestCase;

/**
 * Created by Anton on 2015-09-25.
 */
public class AudioNodeTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private AndroidDeviceAPI androidDeviceAPI;

    public AudioNodeTest() {
        super(MainActivity.class);
    }

    public void setUp(){
        androidDeviceAPI = new AndroidDeviceAPI(getActivity());

    }
}