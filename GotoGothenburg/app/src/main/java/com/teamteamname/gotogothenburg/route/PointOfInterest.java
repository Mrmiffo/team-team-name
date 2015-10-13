package com.teamteamname.gotogothenburg.route;

import com.teamteamname.gotogothenburg.GPSCoord;

import java.io.File;
import java.io.Serializable;

import lombok.Getter;

/**
 * An enum representing a point of interest, complete with physical location, picture and information in both text and sound.
 * Created by kakan on 2015-09-28.
 */
public enum PointOfInterest implements Serializable {
    JOHANNEBERG_SCIENCE_PARK(new GPSCoord(57.684747f, 11.977967f), null, null, null),

    CHALMERS_CAMPUS_JOHANNEBERG(new GPSCoord(57.689167f, 11.973611f),

            new File(getAssetsDir() + "chalmers_campus_johanneberg_text_test.txt"),
            new File(getResDir() +  "chalmers_campus_johanneberg_sound_test.mp3"),
            new File(getResDir() + "chalmers_campus_johanneberg_picture_test.jpg")),

    LANDALA(new GPSCoord(57.693319f, 11.970892f), null, null, null),
    LANDSHÖVDINGEHUS(new GPSCoord(), null, null, null),                     //OBS
    MILJONPROGRAMMET(new GPSCoord(), null, null, null),                     //OBS
    POSEIDON(new GPSCoord(57.697206f, 11.979561f), null, null, null),
    GÖTEBORGS_KONSTMUSEUM(new GPSCoord(57.696389f, 11.980611f), null, null, null),
    STADSTEATERN(new GPSCoord(57.697444f, 11.980417f), null, null, null),
    GÖTEBORGS_KONSERTHUS(new GPSCoord(57.696694f, 11.978583f), null, null, null),
    KOPPARMÄRRA(new GPSCoord(57.704503f, 11.969636f), null, null, null),
    SALUHALLEN(new GPSCoord(57.703564f, 11.967867f), null, null, null),
    TURISTBYRÅN_KUNGSPORTSPLATSEN(new GPSCoord(57.704332f, 11.969843f), null, null, null),
    BRUNNSPARKEN(new GPSCoord(57.706875f, 11.969003f), null, null, null),
    SÅNINGSKVINNAN(new GPSCoord(57.706758f, 11.968575f), null, null, null),
    NORDSTAN(new GPSCoord(57.708803f, 11.968917f), null, null, null),
    TURISTBYRÅN_NORDSTAND(new GPSCoord(57.708444f, 11.969252f), null, null, null),
    ARKADEN(new GPSCoord(57.706389f, 11.969444f), null, null, null),
    NK(new GPSCoord(57.705695f, 11.968755f), null, null, null),
    DOMSTOLEN(new GPSCoord(), null, null, null),                            //OBS
    LÄPPSTIFTET(new GPSCoord(57.713197f, 11.967481f), null, null, null),
    NEW_DEVELOPMENT(new GPSCoord(), null, null, null),                      //OBS
    CHALMERS_CAMPUS_LINDHOLMEN(new GPSCoord(), null, null, null),           //OBS
    KUGGEN(new GPSCoord(57.706786f, 11.938908f), null, null, null),
    LINDHOLMEN_SCIENCE_PARK(new GPSCoord(57.707f, 11.94f), null, null, null),
    INOMHUSHÅLLPLATSEN(new GPSCoord(57.706911f, 11.93721f), null, null, null);


    /**
     * Returns the path for the resource directory. Please note that text files should be put in assets.
     * @return the path for the resource directory.
     */
    private static String getResDir() {
        return "src/main/res/raw/";
    }

    /**
     * Returns the path for the assets directory.
     * @return the path for the assets directory.
     */
    private static String getAssetsDir() {
        return "src/main/assets/";
    }

    @Getter private final GPSCoord location;
    @Getter private File textGuide;
    @Getter private File soundGuide;
    @Getter private File picture;

    PointOfInterest(final GPSCoord location, File textGuide, File soundGuide, File picture) {
        this.location = location;
        this.textGuide = textGuide;
        this.soundGuide = soundGuide;
        this.picture = picture;

        if (textGuide == null) {
            this.textGuide = new File(getAssetsDir() + "null_text.txt");
        }

        if (picture == null) {
            this.picture = new File(getResDir() + "null_picture.jpg");
        }
    }

    public String getName() {
        String name = this.toString();
        name = name.replaceAll("_", " "); //replace non-word characters with a space.
        return name.substring(0, 1).toUpperCase() + name.toString().substring(1).toLowerCase(); //Capitalise first letter only
    }
}