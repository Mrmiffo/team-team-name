package com.teamteamname.gotogothenburg.destination;

import android.test.ActivityInstrumentationTestCase2;

import com.teamteamname.gotogothenburg.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A test class used to verify that the SavedDestinations class is working as intended.
 * Created by Anton on 2015-09-30.
 */
public class SavedDestinationsTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private List<Destination> testDests;
    private SavedDestinations savedDests;
    private Destination dest1;
    private Destination dest2;
    private Destination dest3;

    public SavedDestinationsTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        testDests = new ArrayList<>();
        dest1 = new Destination("Test 1", 1,2);
        dest2 = new Destination("Test 2", 3,4, true);
        dest3 = new Destination("Test 3", 5,6);
        testDests.add(dest1);
        testDests.add(dest2);
        testDests.add(dest3);
        savedDests.init(new MockDestinationSaver());
        savedDests = SavedDestinations.getInstance();
    }

    protected void tearDown() throws Exception {
        testDests = null;
        for (Destination dest: savedDests.getSavedDestinations()) {
            savedDests.removeDestination(dest);
        }
        dest1 = null;
        dest2 = null;
        dest3 = null;
    }

    public void testAddDestination() throws Exception {
        for (Destination dest: testDests){
            savedDests.addDestination(dest);
            assertTrue("TestAddDestination failed: " + dest.toString() + " was not found.", savedDests.getSavedDestinations().contains(dest));
        }
    }

    public void testLoadAll() throws Exception{
        savedDests.loadDestinations(testDests);
        assertTrue("TestLoadAll failed: savedDestinations does not contain all loaded destinations", savedDests.getSavedDestinations().containsAll(testDests));
        List<Destination> testDests2 = new ArrayList<>();
        testDests2.add(new Destination("LoadTest", 5,6));
        testDests2.add(new Destination("LoadTest2", 7,8));
        savedDests.loadDestinations(testDests2);
        for (Destination dest: testDests){
            assertFalse("TestLoadAll failed: Saved destinations contains non-loaded objects", savedDests.getSavedDestinations().contains(dest));
        }
        assertTrue("TestLoadAll failed: SavedDestinations does not contain second load of data", savedDests.getSavedDestinations().containsAll(testDests2));

    }

    public void testGetSavedDestinations() throws Exception {
        savedDests.loadDestinations(testDests);
        assertTrue("Saved destinations does not contain all destinations", savedDests.getSavedDestinations().containsAll(testDests) && savedDests.getSavedDestinations().size() == 3);
    }

    public void testGetVisited() throws Exception {
        savedDests.loadDestinations(testDests);
        //Test getVisited(true)
        assertTrue("Get visited does not contain the visited destination", savedDests.getVisited(true).contains(dest2));
        assertFalse("Get visited contains none visited destination", savedDests.getVisited(true).contains(dest1));

        //Test getVisited(false)
        assertFalse("Get visited does not contain the visited destination", savedDests.getVisited(false).contains(dest2));
        assertTrue("Get visited contains none visited destination", savedDests.getVisited(false).contains(dest1));
        assertTrue("Get visited contains none visited destination", savedDests.getVisited(false).contains(dest3));
    }

    public void testRemoveDestination() throws Exception {
        savedDests.loadDestinations(testDests);
        savedDests.removeDestination(testDests.get(0));
        assertFalse("Saved destinations still contain all items", savedDests.getSavedDestinations().containsAll(testDests));
        assertFalse("Saved destinations still contain the removed item", savedDests.getSavedDestinations().contains(testDests.get(0)));
        assertTrue("Saved destinations has removed Test item 2", savedDests.getSavedDestinations().contains(testDests.get(1)));
        assertTrue("Saved destinations has removed Test item 3", savedDests.getSavedDestinations().contains(testDests.get(2)));
    }



}