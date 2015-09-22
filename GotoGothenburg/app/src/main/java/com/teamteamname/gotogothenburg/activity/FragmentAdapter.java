package com.teamteamname.gotogothenburg.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * The FragmentAdapter class is used by the PagerSlider to manage the fragments for displaying in
 * the application.
 * Created by Anton on 2015-09-21.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    private final String[] TITLES = { "Destinations", "Map", "Info" };

    public FragmentAdapter(FragmentManager fm){
        super(fm);
        fragmentList = new ArrayList<>();
        Fragment destinationFragment = new DestinationFragment();
        Fragment mapFragment = new MapFragment();
        Fragment infoFragment = new InfoFragment();
        fragmentList.add(destinationFragment);
        fragmentList.add(mapFragment);
        fragmentList.add(infoFragment);


    }

    @Override
    public Fragment getItem(int position) {
        Log.d("getItem", "Position: " + position);
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }



}
