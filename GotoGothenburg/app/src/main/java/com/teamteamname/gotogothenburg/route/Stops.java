package com.teamteamname.gotogothenburg.route;

/**
 * An enum containing all valid stops for buses, tram etc.
 * Created by kakan on 2015-09-25.
 */
public enum Stops {
    //Note that only stops located along line 55 are included in the prototype.
    SVEN_HULTINGS_GATA(
            PointOfInterest.JOHANNEBERG_SCIENCE_PARK
    ),
    CHALMERSPLATSEN(
            PointOfInterest.CHALMERS_CAMPUS_JOHANNEBERG
    ),
    KAPELLPLATSEN(
            PointOfInterest.LANDALA,
            PointOfInterest.LANDSHÖVDINGEHUS,
            PointOfInterest.MILJONPROGRAMMET
    ),
    GÖTAPLATSEN(
            PointOfInterest.POSEIDON,
            PointOfInterest.GÖTEBORGS_KONSTMUSEUM,
            PointOfInterest.STADSTEATERN,
            PointOfInterest.GÖTEBORGS_KONSERTHUS
    ),
    VALAND(),
    KUNGSPORTSPLATSEN(
            PointOfInterest.KOPPARMÄRRA,
            PointOfInterest.SALUHALLEN,
            PointOfInterest.TURISTBYRÅN_KUNGSPORTSPLATSEN
    ),
    BRUNNSPARKEN(
            PointOfInterest.BRUNNSPARKEN,
            PointOfInterest.SÅNINGSKVINNAN,
            PointOfInterest.NORDSTAN,
            PointOfInterest.TURISTBYRÅN_NORDSTAND,
            PointOfInterest.ARKADEN,
            PointOfInterest.NK,
            PointOfInterest.DOMSTOLEN
    ),
    LILLA_BOMMEN(
            PointOfInterest.LÄPPSTIFTET
    ),
    FRIHAMNSPORTEN(
            PointOfInterest.NEW_DEVELOPMENT
    ),
    PUMPGATAN(),
    REGNBÅGSGATAN(),
    LINDHOLMEN(
            PointOfInterest.CHALMERS_CAMPUS_LINDHOLMEN,
            PointOfInterest.KUGGEN,
            PointOfInterest.LINDHOLMEN_SCIENCE_PARK
    ),
    TEKNIKGATAN(
            PointOfInterest.INOMHUSHÅLLPLATSEN
    ),
    LINDHOLMSPLATSEN(),
    DEFAULT();


    private PointOfInterest[] pointOfInterests;

    Stops(PointOfInterest... pointOfInterests) {
        this.pointOfInterests = pointOfInterests.clone();
    }

    public PointOfInterest[] getPointOfInterests() {
        return pointOfInterests.clone();
    }
}
