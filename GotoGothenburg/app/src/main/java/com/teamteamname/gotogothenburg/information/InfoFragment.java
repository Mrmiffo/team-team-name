package com.teamteamname.gotogothenburg.information;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamteamname.gotogothenburg.R;


/**
 * Created by Anton on 2015-09-21.
 */
public class InfoFragment extends Fragment {

    public InfoFragment() {}

    public static InfoFragment newInstance(){
        return new InfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }



}
