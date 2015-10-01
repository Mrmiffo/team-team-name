package com.teamteamname.gotogothenburg.destination;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * A test class used to verify that the SavedDestinations class is working as intended.
 * Created by Anton on 2015-09-30.
 */
public class SavedDestinationsTest extends TestCase {
    List<Destination> testDests;
    SavedDestinations savedDests1;
    Destination dest1;
    Destination dest2;
    Destination dest3;

    protected void setUp() throws Exception {
        testDests = new ArrayList<>();
        dest1 = new Destination("Test 1", 1,2);
        dest2 = new Destination("Test 2", 3,4);
        dest3 = new Destination("Test 3", 5,6);
        dest2.setVisited(true);
        testDests.add(dest1);
        testDests.add(dest2);
        testDests.add(dest3);
        savedDests1 = new SavedDestinations();
    }

    protected void tearDown() throws Exception {
        testDests = null;
        savedDests1 = null;
        dest1 = null;
        dest2 = null;
        dest3 = null;
    }

    public void testGetSavedDestinations() throws Exception {
        savedDests1 = new SavedDestinations(testDests);
        assertTrue("Saved destinations does not contain all detinations", savedDests1.getSavedDestinations().containsAll(testDests));
    }

    public void testAddDestination() throws Exception {
        for (Destination dest: testDests){
            savedDests1.addDestination(dest);
            assertTrue("TestAddDestination failed: " + dest.toString() + " was not found.", savedDests1.getSavedDestinations().contains(dest));
        }
    }

    public void testRemoveDestination() throws Exception {
        savedDests1 = new SavedDestinations(testDests);
        savedDests1.removeDestination(testDests.get(0));
        assertFalse("Saved destinations still contain all items", savedDests1.getSavedDestinations().containsAll(testDests));
        assertFalse("Saved destinations still contain the removed item", savedDests1.getSavedDestinations().contains(testDests.get(0)));
        assertTrue("Saved destinations has removed Test item 2", savedDests1.getSavedDestinations().contains(testDests.get(1)));
        assertTrue("Saved destinations has removed Test item 3", savedDests1.getSavedDestinations().contains(testDests.get(2)));
    }

    public void testGetVisited() throws Exception {
        savedDests1 = new SavedDestinations(testDests);
        dest3.setVisited(true);
        //Test getVisited(true)
        assertTrue("Get visited does not contain the visited destination", savedDests1.getVisited(true).contains(dest2));
        assertFalse("Get visited contains none visited destination", savedDests1.getVisited(true).contains(dest1));
        assertTrue("Get visited contains none visited destination", savedDests1.getVisited(true).contains(dest3));

        //Test getVisited(false)
        assertFalse("Get visited does not contain the visited destination", savedDests1.getVisited(false).contains(dest2));
        assertTrue("Get visited contains none visited destination", savedDests1.getVisited(false).contains(dest1));
        assertFalse("Get visited contains none visited destination", savedDests1.getVisited(false).contains(dest3));
    }

}