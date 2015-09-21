package com.teamteamname.gotogothenburg;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by Anton on 2015-09-21.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment destinationFragment = new DestinationFragment();
        Fragment mapFragment = new Fragment();
        Fragment infoFragment = new Fragment();

        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
