package com.teamteamname.gotogothenburg.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * The FragmentAdapter class is used by the PagerSlider to manage the fragments for displaying in
 * the application.
 * Created by Anton on 2015-09-21.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = { "Destinations", "Map", "Info" };

    List<Fragment> fragmentList;

    public FragmentAdapter(FragmentManager fm){
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentList.add(new DestinationFragment());
        fragmentList.add(MapFragment.newInstance());
        fragmentList.add(new InfoFragment());

    }

    @Override
    public Fragment getItem(int position) {
        /*Log.d("getItem", "Position: " + position);
        Fragment toReturn = null;
        if (position == 0){
            toReturn = DestinationFragment.newInstance(0);
        } else if (position == 1){
            toReturn = MapFragment.newInstance(1);
        } else if (position == 2){
            toReturn = InfoFragment.newInstance(2);
        }
        return toReturn;
        */
        return fragmentList.get(position);

    }
    @Override
    public int getItemPosition(Object o){
        for (int i = 0; i < fragmentList.size(); i++){
            if (fragmentList.get(i) == o){
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getCount() {
        //return TITLES.length;
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }



}
