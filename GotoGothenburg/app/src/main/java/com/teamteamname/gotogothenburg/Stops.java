package com.teamteamname.gotogothenburg;

/**
 * An enum containing all valid stops for buses, tram etc.
 * Created by kakan on 2015-09-25.
 */
public enum Stops {
    //Note that only stops located along line 55 are included in the prototype.
    SVEN_HULTINGS_GATA(new PointOfInterest[] {
            PointOfInterest.JOHANNEBERG_SCIENCE_PARK
    }),
    CHALMERSPLATSEN(new PointOfInterest[] {
            PointOfInterest.CHALMERS_CAMPUS_JOHANNEBERG
    }),
    KAPELLPLATSEN(new PointOfInterest[] {
            PointOfInterest.LANDALA,
            PointOfInterest.LANDSHÖVDINGEHUS,
            PointOfInterest.MILJONPROGRAMMET
    }),
    GÖTAPLATSEN(new PointOfInterest[] {
            PointOfInterest.POSEIDON,
            PointOfInterest.GÖTEBORGS_KONSTMUSEUM,
            PointOfInterest.STADSTEATERN,
            PointOfInterest.GÖTEBORGS_KONSERTHUS
    }),
    VALAND(new PointOfInterest[] {}),
    KUNGSPORTSPLATSEN(new PointOfInterest[] {
            PointOfInterest.KOPPARMÄRRA,
            PointOfInterest.SALUHALLEN,
            PointOfInterest.TURISTBYRÅN_KUNGSPORTSPLATSEN
    }),
    BRUNNSPARKEN(new PointOfInterest[] {PointOfInterest.BRUNNSPARKEN,
            PointOfInterest.SÅNINGSKVINNAN,
            PointOfInterest.NORDSTAN,
            PointOfInterest.TURISTBYRÅN_NORDSTAND,
            PointOfInterest.ARKADEN,
            PointOfInterest.NK,
            PointOfInterest.DOMSTOLEN
    }),
    LILLA_BOMMEN(new PointOfInterest[] {
            PointOfInterest.LÄPPSTIFTET
    }),
    FRIHAMNSPORTEN(new PointOfInterest[] {
            PointOfInterest.NEW_DEVELOPMENT
    }),
    PUMPGATAN(new PointOfInterest[] {}),
    REGNBÅGSGATAN(new PointOfInterest[] {}),
    LINDHOLMEN(new PointOfInterest[] {
            PointOfInterest.CHALMERS_CAMPUS_LINDHOLMEN,
            PointOfInterest.KUGGEN,
            PointOfInterest.LINDHOLMEN_SCIENCE_PARK
    }),
    TEKNIKGATAN(new PointOfInterest[] {
            PointOfInterest.INOMHUSHÅLLPLATSEN
    }),
    LINDHOLMSPLATSEN(new PointOfInterest[] {}),
    DEFAULT(null);


    private PointOfInterest[] POIs;

    Stops(PointOfInterest[] POIs) {
        this.POIs = POIs;
    }

    public PointOfInterest[] getPOIs() {
        return POIs.clone();
    }
}
