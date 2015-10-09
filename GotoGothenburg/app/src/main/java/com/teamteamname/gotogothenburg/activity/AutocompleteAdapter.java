package com.teamteamname.gotogothenburg.activity;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.teamteamname.gotogothenburg.R;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-30.
 */
public class AutocompleteAdapter extends CursorAdapter {

    private List<String> items;
    private TextView text;
    private SearchView searchbar;

    public AutocompleteAdapter(Context context, Cursor c, int flags, List<String> items, SearchView searchbar) {
        super(context, c, flags);
        this.items = items;
        this.searchbar = searchbar;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.autocomplete, parent, false);
        this.text = (TextView) view.findViewById(R.id.autocompleteSuggestion);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        this.text.setText(cursor.getString(cursor.getColumnIndex("suggestion")));
        this.text.setOnClickListener(new SuggestionClickListener(searchbar));
    }
}
