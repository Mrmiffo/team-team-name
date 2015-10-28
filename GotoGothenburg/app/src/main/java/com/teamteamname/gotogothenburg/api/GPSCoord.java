package com.teamteamname.gotogothenburg.api;


import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by kakan on 2015-09-22.
 * A class defining a physical coordinate.
 */
@EqualsAndHashCode
public class GPSCoord {

    @Getter private final float latitude;
    @Getter private final float longitude;

    public GPSCoord(final float latitude, final float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
