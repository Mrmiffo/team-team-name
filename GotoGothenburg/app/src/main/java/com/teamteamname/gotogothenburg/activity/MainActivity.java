package com.teamteamname.gotogothenburg.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.api.ElectriCityWiFiSystemIDAPI;
import com.teamteamname.gotogothenburg.api.electricity.ElectricityAPI;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikTripHandler;
import com.teamteamname.gotogothenburg.destination.DestinationSaver;
import com.teamteamname.gotogothenburg.destination.SavedDestinations;
import com.teamteamname.gotogothenburg.guide.GuideHandler;
import com.teamteamname.gotogothenburg.map.Bus;
import com.teamteamname.gotogothenburg.map.MapFragment;
import com.teamteamname.gotogothenburg.map.OnWhichBusIdentifier;

public class MainActivity extends FragmentActivity implements VasttrafikTripHandler, VasttrafikErrorHandler{

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
        ElectricityAPI.init(this, queue);
        queue.start();
        ElectriCityWiFiSystemIDAPI.initialize();
        OnWhichBusIdentifier.init();
        AndroidDeviceAPI.initialize(this);
        LocationServicesAPI.init(this);
        GuideHandler.init(this);


        Bus.init();

        // Adding listeners to searchbar
        SearchView searchBar = (SearchView)findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchbarListener(this, searchBar));
        SearchManager manager = (SearchManager) getSystemService(Activity.SEARCH_SERVICE);
        searchBar.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        //Fill the saved destinations object with data from the database.
        //Create a saver for the SavedDestinations
        final DestinationSaver saver = new DestinationSaver(this);
        SavedDestinations.init(saver);
        AsyncTask loadDest = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                //Initialize the SavedDestinations with destinations from the database. (This must run in background)
                SavedDestinations.getInstance().loadDestinations(saver.loadAll());
                return null;
            }

            @Override
            protected void onPostExecute(Object params) {
                SavedDestinations.getInstance().notifyListeners();
            }
        };
        loadDest.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationServicesAPI.getInstance().disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationServicesAPI.getInstance().connect();
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
        if (requestCode == LocationServicesAPI.REQUEST_RESOLVE_ERROR) {
            LocationServicesAPI.getInstance().resolutionResult(resultCode, data);
        }
    }

    /**
     * Sets the current tab to the one with the given index
     * @param index the index of the tab
     */
    public void changeTab(int index){
        viewPager.setCurrentItem(index, true);
    }

    /**
     * @return The currently active tab
     */
    public Fragment getCurrentTab(){
        return ((FragmentAdapter)viewPager.getAdapter()).getRegisteredFragment(viewPager.getCurrentItem());
    }

    @Override
    public void vasttrafikRequestError(String e) {
        Log.e("VastTrafikError",e);
        Toast.makeText(this, "Error with Vasttrafik: " + e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void vasttrafikRequestDone(boolean newPolyline, LatLng... polyline) {
        changeTab(1);
        ((MapFragment) getCurrentTab()).drawPolyLine(newPolyline, polyline);
    }

    @Override
    public void vasttrafikRequestDone(boolean newPolyline, VasttrafikChange... tripInfo){
        changeTab(1);
        ((MapFragment) getCurrentTab()).updateCurrentTrip(newPolyline, tripInfo);
    }
}
