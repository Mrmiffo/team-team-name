package com.teamteamname.gotogothenburg.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;


/**
 * The FragmentAdapter class is used by the ViewPager to manage the fragments displayed in
 * the application.
 * Created by Anton on 2015-09-21.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    //The names of the Fragments as displayed in the application.
    private final String[] TITLES = { "Destinations", "Map", "Info", "Settings" };

    public FragmentAdapter(FragmentManager fm){
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        //Returns the available fragments. Hardcoded as these are not intended to be changed very often.
        Fragment toReturn = null;
        if (position == 0){
            toReturn = DestinationFragment.newInstance();
        } else if (position == 1){
            toReturn = MapFragment.newInstance();
        } else if (position == 2){
            toReturn = InfoFragment.newInstance();
        } else if (position == 3){
            toReturn = new SettingsFragment();
        }
        return toReturn;

    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }



}
