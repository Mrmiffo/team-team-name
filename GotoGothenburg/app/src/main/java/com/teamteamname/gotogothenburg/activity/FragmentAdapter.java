package com.teamteamname.gotogothenburg.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.teamteamname.gotogothenburg.destination.DestinationScreen;
import com.teamteamname.gotogothenburg.information.InfoScreen;
import com.teamteamname.gotogothenburg.map.MapScreen;
import com.teamteamname.gotogothenburg.settings.SettingsScreen;


/**
 * The FragmentAdapter class is used by the ViewPager to manage the fragments displayed in
 * the application.
 * Created by Anton on 2015-09-21.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    //The names of the Fragments as displayed in the application.
    private final String[] TITLES = { "Destinations", "Map", "Info", "Settings" };

    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public FragmentAdapter(FragmentManager fm){
        super(fm);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {

        //Returns the available fragments. Hardcoded as these are not intended to be changed very often.
        Fragment toReturn;

        if (position == 0){
            toReturn = DestinationScreen.newInstance();
        } else if (position == 1){
            toReturn = MapScreen.newInstance();
        } else if (position == 2){
            toReturn = InfoScreen.newInstance();
        } else if (position == 3){
            toReturn = new SettingsScreen();
        } else {
            return null;
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
