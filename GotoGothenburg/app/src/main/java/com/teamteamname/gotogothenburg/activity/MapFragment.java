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

    public static final String ARG_POS = "ARG_POS";
    GoogleMap map;

    public MapFragment() {
        super();
    }

    public static MapFragment newInstance(int position){
        MapFragment toReturn = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS, position);
        toReturn.setArguments(args);
        return toReturn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
