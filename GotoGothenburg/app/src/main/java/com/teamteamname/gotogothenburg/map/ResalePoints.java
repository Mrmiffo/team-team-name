package com.teamteamname.gotogothenburg.map;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Loads ticket resale points from textfile and stores them as MarkerOptions
 * Created by patrick on 08/10/2015.
 */
public class ResalePoints implements IMapMarkerData{

    final private List<MarkerOptions> markerOptions = new ArrayList<>();
    final private List<Marker> markers = new ArrayList<>();

    /**
     * Loads resale points from resale_points.txt and stores them as MarkerOptions
     *
     * @param context Context for loading the text file
     */
    public ResalePoints(Context context) {
        final AssetManager assetManager = context.getAssets();
        try {

            final Scanner scanner = new Scanner(assetManager.open("resale_points.txt"), "UTF-8");
            while (scanner.hasNextLine()) {
                final String[] values = scanner.nextLine().split("\\|");
                markerOptions.add(
                        new MarkerOptions().title(values[0])
                                .position(new LatLng(Double.parseDouble(values[1]), Double.parseDouble(values[2])))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .snippet("Directions")
                );
            }
        } catch (IOException e) {
            Log.e(e.toString(),e.getMessage());
        }
    }


    @Override
    public void addMarkers(IMap map) {
        for (final MarkerOptions marker : markerOptions) {
            markers.add(map.placeMarker(marker));
        }
    }

    @Override
    public void removeMarkers() {
            for (final Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
    }
}
