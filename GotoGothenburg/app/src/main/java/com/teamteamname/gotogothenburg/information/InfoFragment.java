package com.teamteamname.gotogothenburg.information;

import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.map.Bus;
import com.teamteamname.gotogothenburg.map.IOnWhichBusListener;
import com.teamteamname.gotogothenburg.map.OnWhichBusIdentifier;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View toReturn = inflater.inflate(R.layout.fragment_info, container, false);

        //TODO Remove test code
        //--TEST CODE START--
        OnWhichBusIdentifier.getInstance().registerListener(new BusStatusAPIListener());
        Button testButton = (Button) toReturn.findViewById(R.id.systemIdTestButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (OnWhichBusIdentifier.getInstance().isRunning()){
                OnWhichBusIdentifier.getInstance().stop();
            } else {
                OnWhichBusIdentifier.getInstance().start();
            }
            }
        });

        //--TEST CODE END--
        return toReturn;
    }
    //TODO remove test code
    //--TEST CODE START--
    private class BusStatusAPIListener implements IOnWhichBusListener{


        @Override
        public void whichBussCallBack(Bus busUserIsOn) {
            Log.e("User is on bus: ", busUserIsOn.getRegNr());
        }

        @Override
        public void notConnectedToElectriCityWifiError() {
            Log.e("Not connected to Wifi", "The user was not connected to the wifi.");
        }
    }
    //--TEST CODE END--


}
