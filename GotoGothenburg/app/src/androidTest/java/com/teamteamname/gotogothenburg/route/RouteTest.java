package com.teamteamname.gotogothenburg.route;

import android.test.ActivityInstrumentationTestCase2;

import com.teamteamname.gotogothenburg.activity.MainActivity;
import com.teamteamname.gotogothenburg.api.ApiFactory;
import com.teamteamname.gotogothenburg.api.PointOfInterest;
import com.teamteamname.gotogothenburg.api.Stops;
import com.teamteamname.gotogothenburg.api.Bus;
import com.teamteamname.gotogothenburg.guide.Route;

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
        super.setUp();
        testBusDwg = "Ericsson$Vin_Num_001";
        Bus.init();
        ApiFactory.init(getActivity());
        route = new Route(Bus.getBusByDgw(testBusDwg));
    }

    public void testGetNextPOI() {
        route.electricityNextStopResponse(Stops.KUNGSPORTSPLATSEN);
        assertTrue(route.getNextPOI() == Stops.KUNGSPORTSPLATSEN.getPointOfInterests()[0]); //Make sure the first POI is the first POI


        route.electricityNextStopResponse(Stops.BRUNNSPARKEN);

        final PointOfInterest poi_1 = route.getNextPOI();
        for (final PointOfInterest kungsportsplatsenPOI: Stops.KUNGSPORTSPLATSEN.getPointOfInterests()) {
            assertFalse(poi_1.equals(kungsportsplatsenPOI)); //make sure that POIs from previous stop are not being stored.
        }
        assertTrue("poi_1 was null", poi_1 != null); //make sure a POI is retrived.


        final PointOfInterest poi_2 = route.getNextPOI();
        assertFalse("poi_1 is equal to poi_2", poi_1.equals(poi_2)); //make sure the route is not doubled


        for (int i = 2; i<Stops.BRUNNSPARKEN.getPointOfInterests().length; i++) {
            route.getNextPOI();
        }
        assertTrue(route.getNextPOI() == null); //make sure no POI is retrived if all have been visited

    }
}
