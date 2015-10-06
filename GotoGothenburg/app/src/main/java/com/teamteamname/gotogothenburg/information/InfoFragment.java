package com.teamteamname.gotogothenburg.information;

import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.map.Bus;
import com.teamteamname.gotogothenburg.map.IOnWhichBusListener;
import com.teamteamname.gotogothenburg.map.OnWhichBusIdentifier;


/**
 * Created by Anton on 2015-09-21.
 */
public class InfoFragment extends Fragment {
    public InfoFragment() {}

    public static InfoFragment newInstance(){
        InfoFragment toReturn = new InfoFragment();
        return toReturn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View toReturn = inflater.inflate(R.layout.fragment_info, container, false);
        return toReturn;
    }



}
