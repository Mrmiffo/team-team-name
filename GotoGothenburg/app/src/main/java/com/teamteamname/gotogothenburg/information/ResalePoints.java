package com.teamteamname.gotogothenburg.information;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamteamname.gotogothenburg.activity.MainActivity;
import com.teamteamname.gotogothenburg.map.MapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * Loads resale points from textfile and stores them as MarkerOptions
 *
 * Created by patrick on 08/10/2015.
 */
public class ResalePoints {

    private ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();
    Context context;

    /**
     * Loads resale points from resale_points.txt and stores them as MarkerOptions
     *
     * @param context Context for loading the text file
     */
    public ResalePoints(Context context) {
        this.context = context;
        AssetManager assetManager = context.getAssets();
        try {

            Scanner scanner = new Scanner(assetManager.open("resale_points.txt"));
            while (scanner.hasNextLine()) {
                String[] values = scanner.nextLine().split("\\|");
                markerOptions.add(
                        new MarkerOptions().title(values[0])
                                .position(new LatLng(Double.parseDouble(values[1]), Double.parseDouble(values[2])))
                );
            }
        } catch (IOException e) {
            Log.e(e.toString(),e.getMessage());
        }
    }

    public void drawResalePoints(){
        ((MainActivity) context).changeTab(1);
        for (MarkerOptions marker : markerOptions) {
            markers.add(((MapFragment) ((MainActivity) context).getCurrentTab()).placeMarker(marker));
        }
    }

    public void removeResalePoints(){
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }


}
