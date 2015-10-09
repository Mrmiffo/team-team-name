package com.teamteamname.gotogothenburg.guide;

import android.test.ActivityInstrumentationTestCase2;

import com.teamteamname.gotogothenburg.PointOfInterest;
import com.teamteamname.gotogothenburg.Stops;
import com.teamteamname.gotogothenburg.activity.MainActivity;
import com.teamteamname.gotogothenburg.api.ElectricityAPI;
import com.teamteamname.gotogothenburg.api.mock.MockRequestQueue;
import com.teamteamname.gotogothenburg.map.Bus;

/**
 * Created by kakan on 2015-10-08.
 */
public class GuideTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Guide guide;
    private String testBusDwg;

    public GuideTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        testBusDwg = "Ericsson$Vin_Num_001";
        Bus.init();
        ElectricityAPI.init(getActivity(), new MockRequestQueue());
        guide = new Guide(Bus.getBusByDgw(testBusDwg));
    }

    @Override
    protected void tearDown() throws Exception {
        guide = null;
        testBusDwg = null;
        System.gc();
    }

    public void testGetNextPOI() {
        guide.electricityNextStopResponse(Stops.KUNGSPORTSPLATSEN);
        assertTrue(guide.getNextPOI() == Stops.KUNGSPORTSPLATSEN.getPointOfInterests()[0]); //Make sure the first POI is the first POI


        guide.electricityNextStopResponse(Stops.BRUNNSPARKEN);

        PointOfInterest poi_1 = guide.getNextPOI();
        for (PointOfInterest kungsportsplatsenPOI: Stops.KUNGSPORTSPLATSEN.getPointOfInterests()) {
            assertFalse(poi_1.equals(kungsportsplatsenPOI)); //make sure that POIs from previous stop are not being stored.
        }
        assertTrue("poi_1 was null", poi_1 != null); //make sure a POI is retrived.


        PointOfInterest poi_2 = guide.getNextPOI();
        assertFalse("poi_1 is equal to poi_2", poi_1.equals(poi_2)); //make sure the guide is not doubled


        for (int i = 2; i<Stops.BRUNNSPARKEN.getPointOfInterests().length; i++) {
            guide.getNextPOI();
        }
        assertTrue(guide.getNextPOI() == null); //make sure no POI is retrived if all have been visited

    }
}
