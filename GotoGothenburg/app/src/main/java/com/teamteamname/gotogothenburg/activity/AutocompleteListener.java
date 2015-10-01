package com.teamteamname.gotogothenburg.activity;

import android.content.Context;
import android.database.MatrixCursor;
import android.widget.SearchView;

import com.teamteamname.gotogothenburg.api.VasttrafikAPI;
import com.teamteamname.gotogothenburg.api.VasttrafikHandler;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-29.
 */
public class AutocompleteListener implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, VasttrafikHandler {

    private long lastCall;
    private Context context;
    private SearchView searchBar;

    public AutocompleteListener(Context context, SearchView searchBar){
        this.lastCall = 0;
        this.context = context;
        this.searchBar = searchBar;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String input) {
        if(System.currentTimeMillis()-lastCall > 500){
            VasttrafikAPI.getInstance().getAutocomplete(this, input);
        }
        this.lastCall = System.currentTimeMillis();
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        return false;
    }

    @Override
    public void vasttrafikRequestDone(List<String> autocomplete) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "suggestion"});
        for(int i = 0; i < autocomplete.size(); i++){
            cursor.addRow(new Object[]{i, autocomplete.get(i)});
        }
        AutocompleteAdapter autocompleteAdapter = new AutocompleteAdapter(context, cursor, 0, autocomplete);
        searchBar.setSuggestionsAdapter(autocompleteAdapter);
    }

    @Override
    public void vasttrafikRequestError(String e) {

    }
}
