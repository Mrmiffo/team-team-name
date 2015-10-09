package com.teamteamname.gotogothenburg.information;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

    private ArrayList<MarkerOptions> markers = new ArrayList<>();

    /**
     * Loads resale points from resale_points.txt and stores them as MarkerOptions
     *
     * @param context Context for loading the text file
     */
    public ResalePoints(Context context) {
        AssetManager assetManager = context.getAssets();
        try {

            Scanner scanner = new Scanner(assetManager.open("resale_points.txt"));
            while (scanner.hasNextLine()) {
                String[] values = scanner.nextLine().split("\\|");
                markers.add(
                        new MarkerOptions().title(values[0])
                                .position(new LatLng(Double.parseDouble(values[1]), Double.parseDouble(values[2])))
                );
            }
        } catch (IOException e) {
            Log.e(e.toString(),e.getMessage());
        }



    }

}
