package com.teamteamname.gotogothenburg;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * An enum containing all valid lines (e.g. bus 55, tram 8), complete with stops and nearby points of interests.
 * Created by kakan on 2015-09-25.
 */
public enum Lines {

    //Note that only line bus 55 is included in the prototype.
    BUS_55(new Stops[] {
            Stops.Sven_Hultings_Gata,
            Stops.Chalmersplatsen,
            Stops.Kapellplatsen,
            Stops.Götaplatsen,
            Stops.Valand,
            Stops.Kungsportsplatsen,
            Stops.Brunnsparken,
            Stops.Lilla_Bommen,
            Stops.Frihamnsporten,
            Stops.Pumpgatan,
            Stops.Regnbågsgatan,
            Stops.Lindholmen,
            Stops.Teknikgatan,
            Stops.Lindholmsplatsen
    }, new PointOfInterest[] {
            PointOfInterest.Johanneberg_Science_Park,
            PointOfInterest.Chalmers,
            PointOfInterest.Landala,
            PointOfInterest.Poseidon,
            PointOfInterest.Göteborgs_konstmuseum,
            PointOfInterest.Stadsteatern,
            PointOfInterest.Göteborgs_konserthus,
            PointOfInterest.Kopparmärra,
            PointOfInterest.Såningskvinnan,
            PointOfInterest.Nordstan,
            PointOfInterest.Arkaden,
            PointOfInterest.Domstolen,
            PointOfInterest.Läppstiftet,
            PointOfInterest.Eventyta,
            PointOfInterest.Kuggen,
            PointOfInterest.Lindholmen_Science_Park,
            PointOfInterest.Inomhushållplatsen
    });


    @Getter private Stops[] lineStops;
    @Getter private PointOfInterest[] linePOIs;

    Lines(Stops[] lineStops, PointOfInterest[] linePOIs) {
        this.lineStops = lineStops;
        this.linePOIs = linePOIs;
    }
}
