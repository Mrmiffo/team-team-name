package com.teamteamname.gotogothenburg.destination;

import android.app.Fragment;
import android.location.Location;
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
        View toReturn = inflater.inflate(R.layout.fragment_destination, container, false);



        //Setup the destinations view
        //TODO REMOVE TESTCODE
        //TEST CODE//
        savedDestinations = new SavedDestinations();
        savedDestinations.addDestination(new Destination("Testination", 1,2));
        savedDestinations.addDestination(new Destination("Testination2", 1,2));
        savedDestinations.addDestination(new Destination("Testination3", 1,2));
        savedDestinations.addDestination(new Destination("Testination4", 1, 2));
        savedDestinations.addDestination(new Destination("Testination", 1,2));

        Destination visited = new Destination("Visitation", 3,4);
        visited.setVisited(true);
        savedDestinations.addDestination(visited);

        savedDestinations.addDestination(new Destination("Testination2", 1,2));
        savedDestinations.addDestination(new Destination("Testination3", 1,2));
        savedDestinations.addDestination(new Destination("Testination4", 1,2));
        savedDestinations.addDestination(new Destination("Testination", 1,2));
        savedDestinations.addDestination(new Destination("Testination2", 1,2));
        savedDestinations.addDestination(new Destination("Testination3", 1,2));
        savedDestinations.addDestination(new Destination("Testination4", 1,2));
        savedDestinations.addDestination(new Destination("Testination", 1,2));
        savedDestinations.addDestination(new Destination("Testination2", 1,2));
        savedDestinations.addDestination(new Destination("Testination3", 1,2));
        savedDestinations.addDestination(new Destination("Testination4", 1, 2));


        //TEST CODE//

        destinationListView = (ListView) toReturn.findViewById(R.id.destinationListView);
        adapter = new DestinationListAdapter(savedDestinations, getActivity());
        destinationListView.setAdapter(adapter);
        destinationListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO implement on click action, such as generate a guide or give directions.
            }
        });

        toReturn.findViewById(R.id.newDestinationButton).setOnClickListener(createDestinationListener);

        return toReturn;
    }

    public void displayDestination(Destination destToDisplay){
    }

    /**
     * Creates a new Destination, adds it to the internal list and displays it.
     *
     * @param destinationName The name of the Destination
     * @param destinationPos The position of the Destination
     */
    public void addDestination(String destinationName, LatLng destinationPos){
        savedDestinations.addDestination(new Destination(destinationName,destinationPos.latitude,destinationPos.longitude));
        adapter.notifyDataSetChanged();
    }

    /*
    Listener which displays the CreateNewDestinationFragment but disallows double clicks
     */
    private View.OnClickListener createDestinationListener = new View.OnClickListener() {
        private long time = 0;
        @Override
        public void onClick(View v) {
            if (time + 500 < System.currentTimeMillis()) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out_down).add(android.R.id.content, CreateNewDestinationFragment.newInstance(DestinationFragment.this)).commit();
                time = System.currentTimeMillis();
            }
        }
    };

}
