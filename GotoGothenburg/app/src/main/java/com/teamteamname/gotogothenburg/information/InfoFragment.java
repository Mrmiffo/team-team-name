package com.teamteamname.gotogothenburg.information;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.teamteamname.gotogothenburg.API.BusStatusAPI;
import com.teamteamname.gotogothenburg.API.IBusStatusHandler;
import com.teamteamname.gotogothenburg.R;

/**
 * Created by Anton on 2015-09-21.
 */
public class InfoFragment extends Fragment {
    public InfoFragment() {}

    public static InfoFragment newInstance(){
        InfoFragment toReturn = new InfoFragment();
        return toReturn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View toReturn = inflater.inflate(R.layout.fragment_info, container, false);

        //TODO Remove test code
        //--TEST CODE START--

        Button testButton = (Button) toReturn.findViewById(R.id.systemIdTestButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStatusAPI.getInstance().getConnectedBusSystemID(new BusStatusAPIListener());
            }
        });

        //--TEST CODE END--
        return toReturn;
    }
    //TODO remove test code
    //--TEST CODE START--
    private class BusStatusAPIListener implements IBusStatusHandler{

        @Override
        public void getConnectedBusSystemIDCallback(String returnValue) {

        }
    }
    //--TEST CODE END--


}
