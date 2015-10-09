package com.teamteamname.gotogothenburg.activity;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.SearchView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.astuetz.PagerSlidingTabStrip;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.api.ElectriCityWiFiSystemIDAPI;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.VasttrafikAPI;
import com.teamteamname.gotogothenburg.map.Bus;
import com.teamteamname.gotogothenburg.map.MapFragment;
import com.teamteamname.gotogothenburg.map.OnWhichBusIdentifier;

public class MainActivity extends FragmentActivity{

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(new FragmentAdapter(getFragmentManager()));
        this.viewPager = pager;

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip pager_title_strip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager_title_strip.setViewPager(pager);

        // Initialize the APIs
        Cache cache = new DiskBasedCache(getCacheDir(),1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache,network);
        VasttrafikAPI.init(this, queue);
        queue.start();
        ElectriCityWiFiSystemIDAPI.initialize();
        OnWhichBusIdentifier.initialize();
        AndroidDeviceAPI.initialize(this);
        LocationServicesAPI.init(this);

        Bus.init();

        // Adding listeners to searchbar
        SearchView searchBar = (SearchView)findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchbarListener(this, searchBar));
        SearchManager manager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        searchBar.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
    }

    @Override
    public void onStop(){
        super.onStop();
        LocationServicesAPI.getInstance().disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        if (requestCode == LocationServicesAPI.REQUEST_RESOLVE_ERROR) {
            LocationServicesAPI.getInstance().resolutionResult(resultCode, data);
        }
    }

    public void changeTab(int index){
        viewPager.setCurrentItem(index, true);
    }

    public Fragment getCurrentTab(){
        return ((FragmentAdapter)viewPager.getAdapter()).getRegisteredFragment(viewPager.getCurrentItem());
    }
}
