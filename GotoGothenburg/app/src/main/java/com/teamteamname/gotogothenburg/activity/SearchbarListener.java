package com.teamteamname.gotogothenburg.activity;

import android.content.Context;
import android.database.MatrixCursor;
import android.location.Location;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikAutocompleteHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikLocation;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikTripHandler;
import com.teamteamname.gotogothenburg.map.MapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-29.
 */
public class SearchbarListener implements SearchView.OnQueryTextListener, VasttrafikAutocompleteHandler {

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
                Location myLocation = LocationServicesAPI.getInstance().getLastKnownLocation();
                if (myLocation != null) {
                    VasttrafikAPI.getInstance().getCoordinates(
                            (MainActivity) context,
                            (MainActivity) context,
                            new VasttrafikLocation("origin", myLocation.getLatitude(), myLocation.getLongitude()),
                            location);

                    searchbar.clearFocus();
                    return true;
                } else {
                    Toast.makeText(context, "Device Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String input) {
        if(System.currentTimeMillis()-lastCall > 500){
            VasttrafikAPI.getInstance().getAutocomplete(this, (MainActivity)context, input);
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


}
