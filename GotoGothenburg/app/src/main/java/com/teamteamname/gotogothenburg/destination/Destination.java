package com.teamteamname.gotogothenburg.destination;

import java.io.Serializable;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

//NOTE! JUnit tests has not been created for this class as all methods are auto-generated setters and getters with no logic. //Anton 2015-09-30

/**
 * The basic destination class containing a location, name, create date and a boolean for if it's
 * visited. This class is created when the user select a place on the map to save into the list of
 * destinations to visit. This class is not immutable as the user may want to change information
 * such as the name.
 * Created by Anton on 2015-09-30.
 */
@EqualsAndHashCode
public final class Destination{

    @Getter
    private String name;
    @Getter
    private double latitude;
    @Getter
    private double longitude;
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
