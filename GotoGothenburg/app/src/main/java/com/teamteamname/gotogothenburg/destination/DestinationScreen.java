package com.teamteamname.gotogothenburg.destination;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.Api;
import com.teamteamname.gotogothenburg.api.LocationServicesAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikAPI;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.ErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.TripHandler;
import com.teamteamname.gotogothenburg.guide.GuideHandler;

/**
 * THe fragment used to display the Destination screen in the application.
 * Created by Anton on 2015-09-21.
 */
public class DestinationScreen extends Fragment{

    public DestinationScreen() {
        super();
    }

    /**
     * A method ues in order to set parameters on the object after creation. The FragmentAdapter
     * will call this method instead of the constructor to create the object.
     * @return a new instance of the object.
     */
    public static DestinationScreen newInstance(){
        return new DestinationScreen();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View toReturn = inflater.inflate(R.layout.fragment_destination, container, false);
        //Load the destinations view xml
        final DestinationListAdapter adapter = new DestinationListAdapter(getActivity());

        final ListView destinationListView = (ListView) toReturn.findViewById(R.id.destinationListView);
        destinationListView.setAdapter(adapter);
        destinationListView.setOnItemClickListener(destinationClickListener);

        toReturn.findViewById(R.id.newDestinationButton).setOnClickListener(createDestinationListener);

        return toReturn;
    }

    /**
     * Listener for showing trip in mapfragment based on which destination item was pressed
     */
    final private OnItemClickListener destinationClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Location myLocation = LocationServicesAPI.getInstance().getLastKnownLocation();
            if (myLocation == null) {
                Toast.makeText(getActivity(), "Device Location not found", Toast.LENGTH_SHORT).show();
            } else {
                final LatLng originCoords = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                final String destName = ((Destination) parent.getItemAtPosition(position)).getName();
                final double destLat = ((Destination) parent.getItemAtPosition(position)).getLatitude();
                final double destLng = ((Destination) parent.getItemAtPosition(position)).getLongitude();
                final LatLng destCoord = new LatLng(destLat, destLng);

                Api.getTrip((TripHandler) getActivity(), (ErrorHandler) getActivity(), "Me", originCoords, destName, destCoord);
                GuideHandler.getInstance().startGuide();
            }
        }

    };

    /**
     * Listener which displays the CreateNewDestinationFragment but disallows double clicks
     */
    final private View.OnClickListener createDestinationListener = new View.OnClickListener() {
        private long time;
        @Override
        public void onClick(View v) {
            if (time + 500 < System.currentTimeMillis()) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out_down).add(android.R.id.content, CreateNewDestinationFragment.newInstance()).commit();
                time = System.currentTimeMillis();
            }
        }
    };
}
