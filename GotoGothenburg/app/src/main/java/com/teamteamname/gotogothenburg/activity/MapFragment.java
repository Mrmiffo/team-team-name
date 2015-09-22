package com.teamteamname.gotogothenburg.activity;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.teamteamname.gotogothenburg.R;


/**
 * Created by Anton on 2015-09-21.
 */
public class MapFragment extends com.google.android.gms.maps.MapFragment implements OnMapReadyCallback {

    GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        getMapAsync(this);
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
