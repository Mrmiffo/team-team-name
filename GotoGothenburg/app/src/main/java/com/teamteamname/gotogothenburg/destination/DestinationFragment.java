package com.teamteamname.gotogothenburg.destination;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.teamteamname.gotogothenburg.R;

/**
 * THe fragment used to display the Destination screen in the application.
 * Created by Anton on 2015-09-21.
 */
public class DestinationFragment extends Fragment {

    private ListView destinationListView;
    private SavedDestinations savedDestinations;

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
        savedDestinations.addDestination(new Destination("Testination4", 1,2));
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
        savedDestinations.addDestination(new Destination("Testination4", 1,2));


        //TEST CODE//

        destinationListView = (ListView) toReturn.findViewById(R.id.destinationListView);
        DestinationListAdapter adapter = new DestinationListAdapter(savedDestinations, getActivity());
        destinationListView.setAdapter(adapter);
        destinationListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO implement on click action, such as generate a guide or give directions.
            }
        });
        return toReturn;
    }

    public void displayDestination(Destination destToDisplay){

    }

}
