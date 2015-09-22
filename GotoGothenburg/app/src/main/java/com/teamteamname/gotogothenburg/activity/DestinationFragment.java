package com.teamteamname.gotogothenburg.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamteamname.gotogothenburg.R;

/**
 * Created by Anton on 2015-09-21.
 */
public class DestinationFragment extends Fragment {

    public static final String ARG_POS = "ARG_POS";

    private int mPage;

    public DestinationFragment() {
        super();
    }

    public static DestinationFragment newInstance(int position){
        DestinationFragment toReturn = new DestinationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS, position);
        toReturn.setArguments(args);
        return toReturn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_POS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }
}
