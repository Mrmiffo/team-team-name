package com.teamteamname.gotogothenburg.activity;

import android.content.Context;
import android.database.MatrixCursor;
import android.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.VasttrafikAutocompleteHandler;
import com.teamteamname.gotogothenburg.api.VasttrafikLocation;
import com.teamteamname.gotogothenburg.api.VasttrafikAPI;
import com.teamteamname.gotogothenburg.api.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.VasttrafikTripHandler;
import com.teamteamname.gotogothenburg.map.MapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-29.
 */
public class SearchbarListener implements SearchView.OnQueryTextListener, VasttrafikErrorHandler, VasttrafikAutocompleteHandler, VasttrafikTripHandler {

    private long lastCall;
    private Context context;
    private SearchView searchbar;
    private VasttrafikLocation[] locations;

    public SearchbarListener(Context context, SearchView searchbar){
        this.lastCall = 0;
        this.context = context;
        this.searchbar = searchbar;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        for(VasttrafikLocation location : locations){
            if(location.getName().equals(query)){
                double lat = LocationServicesAPI.getInstance().getLastKnownLocation().getLatitude();
                double lng = LocationServicesAPI.getInstance().getLastKnownLocation().getLongitude();
                VasttrafikAPI.getInstance().getCoordinates(this, this, new VasttrafikLocation("origin", lat, lng), location);
                searchbar.clearFocus();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String input) {
        if(System.currentTimeMillis()-lastCall > 500){
            VasttrafikAPI.getInstance().getAutocomplete(this, this, input);
        }
        this.lastCall = System.currentTimeMillis();
        return true;
    }

    @Override
    public void vasttrafikRequestDone(VasttrafikLocation... autocomplete) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "suggestion"});
        this.locations = autocomplete;
        List<String> temp = new ArrayList<>();
        for(int i = 0; i < autocomplete.length; i++){
            cursor.addRow(new Object[]{i, autocomplete[i].getName()});
            temp.add(autocomplete[i].getName());
        }
        AutocompleteAdapter autocompleteAdapter = new AutocompleteAdapter(context, cursor, 0, temp, searchbar);
        this.searchbar.setSuggestionsAdapter(autocompleteAdapter);
    }


    @Override
    public void vasttrafikRequestDone(boolean newPolyline, LatLng... polyline){
        ((MainActivity)context).changeTab(1);
        ((MapFragment)((MainActivity)context).getCurrentTab()).drawPolyLine(newPolyline, polyline);
    }

    @Override
    public void vasttrafikRequestError(String e) {

    }
}
