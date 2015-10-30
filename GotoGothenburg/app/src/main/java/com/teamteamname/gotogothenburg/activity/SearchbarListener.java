package com.teamteamname.gotogothenburg.activity;

import android.content.Context;
import android.database.MatrixCursor;
import android.location.Location;
import android.util.Pair;
import android.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.api.ApiFactory;
import com.teamteamname.gotogothenburg.api.AutocompleteHandler;

/**
 * Created by Mattias Ahlstedt on 2015-09-29.
 */
public class SearchbarListener implements SearchView.OnQueryTextListener, AutocompleteHandler {

    private long lastCall;
    final private Context context;
    final private SearchView searchbar;
    private Pair<String, LatLng>[] locations;

    public SearchbarListener(Context context, SearchView searchbar){
        this.lastCall = 0;
        this.context = context;
        this.searchbar = searchbar;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        for(final Pair<String, LatLng> location : locations){
            if(location.first.equals(query)){
                Location myLocation = ApiFactory.getInstance().createILocationServices().getLastKnownLocation();
                if (myLocation != null) {
                    final LatLng origin = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    ApiFactory.getInstance().createITrip().getTrip((MainActivity) context, (MainActivity) context,
                            "Me", origin, location.first, location.second);
                    searchbar.clearFocus();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String input) {
        if(System.currentTimeMillis()-lastCall > 500){
            ApiFactory.getInstance().createIAutocomplete().getAutocomplete(this, (MainActivity) context, input);
        }
        this.lastCall = System.currentTimeMillis();
        return true;
    }

    @Override
    public void requestDone(Pair<String, LatLng>... suggestions) {
        final MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "suggestion"});
        locations = new Pair[suggestions.length];
        for(int i = 0; i < locations.length; i++){
            locations[i] = new Pair<>(suggestions[i].first, suggestions[i].second);
        }

        for(int i = 0; i < locations.length; i++){
            cursor.addRow(new Object[]{i, locations[i].first});
        }
        final AutocompleteAdapter autocompleteAdapter = new AutocompleteAdapter(context, cursor, 0, searchbar);
        this.searchbar.setSuggestionsAdapter(autocompleteAdapter);
    }
}
