package com.teamteamname.gotogothenburg.activity;


import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Created by Mattias Ahlstedt on 2015-10-02.
 */
public class SuggestionClickListener implements View.OnClickListener {

    final private SearchView searchbar;

    public SuggestionClickListener(SearchView searchbar){
        this.searchbar = searchbar;
    }

    @Override
    public void onClick(View v) {
        final TextView text = (TextView) v;
        this.searchbar.setQuery(text.getText(), true);
    }
}
