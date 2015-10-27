package com.teamteamname.gotogothenburg.activity;

import android.content.Context;
import android.database.MatrixCursor;
import android.location.Location;
import android.util.Pair;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.AutocompleteHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-29.
 */
public class SearchbarListener implements SearchView.OnQueryTextListener, AutocompleteHandler {

    private long lastCall;
    private Context context;
    private SearchView searchbar;
    private Pair<String, LatLng>[] locations;

    public SearchbarListener(Context context, SearchView searchbar){
        this.lastCall = 0;
        this.context = context;
        this.searchbar = searchbar;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        for(Pair<String, LatLng> location : locations){
            if(location.first.equals(query)){
                Location myLocation = LocationServicesAPI.getInstance().getLastKnownLocation();
                if (myLocation != null) {
                    LatLng origin = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    VasttrafikAPI.getTripInstance().getTrip(
                            (MainActivity) context, (MainActivity) context,
                            "Me", origin, location.first, location.second);
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
            VasttrafikAPI.getAutocompleteInstance().getAutocomplete(this, (MainActivity)context, input);
        }
        this.lastCall = System.currentTimeMillis();
        return true;
    }

    @Override
    public void RequestDone(Pair<String, LatLng>[] suggestions) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "suggestion"});

        locations = new Pair[suggestions.length];
        for(int i = 0; i < locations.length; i++){
            locations[i] = new Pair<>(suggestions[i].first, suggestions[i].second);
        }

        List<String> temp = new ArrayList<>();
        for(int i = 0; i < locations.length; i++){
            cursor.addRow(new Object[]{i, locations[i].first});
            temp.add(locations[i].first);
        }
        AutocompleteAdapter autocompleteAdapter = new AutocompleteAdapter(context, cursor, 0, temp, searchbar);
        this.searchbar.setSuggestionsAdapter(autocompleteAdapter);
    }
}
