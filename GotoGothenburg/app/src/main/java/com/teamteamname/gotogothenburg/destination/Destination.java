package com.teamteamname.gotogothenburg.destination;

import lombok.EqualsAndHashCode;
import lombok.Getter;

//NOTE! JUnit tests has not been created for this class as all methods are auto-generated setters and getters with no logic. //Anton 2015-09-30

/**
 * The destination class containing a location, nameand a boolean for if it has been
 * visited. This is an immutable final class in order to make sure that the save functionality
 * remain intact. To modify a destination, please use the copy constructor.
 * This class is created when the user select a place on the map to save into the list of
 * destinations to visit. This class is not immutable as the user may want to change information
 * such as the name.
 * Created by Anton on 2015-09-30.
 */
@EqualsAndHashCode
public final class Destination{

    @Getter
    final private String name;
    @Getter
    final private double latitude;
    @Getter
    final private double longitude;
    @Getter
    private boolean visited;

    public Destination(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        visited = false;
    }

    public Destination(String name, double latitude, double longitude, boolean visited){
        this(name,latitude,longitude);
        this.visited = visited;
    }

    @Override
    public String toString(){
        return getName() + "At: " + latitude + ", " + longitude + "Visited: " + visited;
    }


}
