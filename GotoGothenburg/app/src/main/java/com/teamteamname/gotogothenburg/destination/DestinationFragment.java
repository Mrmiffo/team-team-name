package com.teamteamname.gotogothenburg.destination;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.activity.MainActivity;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.VasttrafikAPI;
import com.teamteamname.gotogothenburg.api.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.VasttrafikLocation;
import com.teamteamname.gotogothenburg.api.VasttrafikTripHandler;
import com.teamteamname.gotogothenburg.map.MapFragment;

/**
 * THe fragment used to display the Destination screen in the application.
 * Created by Anton on 2015-09-21.
 */
public class DestinationFragment extends Fragment{

    private ListView destinationListView;
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
        adapter = new DestinationListAdapter(getActivity());
        destinationListView.setAdapter(adapter);

        destinationListView.setOnItemClickListener(new DestinationClickListener());

        toReturn.findViewById(R.id.newDestinationButton).setOnClickListener(createDestinationListener);

        return toReturn;
    }

    private class DestinationClickListener implements OnItemClickListener, VasttrafikTripHandler, VasttrafikErrorHandler{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            double originLat = LocationServicesAPI.getInstance().getLastKnownLocation().getLatitude();
            double originLng = LocationServicesAPI.getInstance().getLastKnownLocation().getLongitude();
            VasttrafikLocation origin = new VasttrafikLocation("origin", originLat, originLng);

            double destLat = ((Destination)parent.getItemAtPosition(position)).getLatitude();
            double destLng = ((Destination)parent.getItemAtPosition(position)).getLongitude();
            String name = ((Destination)parent.getItemAtPosition(position)).getName();
            VasttrafikLocation dest = new VasttrafikLocation(name, destLat, destLng);

            VasttrafikAPI.getInstance().getCoordinates(this, this, origin, dest);
        }

        @Override
        public void vasttrafikRequestDone(boolean newPolyline, int r, int g, int b, LatLng... polyline) {
            ((MainActivity)getActivity()).changeTab(1);
            ((MapFragment)((MainActivity)getActivity()).getCurrentTab()).drawPolyLine(newPolyline, r, g, b, polyline);
        }

        @Override
        public void vasttrafikRequestError(String e) {

        }
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
