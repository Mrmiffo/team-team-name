package com.teamteamname.gotogothenburg.destination;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamteamname.gotogothenburg.R;

import java.util.List;

/**
 * Adapter for populating a listview.
 * Uses the SavedDestinations object for its underlying data. This means that it needs to query the
 * object when the data set has changed. This is done by registering itself to savedDestinations
 * as a listener.
 *
 * Created by Anton on 2015-09-30.
 */
public class DestinationListAdapter extends BaseAdapter implements ISavedDestinationListener{

    private SavedDestinations savedDestinations;
    private List<Destination> destinations;
    private Activity activity;

    public DestinationListAdapter(Activity activity){
        this.savedDestinations = SavedDestinations.getInstance();
        this.activity = activity;
        destinations = savedDestinations.getSavedDestinations();
        savedDestinations.registerListener(this);
    }
    @Override
    public int getCount() {
        return destinations.size();
    }

    @Override
    public Object getItem(int position) {
        return destinations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        destinations = savedDestinations.getSavedDestinations();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //Setup
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.destination_list_component, parent, false);
        }
        TextView firstLine, secondLine;

        firstLine = (TextView) convertView.findViewById(R.id.firstLine);
        secondLine = (TextView) convertView.findViewById(R.id.secondLine);

        //Change the color of the text of a destination which is visited.
        if (destinations.get(position).isVisited()){
            firstLine.setTextColor(Color.argb(90,0,0,0));
        }

        firstLine.setText(destinations.get(position).getName());
        //TODO make second line display time to destination.
        secondLine.setText("Insert time to destination here!");

        convertView.findViewById(R.id.remove_destination).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedDestinations.getInstance().removeDestination(destinations.get(position));
            }
        });

        return convertView;
    }

    //Update method called once each time when the SavedDestinations has been modified.
    @Override
    public void update() {
        //destinations = savedDestinations.getSavedDestinations();
        notifyDataSetChanged();
    }
}
