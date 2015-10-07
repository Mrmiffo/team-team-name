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
public class Destination implements Serializable{

    public static final long serialVersionUID = 2894;

    @Getter @Setter
    private String name;
    @Getter @Setter
    private double latitude;
    @Getter @Setter
    private double longitude;
    @Getter
    private Date dateCreated;
    @Getter @Setter
    private boolean visited;

    public Destination(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        dateCreated = new Date();
        visited = false;
    }

    @Override
    public String toString(){
        return getName() + "At: " + latitude + ", " + longitude + "Visited: " + visited + "Created: " + dateCreated;
    }

}
