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
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.AndroidDeviceAPI;
import com.teamteamname.gotogothenburg.api.Api;
import com.teamteamname.gotogothenburg.api.Bus;
import com.teamteamname.gotogothenburg.api.electricity.ElectricityAPI;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.ErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.TripHandler;
import com.teamteamname.gotogothenburg.destination.DestinationSaver;
import com.teamteamname.gotogothenburg.destination.SavedDestinations;
import com.teamteamname.gotogothenburg.guide.GuideHandler;
import com.teamteamname.gotogothenburg.map.MapScreen;

public class MainActivity extends FragmentActivity implements TripHandler, ErrorHandler {

    private ViewPager viewPager;
    private boolean guideWasStopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the ViewPager and set an adapter
        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(new FragmentAdapter(getFragmentManager()));
        this.viewPager = pager;

        // Bind the tabs to the ViewPager
        final PagerSlidingTabStrip pagerTitleStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerTitleStrip.setViewPager(pager);

        // Initialize the APIs
        Api.init(this);
        AndroidDeviceAPI.init(this);
        LocationServicesAPI.init(this);
        GuideHandler.init(this);
        Bus.init();

        // Adding listeners to searchbar
        final SearchView searchBar = (SearchView)findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchbarListener(this, searchBar));
        final SearchManager manager = (SearchManager) getSystemService(Activity.SEARCH_SERVICE);
        searchBar.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        //Fill the saved destinations object with data from the database.
        //Create a saver for the SavedDestinations
        final DestinationSaver saver = new DestinationSaver(this);
        SavedDestinations.init(saver);
        final AsyncTask loadDest = new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                //Initialize the SavedDestinations with destinations from the database. (This must run in background)
                SavedDestinations.getInstance().loadDestinations(saver.loadAll());
                return null;
            }

            @Override
            protected void onPostExecute(Object params) {
                SavedDestinations.getInstance().notifyListeners();
            }
        };
        //noinspection unchecked
        loadDest.execute();
    }

    @Override
    protected void onDestroy(){
        //Turning off the guide and all it's threads if the application is destroyed. Do not set guideWasStopped as the application itself was killed.
        GuideHandler.getInstance().stopGuide();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        //Turning off the guide and all it's threads if the application is paused.
        GuideHandler.getInstance().stopGuide();
        guideWasStopped = true;
        super.onPause();
        LocationServicesAPI.getInstance().disconnect();
    }

    @Override
    public void onResume() {
        //Restart the guide if it was stopped by the pause.
        if (guideWasStopped){
            GuideHandler.getInstance().startGuide();
            guideWasStopped = false;
        }
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
        final int id = item.getItemId();

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
    public void requestError(String e) {
        Log.e("VastTrafikError",e);
        Toast.makeText(this, "Error with Vasttrafik: " + e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestDone(String[] lines, String[] stopNames, String[] tracks, LatLng[] positions, PolylineOptions[] polyline){
        changeTab(1);
        ((MapScreen) getCurrentTab()).updateCurrentTrip(lines, stopNames, tracks, positions);
        ((MapScreen) getCurrentTab()).drawPolyLine(polyline);
    }
}
