package com.teamteamname.gotogothenburg;

import java.io.File;

/**
 * Created by kakan on 2015-09-28.
 */
public enum PointOfInterest {
    JOHANNEBERG_SCIENCE_PARK(new GPSCoord(57.684747f, 11.977967f)),
    CHALMERS_CAMPUS_JOHANNEBERG(new GPSCoord(57.689167f, 11.973611f)),
    LANDALA(new GPSCoord(57.693319f, 11.970892f)),
    LANDSHÖVDINGEHUS(new GPSCoord()),
    MILJONPROGRAMMET(new GPSCoord()),
    POSEIDON(new GPSCoord(57.697206f, 11.979561f)),
    GÖTEBORGS_KONSTMUSEUM(new GPSCoord(57.696389f, 11.980611f)),
    STADSTEATERN(new GPSCoord(57.697444f, 11.980417f)),
    GÖTEBORGS_KONSERTHUS(new GPSCoord(57.696694f, 11.978583f)),
    KOPPARMÄRRA(new GPSCoord(57.704503f, 11.969636f)),
    SALUHALLEN(new GPSCoord(57.703564f, 11.967867f)),
    TURISTBYRÅN_KUNGSPORTSPLATSEN(new GPSCoord(57.704332f, 11.969843f)),
    BRUNNSPARKEN(new GPSCoord(57.706875f, 11.969003f)),
    SÅNINGSKVINNAN(new GPSCoord(57.706758f, 11.968575f)),
    NORDSTAN(new GPSCoord(57.708803f, 11.968917f)),
    TURISTBYRÅN_NORDSTAND(new GPSCoord(57.708444f, 11.969252f)),
    ARKADEN(new GPSCoord(57.706389f, 11.969444f)),
    NK(new GPSCoord(57.705695f, 11.968755f)),
    DOMSTOLEN(new GPSCoord()),
    LÄPPSTIFTET(new GPSCoord(57.713197f, 11.967481f)),
    NEW_DEVELOPMENT(new GPSCoord()),
    CHALMERS_CAMPUS_LINDHOLMEN(new GPSCoord()),
    KUGGEN(new GPSCoord(57.706786f, 11.938908f)),
    LINDHOLMEN_SCIENCE_PARK(new GPSCoord(57.707f, 11.94f)),
    INOMHUSHÅLLPLATSEN(new GPSCoord(57.706911f, 11.93721f));

    private final GPSCoord location;
    private File textGuide;
    private File soundGuide;
    private File picture;

    PointOfInterest(final GPSCoord location) {
        this.location = location;

    }
}
