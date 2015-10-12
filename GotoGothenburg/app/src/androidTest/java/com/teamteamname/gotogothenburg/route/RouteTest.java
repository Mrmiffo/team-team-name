package com.teamteamname.gotogothenburg.route;

import android.test.ActivityInstrumentationTestCase2;

import com.teamteamname.gotogothenburg.activity.MainActivity;
import com.teamteamname.gotogothenburg.api.ElectricityAPI;
import com.teamteamname.gotogothenburg.api.mock.MockRequestQueue;
import com.teamteamname.gotogothenburg.map.Bus;

/**
 * Created by kakan on 2015-10-08.
 */
public class RouteTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Route route;
    private String testBusDwg;

    public RouteTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        testBusDwg = "Ericsson$Vin_Num_001";
        Bus.init();
        ElectricityAPI.init(getActivity(), new MockRequestQueue());
        route = new Route(Bus.getBusByDgw(testBusDwg));
    }

    @Override
    protected void tearDown() throws Exception {
        route = null;
        testBusDwg = null;
        System.gc();
    }

    public void testGetNextPOI() {
        route.electricityNextStopResponse(Stops.KUNGSPORTSPLATSEN);
        assertTrue(route.getNextPOI() == Stops.KUNGSPORTSPLATSEN.getPointOfInterests()[0]); //Make sure the first POI is the first POI


        route.electricityNextStopResponse(Stops.BRUNNSPARKEN);

        PointOfInterest poi_1 = route.getNextPOI();
        for (PointOfInterest kungsportsplatsenPOI: Stops.KUNGSPORTSPLATSEN.getPointOfInterests()) {
            assertFalse(poi_1.equals(kungsportsplatsenPOI)); //make sure that POIs from previous stop are not being stored.
        }
        assertTrue("poi_1 was null", poi_1 != null); //make sure a POI is retrived.


        PointOfInterest poi_2 = route.getNextPOI();
        assertFalse("poi_1 is equal to poi_2", poi_1.equals(poi_2)); //make sure the route is not doubled


        for (int i = 2; i<Stops.BRUNNSPARKEN.getPointOfInterests().length; i++) {
            route.getNextPOI();
        }
        assertTrue(route.getNextPOI() == null); //make sure no POI is retrived if all have been visited

    }
}
