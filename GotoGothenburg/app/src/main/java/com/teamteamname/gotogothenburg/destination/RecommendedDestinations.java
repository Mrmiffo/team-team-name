package com.teamteamname.gotogothenburg.destination;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a list of Receommended destinations.
 * In it's current state it simply contain a list of destinations recommended
 * by the developers. In the future it will be used to gather destination
 * recommendations from different sources.
 * Created by Anton on 2015-10-15.
 */
public class RecommendedDestinations {
    private List<Destination> recommendedDestinations;
    private static RecommendedDestinations instance;

    private RecommendedDestinations(){
        recommendedDestinations = new ArrayList<>();
        recommendedDestinations.add(new Destination("Liseberg",57.697115, 11.990874));
        recommendedDestinations.add(new Destination("Skansen kronan", 57.695939, 11.955199));
        recommendedDestinations.add(new Destination("Sjöfartsmuseet Akvariet",57.699112, 11.932274));
        recommendedDestinations.add(new Destination("Universeum", 57.695836, 11.988330));
        recommendedDestinations.add(new Destination("Världskulturmuseet", 57.694582, 11.989076));
        recommendedDestinations.add(new Destination("Hagakyrkan",57.698872, 11.962107));
        recommendedDestinations.add(new Destination("Avenyn",57.697371, 11.979325));
        recommendedDestinations.add(new Destination("Nordstan", 57.707279, 11.969752));
    }

    public synchronized static RecommendedDestinations getInstance(){
        if (instance == null){
            instance = new RecommendedDestinations();
        }
        return instance;
    }

    public List<Destination> getRecommendedDestinations(){
        List<Destination> toReturn = new ArrayList<>();
        toReturn.addAll(recommendedDestinations);
        return toReturn;
    }


}
