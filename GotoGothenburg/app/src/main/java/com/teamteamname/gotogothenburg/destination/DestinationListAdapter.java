package com.teamteamname.gotogothenburg.destination;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamteamname.gotogothenburg.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Anton on 2015-09-30.
 */
public class DestinationListAdapter extends BaseAdapter{
    final private SavedDestinations destinations;
    final private List<Destination> currentSorting;
    final private Activity activity;

    public DestinationListAdapter(SavedDestinations destinations, Activity activity){
        this.destinations = destinations;
        this.activity = activity;
        currentSorting = new ArrayList<>();
        currentSorting.addAll(destinations.getVisited(false));
        currentSorting.addAll(destinations.getVisited(true));
    }
    @Override
    public int getCount() {
        return currentSorting.size();
    }

    @Override
    public Object getItem(int position) {
        return currentSorting.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Setup
        final LayoutInflater inflater = activity.getLayoutInflater();
        final View row = inflater.inflate(R.layout.destination_list_component, parent, false);
        TextView firstLine;
        TextView secondLine;

        firstLine = (TextView) row.findViewById(R.id.firstLine);
        secondLine = (TextView) row.findViewById(R.id.secondLine);

        if (currentSorting.get(position).isVisited()){
            firstLine.setTextColor(Color.argb(90,0,0,0));
        }

        firstLine.setText(currentSorting.get(position).getName());
        //TODO make second line display time to destination.
        secondLine.setText("Insert time to destination here!");
        return row;
    }
}
