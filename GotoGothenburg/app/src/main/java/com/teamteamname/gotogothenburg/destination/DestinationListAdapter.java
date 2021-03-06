package com.teamteamname.gotogothenburg.destination;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamteamname.gotogothenburg.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
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

    final private SavedDestinations savedDestinations;
    private List<Destination> destinations;
    final private Activity activity;

    public DestinationListAdapter(Activity activity){
        this.savedDestinations = SavedDestinations.getInstance();
        this.activity = activity;
        destinations = savedDestinations.getSavedDestinations();
        savedDestinations.registerListener(this);
    }

    //TODO Make proper sorting method. Currently using visited, even though this is not yet implemented.
    //Local method used to sort all the locations by if they are visited or not.
    private void sort() {
        final ArrayList<Destination> visited = new ArrayList<>();
        final ArrayList<Destination> notVisited = new ArrayList<>();
        for (final Destination dest:destinations){
            if (dest.isVisited()){
                visited.add(dest);
            } else {
                notVisited.add(dest);
            }
        }
        destinations = new ArrayList<>();
        destinations.addAll(visited);
        destinations.addAll(notVisited);
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
        sort();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //Setup
            final LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.destination_list_component, parent, false);
        }

        TextView firstLine = (TextView) convertView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) convertView.findViewById(R.id.secondLine);

        //Change the color of the text of a destination which is visited.
        int colorToSetToLine;
        if (destinations.get(position).isVisited()){
            colorToSetToLine = Color.argb(90, 0, 0, 0);
        } else {
            colorToSetToLine = Color.argb(150,0,0,0);
        }
        firstLine.setTextColor(colorToSetToLine);
        secondLine.setTextColor(colorToSetToLine);

        firstLine.setText(destinations.get(position).getName());
        //TODO make second line display time to destination.
        // This text has been left empty instead of removing the text field object as the application has been tested
        // and verifgied with the text field in place even if it has not been properly implemented.
        secondLine.setText(" ");

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
