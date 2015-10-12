package com.teamteamname.gotogothenburg.destination;

import android.app.Fragment;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.R;

import java.util.Timer;

/**
 * THe fragment used to display the Destination screen in the application.
 * Created by Anton on 2015-09-21.
 */
public class DestinationFragment extends Fragment {

    private ListView destinationListView;
    private SavedDestinations savedDestinations;
    private DestinationListAdapter adapter;

    public DestinationFragment() {
        super();
    }

    /**
     * A method ues in order to set parameters on the object after creation. The FragmentAdapter
     * will call this method instead of the constructor to create the object.
     * @return a new instance of the object.
     */
    public static DestinationFragment newInstance(){
        DestinationFragment toReturn = new DestinationFragment();
        return toReturn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View toReturn = inflater.inflate(R.layout.fragment_destination, container, false);
        //Load the destinations view xml
        destinationListView = (ListView) toReturn.findViewById(R.id.destinationListView);
        //Fill the saved destinations object with data from the database.
        //Create a saver for the SavedDestinations
        final DestinationSaver saver = new DestinationSaver(getActivity());
        SavedDestinations.init(saver);
        adapter = new DestinationListAdapter(getActivity());
        AsyncTask loadDest = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                //Initialize the SavedDestinations with destinations from the database. (This must run in background)
                SavedDestinations.getInstance().loadDestinations(saver.loadAll());
                return null;
            }
        };
        loadDest.execute();

        destinationListView.setAdapter(adapter);
        destinationListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO implement on click action, such as generate a route or give directions.

                //TODO Remove test code.
                //--TEST CODE--
                Destination dest = (Destination) parent.getItemAtPosition(position);
                Log.e("Name", dest.getName());
                Log.e("Lat", String.valueOf(dest.getLatitude()));
                Log.e("Long", String.valueOf(dest.getLongitude()));
                Log.e("Visited", String.valueOf(dest.isVisited()));
                //--TEST CODE--
            }
        });


        toReturn.findViewById(R.id.newDestinationButton).setOnClickListener(createDestinationListener);

        return toReturn;
    }

    /*
    Listener which displays the CreateNewDestinationFragment but disallows double clicks
     */
    private View.OnClickListener createDestinationListener = new View.OnClickListener() {
        private long time = 0;
        @Override
        public void onClick(View v) {
            if (time + 500 < System.currentTimeMillis()) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out_down).add(android.R.id.content, CreateNewDestinationFragment.newInstance()).commit();
                time = System.currentTimeMillis();
            }
        }
    };

}
