package API;

import android.app.Activity;
import android.test.ActivityUnitTestCase;

/**
 * Created by Anton on 2015-09-24.
 */
public class AndroidDeviceAPITest extends ActivityUnitTestCase {

    public AndroidDeviceAPITest(Class activityClass) {
        super(activityClass);
    }

    //Tests if the wifiRouter mac function "works" note that the emulator can't handle wifi, thus tests for wifi returns only null.
    public void testGetWiFiRouterMAC() throws Exception {
        Activity mActivity = getActivity();
        AndroidDeviceAPI androidDeviceAPI = new AndroidDeviceAPI(mActivity);

        assertTrue(androidDeviceAPI.getWiFiRouterMAC() == null);

    }
}