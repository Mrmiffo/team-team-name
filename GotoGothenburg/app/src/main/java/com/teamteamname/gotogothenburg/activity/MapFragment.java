package com.teamteamname.gotogothenburg.activity;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamteamname.gotogothenburg.R;


/**
 * Created by Anton on 2015-09-21.
 */
public class MapFragment extends Fragment {

    public static final String ARG_POS = "ARG_POS";

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }
}
